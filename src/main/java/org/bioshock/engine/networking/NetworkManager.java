package org.bioshock.engine.networking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bioshock.engine.ai.SeekerAI;
import org.bioshock.engine.entity.Entity;
import org.bioshock.engine.entity.Hider;
import org.bioshock.engine.entity.SquareEntity;
import org.bioshock.engine.networking.Message.ClientInput;
import org.bioshock.engine.scene.SceneManager;
import org.bioshock.main.App;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

public class NetworkManager {
    private static Map<KeyCode, Boolean> keyPressed = new EnumMap<>(
        KeyCode.class
    );

    private static String myID = UUID.randomUUID().toString();
    private static Hider me;
    private static SeekerAI seeker;

    private static List<Hider> playerList = new ArrayList<>(App.playerCount());
    private static Map<String, Hider> loadedPlayers = new HashMap<>(
        App.playerCount()
    );

    private static boolean inGame = false;

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

                me = loadedPlayers.get(myID);

                me.initMovement();

                inGame = true;

                App.logger.info("Networking initialised");

                return null;
            }
        });

        initThread.start();
    }

    // TDDO
    private static Message pollInputs() {
        int x = (int) me().getX();
        int y = (int) me().getY();

        Point2D aiPos = seeker.getPosition();

        Message.ClientInput input = new Message.ClientInput(
            x, y, aiPos.getX(), aiPos.getY()
        );

        return new Message(-1, myID, input);
    }

    public static void tick() {
        if (!inGame || playerList.isEmpty()) return;

        if (me() != null) {
            client.send(Message.serialise(pollInputs()));
        }

        try {
            client.getMutex().acquire();
        } catch(InterruptedException e) {
            App.logger.error(e);
            Thread.currentThread().interrupt();
        }

        App.logger.debug("acquired");
        for (Hider hider : playerList) {
            App.logger.debug("message");
            ClientInput input = client.getInputQ().get(hider.getID());

            if (input == null || hider == me()) continue;

            if (hider == playerList.get(0)) {
                seeker.getMovement().direction(
                    input.aiX,
                    input.aiY
                );
            }

            loadedPlayers.get(hider.getID()).getMovement().moveTo(
                input.x,
                input.y
            );
        }

        client.getMutex().release();
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
}
