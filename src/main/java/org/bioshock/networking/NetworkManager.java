package org.bioshock.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

import org.bioshock.engine.core.ChatManager;
import org.bioshock.entities.Entity;
import org.bioshock.entities.SquareEntity;
import org.bioshock.entities.players.Hider;
import org.bioshock.entities.players.SeekerAI;
import org.bioshock.gui.LobbyController;
import org.bioshock.main.App;
import org.bioshock.networking.Message.ClientInput;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class NetworkManager {

    /** Seconds between game server ping time updates */
    private static final int PING_UPDATE_RATE = 1;

    /** ID of local player */
    private static String myID = UUID.randomUUID().toString();

    /** Name of local player */
    private static String myName;

    /** Local player */
    private static Hider me;

    /** Player which sends information of seekers */
    private static Hider masterHider;

    /** List of seekers in game */
    private static List<SeekerAI> seekers = new ArrayList<>();

    /** List of players in game */
    private static List<Hider> playerList = new ArrayList<>();

    /** Maps UUID to player that is loaded into game */
    private static Map<String, Hider> loadedPlayers = new HashMap<>();

    /** Maps {@link Hider} to its chosen name */
    private static Map<Hider, String> playerNames = new HashMap<>();

    /** A queue of messages sent in chat */
    private static Queue<String> chatMessageList = new ArrayDeque<>();

    /** A client using default URI */
    private static Client client = new Client();

    /**
     * A lock used whilst waiting for an appropriate number of players to join
     * the lobby
     */
    private static Object awaitingPlayerLock = new Object();

    /**
     * Thread used to wait for players to join
     */
    private static Thread initThread;

    /**
     * Maps each player to their ping
     */
    private static Map<Hider, IntegerProperty> pingMap = new HashMap<>();

    /** Nano Time of previous ping to game server */
    private static long previousPing;


    /** NetworkManager is a static class */
    private NetworkManager() {}

    public static void initialise() {
        initThread = new Thread(() -> {
            try {
                App.logger.info("Connecting to web socket...");
                client.connectBlocking();
                App.logger.info("Connected to web socket");
            } catch (InterruptedException e) {
                App.logger.error(
                    "Thread was interrupted whilst connecting to server"
                );
                Thread.currentThread().interrupt();
                return;
            }

            myName = client.getPlayerName();
            client.send(Integer.toString(App.playerCount()));

            /* Wait until players join then add them to loadedPlayers */
            while (loadedPlayers.size() < App.playerCount()) {
                synchronized (awaitingPlayerLock) {
                    while (client.getInitialMessages().isEmpty()) {
                        try {
                            awaitingPlayerLock.wait();
                        } catch (InterruptedException e) {
                            App.logger.error(
                                "Thread was interrupted whilst waiting for"
                                + " players to join. This is usually due"
                                + " to a change of lobby size"
                            );
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                }

                Message message = client.getInitialMessages().remove();

                Hider hider = playerList.get(message.playerNumber - 1);

                hider.setID(message.uuid);

                hider.setName(message.name);

                loadedPlayers.putIfAbsent(message.uuid, hider);
                playerNames.putIfAbsent(hider, message.name);
                pingMap.putIfAbsent(hider, new SimpleIntegerProperty(0));

                if (App.getFXMLController() instanceof LobbyController) {
                    LobbyController lobbyController =
                        (LobbyController) App.getFXMLController();

                    Platform.runLater(() ->
                        lobbyController.updatePlayerCount(
                            loadedPlayers.size()
                        )
                    );
                } else {
                    App.logger.error(
                        "Tried to get LobbyController whilst not in lobby"
                    );
                }
            }

            masterHider = playerList.get(0);

            me = loadedPlayers.get(myID);

            me.getMovement().initMovement();

            playerList.forEach(Hider::initAnimations);
            seekers.forEach(SeekerAI::initAnimations);

            Timeline pingTimeline = new Timeline(new KeyFrame(
                Duration.seconds(PING_UPDATE_RATE),
                e -> NetworkManager.sendPing()
            ));
            pingTimeline.setCycleCount(Animation.INDEFINITE);
            pingTimeline.play();

            App.logger.info("Networking initialised");
        });

        initThread.start();
    }


    /**
     * Gathers information to send to the server about the local player
     * @return The {@link Message} to send to the server
     */
    private static Message pollInputs() {
        int x = (int) me.getX();
        int y = (int) me.getY();

        int[][] aiCoords = new int[seekers.size()][2];
        for (int i = 0; i < seekers.size(); i++) {
            Point2D seekerPos = seekers.get(i).getPosition();
            aiCoords[i][0] = (int) seekerPos.getX();
            aiCoords[i][1] = (int) seekerPos.getY();
        }

        String message = "";

        if (!chatMessageList.isEmpty() && !me.isDead()) {
            message = chatMessageList.poll();
        }

        Message.ClientInput input = new Message.ClientInput(
            x,
            y,
            aiCoords,
            message
        );

        return new Message(-1, myID, myName, input, me.isDead());
    }


    /**
     * Called every game tick, does the following:
     * <ul>
     *  <li>Sends information about local player</li>
     *  <li>Updates non-local player's positions and animations</li>
     *  <li>Updates whether non-local player's are dead</li>
     *  <li>Updates the chat</li>
     * </ul>
     */
    public static void tick() {
        if (me != null) client.send(Message.serialise(pollInputs()));

        try {
            client.getMutex().acquire();
        } catch(InterruptedException e) {
            App.logger.error(e);
            client.getMutex().release();
            Thread.currentThread().interrupt();
        }

        Message message;
        while ((message = client.getMessageQ().poll()) != null) {
            /* The hider the message came from */
            Hider messageFrom = loadedPlayers.get(message.uuid);

            ClientInput input = message.input;

            if (input == null && message.dead) messageFrom.setDead(true);

            if (
                input != null
                && input.message != null
                && !input.message.isEmpty()
            ) {
                sendChat(message);
            }

            if (messageFrom == me || input == null) continue;

            updateDirection(input, messageFrom);

            NetworkManager.setPing(messageFrom, input.ping);

            if (
                messageFrom == masterHider
                && input.aiCoords != null
                && input.aiCoords.length == seekers.size()
            ) {
                for (int i = 0; i < seekers.size(); i++) {
                    seekers.get(i).getMovement().moveTo(
                        input.aiCoords[i][0],
                        input.aiCoords[i][1]
                    );
                }
            }

            messageFrom.getMovement().moveTo(
                input.x,
                input.y
            );
        }

        client.getMutex().release();
    }


    /**
     * Updates chat
     * @param message The message to add to chat
     */
    private static void sendChat(Message message) {
        ClientInput input = message.input;

        ChatManager.incomingMessage(input.message);
    }


    /**
     * Updates the
     * {@link org.bioshock.physics.Movement#direction Movement.direction}
     * of each {@link Entity}
     * @param input Information about the player
     * @param messageFrom The player to update
     * @see Message.ClientInput
     */
    private static void updateDirection(ClientInput input, Hider messageFrom) {
        int dispX = (int) (input.x - messageFrom.getX());
        if (dispX != 0) dispX = dispX / Math.abs(dispX);

        int dispY = (int) (input.y - messageFrom.getY());
        if (dispY != 0) dispY = dispY / Math.abs(dispY);

        messageFrom.getMovement().direction(dispX, dispY);
    }


    /**
     * Adds {@link Entity} to appropriate {@link Collection Collections(s)}
     * @param entity To add to {@link Collection Collections(s)}
     */
    public static void register(SquareEntity entity) {
        if (entity instanceof Hider) {
            playerList.add((Hider) entity);
        }
        else if (entity instanceof SeekerAI) {
            seekers.add((SeekerAI) entity);
        }
        else {
            App.logger.error("Tried to register non player entity {}", entity);
        }
    }


    /**
     * Adds {@link Entity Entities} to appropriate
     * {@link Collection Collections(s)}
     * @param entities To add to {@link Collection Collections(s)}
     */
    public static void registerAll(Collection<SquareEntity> entities) {
        entities.forEach(NetworkManager::register);
    }


    /**
     * Removes {@link Entity} from appropriate
     * {@link Collection Collections(s)}
     * @param entities To remove from {@link Collection Collections(s)}
     */
    public static void unregister(Entity entity) {
        playerList.remove(entity);
        loadedPlayers.remove(entity.getID());

        if (entity == seekers) seekers = null;
    }


    /**
     * Removes {@link Entity Entities} from appropriate
     * {@link Collection Collections(s)}
     * @param entities To remove from {@link Collection Collections(s)}
     */
    public static void unregisterAll(Collection<Entity> entities) {
       entities.forEach(NetworkManager::unregister);
    }


    /**
     * Sends a message to server specifying which player has been killed
     * @param hider The player that has been killed
     */
    public static void kill(Hider hider) {
        client.send(Message.serialise(
                new Message(
                        -1,
                        hider.getID(),
                        playerNames.get(hider),
                        null,
                        true
                )
        ));

        if (hider == me) {
            try {
                URL url = new URL("http://recklessgame.net:8034/increaseScore");
                String jsonInputString = "{\"Token\":\"" + Account.getToken() + "\",\"Score\":\"" + Integer.toString(Account.getScoreToInc()) + "\"}";
                byte[] postDataBytes = jsonInputString.getBytes("UTF-8");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("PUT");
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setDoOutput(true);
                con.getOutputStream().write(postDataBytes);
                Reader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                Account.setScore(Account.getScoreToInc() + Account.getScore());
                Account.setScoreToInc(0);
            } catch (IOException e) {
                App.logger.error(e);
            }

        }
    }


    /**
     * Adds a message to {@link #chatMessageList}
     * @param message To be added to {@link #chatMessageList}
     */
    public static void addMessage(String message) {
        if (!message.isEmpty()) {
            chatMessageList.add(message);
        }
    }


    /**
     * Sends ping to game server
     */
    private static void sendPing() {
        client.sendPing();
        previousPing = System.nanoTime();
    }


    /**
     * Updates the players mapped ping time to game server
     * @param hider
     * @param ping
     */
    public static void setPing(Hider hider, int ping) {
        pingMap.get(hider).set(ping);
    }


    /**
     * @return The number of players loaded correctly in lobby
     */
    public static int playerCount() {
        return loadedPlayers.size();
    }


    /**
     * @return The local player's {@link Hider}
     */
    public static Hider me() {
        return me;
    }


    /**
     * @return ID of local player
     */
    public static String getMyID() {
        return myID;
    }


    /**
     * @return The lock used whilst waiting for an appropriate number of
     * players to join the lobby
     */
    public static Object getPlayerJoinLock() {
        return awaitingPlayerLock;
    }


    /**
     * @return A map of players to their ping
     */
    public static Map<Hider, IntegerProperty> getPingMap() {
        return pingMap;
    }


    /**
     * @return Nano time of previous ping to game server
     */
    public static long getPreviousPingTime() {
        return previousPing;
    }


    /**
     * Stops thread responsible for waiting for players to join
     */
    public static void reset() {
        if (initThread != null) {
            initThread.interrupt();
            initThread = null;
        } else {
            App.logger.error(
                "Tried to kill networking thread before initalisation"
            );
        }

        client.close();
        client = new Client();
        loadedPlayers.clear();
    }
}
