package org.bioshock.engine.networking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bioshock.engine.ai.Seeker;
import org.bioshock.engine.entity.Hider;
import org.bioshock.engine.entity.SquareEntity;
import org.bioshock.engine.input.InputManager;
import org.bioshock.engine.networking.Message.ClientInput;
import org.bioshock.engine.scene.SceneManager;
import org.bioshock.main.App;

import javafx.concurrent.Task;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

public class NetworkManager {
    private static Map<KeyCode, Boolean> keyPressed = new EnumMap<>(KeyCode.class);

    private static String me = UUID.randomUUID().toString();
    private static List<Hider> playerList = new ArrayList<>(App.PLAYERCOUNT);
    private static Map<String, Hider> loadedPlayers = new HashMap<>(App.PLAYERCOUNT);
    private static Seeker seeker;

    private static boolean inGame = false;

    private static Client client = new Client("ws://localhost:8010");
    private static Object gameStartedMutex = new Object();
    private static Object awaitingMessage = new Object();

    private NetworkManager() {}

    public static void initialise() {
        keyPressed.put(KeyCode.W, false);
        keyPressed.put(KeyCode.A, false);
        keyPressed.put(KeyCode.S, false);
        keyPressed.put(KeyCode.D, false);

        Thread initThread = new Thread(new Task<>() {
            @Override
            protected Object call() {
                // Thread.setDefaultUncaughtExceptionHandler((Thread t, Throwable e) ->
                //     App.logger.error(
                //         "{}\n{}",
                //         e,
                //         Arrays.toString(e.getStackTrace()).replace(',', '\n')
                //     )
                // );

                /* Wait until game loads, then connect to server */
                synchronized (gameStartedMutex) {
                    while (!SceneManager.isGameStarted()) {
                        try {
                            gameStartedMutex.wait();

                            App.logger.info("Connecting to web socket...");
                            client.connectBlocking();
                            App.logger.info("Connected to web socket");

                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }

                /* Wait until players join then add them to loadedPlayers */
                while (loadedPlayers.size() < App.PLAYERCOUNT) {
                    synchronized(awaitingMessage) {
                        while (client.getInitialMessages().isEmpty()) {
                            try {
                                awaitingMessage.wait();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    }

                    Message m = client.getInitialMessages().poll();

                    Hider hider = playerList.get(m.playerNumber - 1);
                    hider.setID(m.UUID);
                    loadedPlayers.putIfAbsent(m.UUID, hider);
                }

                InputManager.onPressListener(
                    KeyCode.W, () -> setKeysPressed(KeyCode.W, true)
                );
                InputManager.onPressListener(
                    KeyCode.A, () -> setKeysPressed(KeyCode.A, true)
                );
                InputManager.onPressListener(
                    KeyCode.S, () -> setKeysPressed(KeyCode.S, true)
                );
                InputManager.onPressListener(
                    KeyCode.D, () -> setKeysPressed(KeyCode.D, true)
                );

                InputManager.onReleaseListener(
                    KeyCode.W, () -> setKeysPressed(KeyCode.W, false)
                );
                InputManager.onReleaseListener(
                    KeyCode.A, () -> setKeysPressed(KeyCode.A, false)
                );
                InputManager.onReleaseListener(
                    KeyCode.S, () -> setKeysPressed(KeyCode.S, false)
                );
                InputManager.onReleaseListener(
                    KeyCode.D, () -> setKeysPressed(KeyCode.D, false)
                );

                inGame = true;

                App.logger.info("Networking initialised");

                return null;
            }
        });

        initThread.start();
    }

    // TDDO
    private static Message pollInputs() {
        int x = Boolean.compare(
            keyPressed.get(KeyCode.D),
            keyPressed.get(KeyCode.A)
        );
        int y = Boolean.compare(
            keyPressed.get(KeyCode.S),
            keyPressed.get(KeyCode.W)
        );

        Point2D aiDir = seeker.getMovement().getDirection();

        Message.ClientInput input = new Message.ClientInput(
            x, y, aiDir.getX(), aiDir.getY()
        );

        return new Message(-1, me, input);
    }

    public static void tick() {
        if (!inGame || playerList.isEmpty()) return;

        if (loadedPlayers.get(me) != null) {
            loadedPlayers.get(me).getMovement().direction(0, 0);

            client.send(Message.serialise(pollInputs()));
        }

        try {
            client.getMutex().acquire();

            for (Hider hider : playerList) {
                ClientInput input = client.getInputQ().get(hider.getID());
                if (hider == playerList.get(0)) {
                    seeker.getMovement().direction(
                        input.aiX,
                        input.aiY
                    );
                }

                loadedPlayers.get(hider.getID()).getMovement().direction(
                    input.x,
                    input.y
                );
            }
        } catch(InterruptedException ie) {
            Thread.currentThread().interrupt();
        } finally {
            client.getMutex().release();
        }
	}

	public static void register(SquareEntity entity) {
        if (entity instanceof Hider) {
            if (playerList.isEmpty()) {
            }
            playerList.add((Hider) entity);
        }
        else if (entity instanceof Seeker) {
            seeker = (Seeker) entity;
        }
        else {
            App.logger.error("Tried to register non player entity {}", entity);
        }
	}

    public static void registerAll(List<SquareEntity> entities) {
        entities.forEach(NetworkManager::register);
	}

	public static void unregister(SquareEntity entity) {
        if (entity instanceof Hider) {
            playerList.remove(entity);
            loadedPlayers.remove(entity.getID());
        }
		else if (entity instanceof Seeker) {
            seeker = null;
        }
        else {
            App.logger.error("Tried to register non player entity {}", entity);
        }
	}

	public static void unregisterAll(List<SquareEntity> entities) {
       entities.forEach(NetworkManager::unregister);
	}

    public static void setKeysPressed(KeyCode key, boolean pressed) {
        keyPressed.replace(key, pressed);
    }

	public static String getMe() {
		return me;
	}

    public static Object getMutex() {
        return gameStartedMutex;
    }

    public static Object getMessageMutex() {
        return awaitingMessage;
    }
}
