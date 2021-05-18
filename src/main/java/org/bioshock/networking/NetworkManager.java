package org.bioshock.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.prefs.Preferences;

import org.bioshock.engine.core.ChatManager;
import org.bioshock.entities.Entity;
import org.bioshock.entities.EntityManager;
import org.bioshock.entities.SquareEntity;
import org.bioshock.entities.players.Hider;
import org.bioshock.entities.players.SeekerAI;
import org.bioshock.gui.LobbyController;
import org.bioshock.gui.SettingsController;
import org.bioshock.main.App;
import org.bioshock.networking.Message.ClientInput;
import org.bioshock.scenes.MainGame;
import org.bioshock.scenes.SceneManager;
import org.bioshock.utils.JSON;
import org.json.JSONObject;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;
import javafx.util.Duration;


public class NetworkManager {
    /**
     * The address of the scoring server
     */
    private static final String SCORE_SERVER =
        "http://51.15.109.210:8034";

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

    /**
     * Timeline responsible for periodically updating ping
     */
    private static Timeline pingTimeline;

    /**
     * A JSON of the local players account information
     */
    private static JSONObject account = new JSONObject();

    /**
     * A JSON of the high scores scoreboard
     */
    private static JSONObject highScores = new JSONObject();


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

            pingTimeline = new Timeline(new KeyFrame(
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

        if(me.isTeleported()){
            me.setTeleported(false);
            SceneManager.getMainGame().destroyTeleporter();
            return new Message(-2, myID, myName, input, me.isDead());
        }
        else
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
            if (messageFrom == null) {
                App.logger.error("messageFrom null");
                return;
            }

            ClientInput input = message.input;

            if (message.playerNumber == -2) {
                if (message.dead) {
                    SceneManager.getMainGame().destroyBomb();
                }
                else {
                    SceneManager.getMainGame().destroyTeleporter();
                }
            }

            if (input == null && message.dead) messageFrom.setDead(true);

            if (input == null) continue;

            if (
                input.message != null
                && !input.message.isEmpty()
            ) {
                sendChat(message);
            }

            if (messageFrom == me) continue;

            NetworkManager.setPing(messageFrom, input.ping);

            if (
                messageFrom == masterHider
                && input.aiCoords != null
                && input.aiCoords.length == seekers.size()
            ) {
                for (int i = 0; i < seekers.size(); i++) {
                    int x = input.aiCoords[i][0];
                    int y = input.aiCoords[i][1];

                    setDisplacement(new Point2D(x, y), seekers.get(i));
                    seekers.get(i).getMovement().moveTo(x, y);
                }
            }

            setDisplacement(new Point2D(input.x, input.y), messageFrom);

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
     * {@link org.bioshock.physics.Movement#getDisplacement()
     * Movement.displacement}
     * of each {@link Entity}
     * @param newPosition New position
     * @param entity The {@link Entity} to update
     * @see Message.ClientInput
     */
    private static void setDisplacement(
        Point2D newPosition,
        SquareEntity entity
    ) {
        double dispX = newPosition.getX() - entity.getX();

        double dispY = newPosition.getY() - entity.getY();

        entity.getMovement().setDisplacement(dispX, dispY);
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
        if (hider.isBombed()) {
            hider.setBombed(false);
            SceneManager.getMainGame().destroyBomb();
            client.send(Message.serialise(
                new Message(
                    -2,
                    hider.getID(),
                    playerNames.get(hider),
                    null,
                    true
                )
            ));
        } else {
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

        if (hider == me) {
            sendScores();
        }
    }


    /**
     * Attempts to create a new account for the scoring server
     * @param username Of the account to create
     * @param password Of the account to create
     * @param passwordConfirmation Should match #password
     * @return
     */
    public static String registerAccount(
        String username,
        String password,
        String passwordConfirmation
    ) {
        try {
            URL url = new URL(SCORE_SERVER + "/register");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(JSON.POST);
            con.setRequestProperty(
                JSON.CONTENT_TYPE,
                JSON.JSON_HEADER
            );
            con.setDoOutput(true);

            JSONObject json = new JSONObject();
            json.put(JSON.NAME, username);
            json.put(JSON.PASSWORD, password);
            json.put("PasswordConfirmation", passwordConfirmation);

            byte[] data = json.toString().getBytes(StandardCharsets.UTF_8);

            con.getOutputStream().write(data);

            try (
                BufferedReader input = new BufferedReader(
                    new InputStreamReader(
                        con.getInputStream(),
                        StandardCharsets.UTF_8
                    )
                )
            ) {
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = input.readLine()) != null) {
                    stringBuilder.append(line);
                }

                JSONObject response = new JSONObject(stringBuilder.toString());

                if (response.optInt(JSON.STATUS) == 200) {
                    Preferences prefs = Preferences.userNodeForPackage(
                        SettingsController.class
                    );
                    prefs.put("playerName", username);

                    account.put(JSON.NAME,  username);
                    account.put(JSON.SCORE, response.optInt(   JSON.SCORE));
                    account.put(JSON.TOKEN, response.optString(JSON.TOKEN));
                }

                return response.optString(
                    JSON.MESSAGE,
                    JSON.AN_ERROR_OCCURRED
                );
            }
        }
        catch (MalformedURLException e) {
            App.logger.error(JSON.ADDRESS_INCORRECT, e);
            return JSON.AN_ERROR_OCCURRED;
        }
        catch (IOException e) {
            App.logger.error(e);
            return JSON.AN_ERROR_OCCURRED;
        }
    }


    /**
     * Attempts to login to scoring server then stores account information if
     * successful
     * @param username The username of the account to attempt log in to
     * @param password The password of the account to attempt log in to
     * @return The response from the server
     */
    public static String login(String username, String password) {
        try {
            URL url = new URL(SCORE_SERVER + "/login");

            JSONObject json = new JSONObject();
            json.put(JSON.NAME, username);
            json.put(JSON.PASSWORD, password);

            byte[] data = json.toString().getBytes(StandardCharsets.UTF_8);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(JSON.POST);
            con.setRequestProperty(
                JSON.CONTENT_TYPE,
                JSON.JSON_HEADER
            );
            con.setDoOutput(true);
            con.getOutputStream().write(data);

            try (
                BufferedReader input = new BufferedReader(
                    new InputStreamReader(
                        con.getInputStream(),
                        StandardCharsets.UTF_8
                    )
                )
            ) {
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = input.readLine()) != null) {
                    stringBuilder.append(line);
                }
                JSONObject response = new JSONObject(stringBuilder.toString());

                if (response.optInt(JSON.STATUS) == 200) {
                    Preferences prefs = Preferences.userNodeForPackage(
                        SettingsController.class
                    );
                    prefs.put("playerName", username);

                    account.put(JSON.NAME,  username);
                    account.put(JSON.SCORE, response.optInt(   JSON.SCORE));
                    account.put(JSON.TOKEN, response.optString(JSON.TOKEN));
                }
                App.logger.debug("Username: " + username);
                App.logger.debug("Json Account: " + account.getString(JSON.NAME));

                return response.optString(
                    JSON.MESSAGE,
                    JSON.AN_ERROR_OCCURRED
                );
            }
        }
        catch (MalformedURLException e) {
            App.logger.error(JSON.ADDRESS_INCORRECT, e);
            return JSON.AN_ERROR_OCCURRED;
        }
        catch (IOException e) {
            App.logger.error(e);
            return JSON.AN_ERROR_OCCURRED;
        }
    }


    /**
     * Clears stored account data
     */
    public static void logout() {
        account = new JSONObject();
    }


    /**
     * A successful {@link #login(String, String) login()} must have been made
     * before this function returns a on empty {@link JSONObject}
     *
     * @return A JSON of the local players account information, empty if not
     * logged in
     *
     * @see #login(String, String)
     */
    public static JSONObject getMyAccount() {
        return account;
    }


    /**
     * Sends local players scores to scoring server
     * @param victory True if game was won
     */
    public static void sendScores() {
        try {
            int oldScore = account.optInt(JSON.SCORE);
            MainGame mainGame = SceneManager.getMainGame();
            Map<Hider, Integer> playerScores = mainGame.getPlayerScores();
            Hider player = EntityManager.getCurrentPlayer();
            account.put(JSON.SCORE, oldScore + playerScores.get(player));

            URL url = new URL("http://recklessgame.net:8034/increaseScore");
            String jsonInputString = "{\"Token\":\"" + account.optString(JSON.TOKEN) + "\",\"Score\":\"" + playerScores.get(player) + "\"}";
            byte[] postDataBytes = jsonInputString.getBytes(StandardCharsets.UTF_8);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setDoOutput(true);
            con.getOutputStream().write(postDataBytes);
            Reader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            App.logger.error(e);
        }
    }


    /**
     * Makes a request to scoring server to receive the high scores scoreboard
     * @return True if valid response is given
     * @see #getHighScores()
     */
    public static boolean requestHighScores() {
        try {
            URL url = new URL(SCORE_SERVER + "/getTop5Scores");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod(JSON.GET);
            con.setRequestProperty(
                JSON.CONTENT_TYPE,
                JSON.JSON_HEADER
            );
            con.setDoInput(true);

            try (
                BufferedReader input = new BufferedReader(
                    new InputStreamReader(
                        con.getInputStream(),
                        StandardCharsets.UTF_8
                    )
                )
            ) {
                StringBuilder stringBuilder = new StringBuilder();

                String line;
                while ((line = input.readLine()) != null) {
                    stringBuilder.append(line);
                }

                JSONObject response = new JSONObject(stringBuilder.toString());

                List<String> requiredKeys = List.of(
                    JSON.STATUS, "Score1", "Score2", "Score3", "Score4",
                    "Score5", "Name1", "Name2", "Name3", "Name4", "Name5"
                );

                if (
                    response.optInt(JSON.STATUS) == 200
                    && response.keySet().containsAll(requiredKeys)
                ) {
                    highScores = response;
                }

                return true;
            }
        }
        catch (MalformedURLException e) {
            App.logger.error(JSON.ADDRESS_INCORRECT, e);
            return false;
        }
        catch (IOException e) {
            App.logger.error(e);
            return false;
        }
    }


    /**
     * A call to {@link #requestHighScores()} must evaluate to {@code true}
     * before this function returns a non empty {@link JSONObject}
     *
     * @return A JSON of the local players in-game statistics, empty if
     * successful request has not been made
     *
     * @see #requestHighScores()
     */
    public static JSONObject getHighScores() {
        return highScores;
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
        if (client.isOpen()) {
            client.sendPing();
            previousPing = System.nanoTime();
        }
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
                "Tried to kill networking thread before initialisation"
            );
        }

        pingTimeline.stop();
        client.getMessageQ().clear();
        client.close();
        client = new Client();
        loadedPlayers.clear();
    }
}
