package org.bioshock.networking;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

import org.bioshock.entities.Entity;
import org.bioshock.entities.SquareEntity;
import org.bioshock.entities.players.Hider;
import org.bioshock.entities.players.SeekerAI;
import org.bioshock.main.App;
import org.bioshock.networking.Message.ClientInput;
import org.bioshock.scenes.SceneManager;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Point2D;

public class NetworkManager {

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


    /** NetworkManager is a static class */
    private NetworkManager() {}


    /**
     * Creates a new thread that connects to game server and awaits joining
     * players
     * @see Client#DEF_URI
     */
    public static void initialise() {
        Thread initThread = new Thread(new Task<>() {
            @Override
            protected Object call() {
                try {
                    App.logger.info("Connecting to web socket...");
                    client.connectBlocking();
                    App.logger.info("Connected to web socket");
                } catch (InterruptedException e) {
                    App.logger.error(e);
                    Thread.currentThread().interrupt();
                }

                myName = client.getPlayerName();
                client.send(Integer.toString(App.playerCount()));

                /* Wait until players join then add them to loadedPlayers */
                while (loadedPlayers.size() < App.playerCount()) {
                    synchronized(awaitingPlayerLock) {
                        while (client.getInitialMessages().isEmpty()) {
                            try {
                                awaitingPlayerLock.wait();
                            } catch (InterruptedException e) {
                                App.logger.error(e);
                                Thread.currentThread().interrupt();
                            }
                        }
                    }

                    Message message = client.getInitialMessages().remove();

                    Hider hider = playerList.get(message.playerNumber - 1);

                    hider.setID(message.uuid);

                    hider.setName(message.name);

                    loadedPlayers.putIfAbsent(message.uuid, hider);
                    playerNames.putIfAbsent(hider, message.name);

                    Platform.runLater(() ->
                        SceneManager.getLobby().updatePlayerCount(newCount)
                    );
                }

                masterHider = playerList.get(0);

                me = loadedPlayers.get(myID);

                me.getMovement().initMovement();

                playerList.forEach(Hider::initAnimations);
                seekers.forEach(SeekerAI::initAnimations);

                App.logger.info("Networking initialised");

                return null;
            }
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
            message = chatMessageList.peek();
            chatMessageList.poll();
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

            if (
                messageFrom == masterHider
                && input.aiCoords != null
                && input.aiCoords.length == seekers.size()
            ) {
                for(int i = 0; i < seekers.size(); i++) {
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
        Hider messageFrom = loadedPlayers.get(message.uuid);
        ClientInput input = message.input;

        if (messageFrom == me) {
            SceneManager.getMainGame().appendStringToChat(
                "Me: " + input.message
            );
            return;
        }

        SceneManager.getMainGame().appendStringToChat(
            message.name + ": " + input.message
        );
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
}
