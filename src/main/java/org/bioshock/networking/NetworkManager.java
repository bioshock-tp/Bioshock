package org.bioshock.networking;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import org.bioshock.entities.Entity;
import org.bioshock.entities.SquareEntity;
import org.bioshock.entities.players.Hider;
import org.bioshock.entities.players.SeekerAI;
import org.bioshock.main.App;
import org.bioshock.networking.Message.ClientInput;
import org.bioshock.scenes.SceneManager;

import java.util.*;

public class NetworkManager {
    private static Map<KeyCode, Boolean> keyPressed = new EnumMap<>(
        KeyCode.class
    );

    private static String myID = UUID.randomUUID().toString();
    private static Hider me;
    private static Hider masterHider;
    private static SeekerAI seeker;
    private static long seed = 0;

    public static void setSeed(long currSeed) {
        seed = currSeed;
    }

    public static long getSeed() {
        return seed;
    }

    private static List<Hider> playerList = new ArrayList<>(App.playerCount());
    private static Map<String, Hider> loadedPlayers = new HashMap<>(
        App.playerCount()
    );

    private static Client client = new Client();
    private static Object awaitingPlayerLock = new Object();

    private NetworkManager() {}

    public static void initialise() {
        keyPressed.put(KeyCode.W, false);
        keyPressed.put(KeyCode.A, false);
        keyPressed.put(KeyCode.S, false);
        keyPressed.put(KeyCode.D, false);

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
                    loadedPlayers.putIfAbsent(message.uuid, hider);
                    Platform.runLater(() ->
                        SceneManager.getLobby().updatePlayerCount()
                    );
                }


                masterHider = playerList.get(0);

                me = loadedPlayers.get(myID);

                me.getMovement().initMovement();
                playerList.forEach(Hider::initAnimations);
                seeker.initAnimations();

                App.logger.info("Networking initialised");

                return null;
            }
        });

        initThread.start();
    }

    private static Message pollInputs() {
        int x = (int) me.getX();
        int y = (int) me.getY();

        Point2D aiPos = seeker.getPosition();
        int aiX = (int) aiPos.getX();
        int aiY = (int) aiPos.getY();

        Message.ClientInput input = new Message.ClientInput(x, y, aiX, aiY);

        return new Message(-1, myID, input, me.isDead());
    }

    public static void tick() {
        if (me != null) {
            client.send(Message.serialise(pollInputs()));
        }

        try {
            client.getMutex().acquire();
        } catch(InterruptedException e) {
            App.logger.error(e);
            Thread.currentThread().interrupt();
        }

        Message message;
        while ((message = client.getMessageQ().poll()) != null) {
            /* The hider the message came from */
            Hider messageFrom = loadedPlayers.get(message.uuid);
            if (messageFrom == me) continue;

            ClientInput input = message.input;

            if (input == null) {
                if (message.dead) messageFrom.setDead(true);
            } else {
                updateDirection(input, messageFrom);
                
                if (messageFrom == masterHider) {
                    seeker.getMovement().moveTo(
                        input.aiX,
                        input.aiY
                    );
                }

                messageFrom.getMovement().moveTo(
                    input.x,
                    input.y
                );
            }
        }

        client.getMutex().release();
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
            seeker = (SeekerAI) entity;
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

        if (entity == seeker) seeker = null;
    }

    public static void unregisterAll(Collection<Entity> entities) {
       entities.forEach(NetworkManager::unregister);
    }

    public static void kill(Hider hider) {
        App.logger.debug("killing");
        client.send(Message.serialise(
            new Message(-1, hider.getID(), null, true)
        ));
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
}
