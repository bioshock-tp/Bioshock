package org.bioshock.networking;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import org.bioshock.entities.Entity;
import org.bioshock.entities.SquareEntity;
import org.bioshock.entities.players.Hider;
import org.bioshock.entities.players.SeekerAI;
import org.bioshock.gui.LobbyController;
import org.bioshock.main.App;
import org.bioshock.networking.Message.ClientInput;
import org.bioshock.scenes.MainGame;
import org.bioshock.scenes.SceneManager;

import java.util.*;

public class NetworkManager {
    private static Map<KeyCode, Boolean> keyPressed = new EnumMap<>(
        KeyCode.class
    );

    private static String myID = UUID.randomUUID().toString();
    private static String myName;
    private static Hider me;
    private static Hider masterHider;
    
    private static List<SeekerAI> seekers = new ArrayList<>(App.playerCount());
    private static List<Hider> playerList = new ArrayList<>(App.playerCount());
    private static Map<String, Hider> loadedPlayers = new HashMap<>(
        App.playerCount()
    );

    private static Map<Hider, String> playerNames = new HashMap<>();

    private static LinkedList<String> messageList = new LinkedList<>();

    private static Client client = new Client();
    private static Object awaitingPlayerLock = new Object();
    private static Thread initThread;

    private NetworkManager() {}

    public static void initialise(LobbyController lobbyController) {
        keyPressed.put(KeyCode.W, false);
        keyPressed.put(KeyCode.A, false);
        keyPressed.put(KeyCode.S, false);
        keyPressed.put(KeyCode.D, false);

        initThread = new Thread(new Task<>() {
            @Override
            protected Object call() {
                try {
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
                        synchronized (awaitingPlayerLock) {
                            while (client.getInitialMessages().isEmpty()) {
                                try {
                                    awaitingPlayerLock.wait();
                                } catch (InterruptedException e) {
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

                        Platform.runLater(lobbyController::updatePlayerCount);
                    }


                    masterHider = playerList.get(0);

                    me = loadedPlayers.get(myID);

                    me.getMovement().initMovement();
                    playerList.forEach(Hider::initAnimations);
                    seekers.forEach(SeekerAI::initAnimations);

                    App.logger.info("Networking initialised");
                }
                catch (Exception e) {
                    App.logger.error(
                        "{}\n{}",
                        e,
                        Arrays.toString(e.getStackTrace()).replace(',', '\n')
                    );
                }
                return null;
            }
        });

        initThread.start();
    }

    private static Message pollInputs() {

        int x = (int) me.getX();
        int y = (int) me.getY();

        int[][] aiCoords = new int[seekers.size()][2];
        for(int i=0;i<seekers.size();i++) {
            Point2D seekerPos = seekers.get(i).getPosition();
            aiCoords[i][0] = (int) seekerPos.getX();
            aiCoords[i][1] = (int) seekerPos.getY();
        }
//        Point2D aiPos = seekers.getPosition();
//        int aiX = (int) aiPos.getX();
//        int aiY = (int) aiPos.getY();

        String message = "";

        if (!messageList.isEmpty() && !me.isDead()) {
            message = messageList.getFirst();
            messageList.poll();
        }

        Message.ClientInput input = new Message.ClientInput(x, y, aiCoords, message);

        return new Message(-1, myID, myName, input, me.isDead());
    }

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

            if (messageFrom == masterHider && input.aiCoords != null && input.aiCoords.length == seekers.size()) {
                for(int i=0;i<seekers.size();i++) {
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

    private static void sendChat(Message message) {
        Hider messageFrom = loadedPlayers.get(message.uuid);
        ClientInput input = message.input;

        if (messageFrom == me) {
            ((MainGame) SceneManager.getScene()).appendStringToChat(
                "Me: " + input.message
            );
            return;
        }

        ((MainGame) SceneManager.getScene()).appendStringToChat(
            message.name + ": " + input.message
        );
    }

    private static void updateDirection(ClientInput input, Hider messageFrom) {
        int dispX = (int) (input.x - messageFrom.getX());
        if (dispX != 0) dispX = dispX / Math.abs(dispX);

        int dispY = (int) (input.y - messageFrom.getY());
        if (dispY != 0) dispY = dispY / Math.abs(dispY);

        messageFrom.getMovement().direction(dispX, dispY);
    }

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

    public static void registerAll(Collection<SquareEntity> entities) {
        entities.forEach(NetworkManager::register);
    }

    public static void unregister(Entity entity) {
        playerList.remove(entity);
        loadedPlayers.remove(entity.getID());

        if (entity == seekers) seekers = null;
    }

    public static void unregisterAll(Collection<Entity> entities) {
       entities.forEach(NetworkManager::unregister);
    }

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

    public static void addMessage(String message) {
        if (!message.isEmpty()) {
            messageList.add(message);
        }
    }

    public static void setKeysPressed(KeyCode key, boolean pressed) {
        keyPressed.replace(key, pressed);
    }

    public static int playerCount() {
        return loadedPlayers.size();
    }

    public static Hider me() {
        return me;
    }

    public static String getMyID() {
        return myID;
    }

    public static Object getPlayerJoinLock() {
        return awaitingPlayerLock;
    }

    public static Map<String, Hider> getLoadedPlayers() {
        return loadedPlayers;
    }

    public static Map<Hider, String> getPlayerNames() {
        return playerNames;
    }

    public static void reset() {
        client.close();
        client = new Client();
        loadedPlayers.clear();
        if (initThread != null) {
            initThread.stop();
        }
    }
}
