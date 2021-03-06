package org.bioshock.engine.networking;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bioshock.engine.ai.Seeker;
import org.bioshock.engine.entity.Hider;
import org.bioshock.engine.entity.SquareEntity;
import org.bioshock.engine.input.InputManager;
import org.bioshock.engine.scene.SceneManager;
import org.bioshock.main.App;

import javafx.concurrent.Task;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

public class NetworkManager {
    private static Map<KeyCode, Boolean> keyPressed = new EnumMap<>(KeyCode.class);

    private static String me = UUID.randomUUID().toString();
    private static Map<String, Hider> players = new HashMap<>();
    private static Seeker seeker;

    private static boolean inGame = false;

    private static Client client;
    private static Hider firstPlayer;

    private static Object gameStartedMutex = new Object();
    private static Object awaitingMessage = new Object();

    private NetworkManager() {}

    public static void initialise() {
        Client client2 = new Client();
        try {
            client2.connectBlocking();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        App.logger.debug("connected");

        client2.send("yo");
        App.logger.debug("sent");

        keyPressed.put(KeyCode.W, false);
        keyPressed.put(KeyCode.A, false);
        keyPressed.put(KeyCode.S, false);
        keyPressed.put(KeyCode.D, false);

        Thread initThread = new Thread(new Task<>() {
            @Override
            protected Object call() {
                synchronized (gameStartedMutex) {
                    while (!SceneManager.isGameStarted()) {
                        try {
                            gameStartedMutex.wait();
                            client.connectBlocking();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }

                for (int psConn = 0; psConn < App.PLAYERCOUNT; psConn++) {
                    synchronized(awaitingMessage) {
                        while (client.getInitialMessages().isEmpty()) {
                            try {
                                App.logger.info("Awaiting Players");
                                App.logger.debug("connected {}", client.getReadyState());
                                if (psConn != 0) {
                                    awaitingMessage.wait();
                                }
                                client.send("I'm waiting");
                                App.logger.debug("Lobby message");
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                        App.logger.debug("Player joined");
                    }

                    Message message = client.getInitialMessages().poll();

                    Hider[] localPlayers = players.values().toArray(new Hider[1]);
                    Hider player = localPlayers[psConn];
                    players.remove(player.getID());
                    player.setID(message.uuid);
                    players.put(player.getID(), player);
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

        Point2D ai = seeker.getMovement().getDirection();

        Message.ClientInput input = new Message.ClientInput(
            x, y, ai.getX(), ai.getY()
        );

        return new Message(me, input);
    }

    public static void tick() {
        if (!inGame) {
            return;
        }

        players.get(me).getMovement().direction(0, 0);

        try {
            App.logger.debug("deadlock?");
            client.getMutex().acquire();
            App.logger.debug("mutex acquired");
            for (Message message : client.getMsgQ()) {
                App.logger.debug("Received Message");

                if (message.uuid.equals(firstPlayer.getID())) {
                    seeker.getMovement().direction(
                        message.input.aiX,
                        message.input.aiY
                    );
                }

                players.get(message.uuid).getMovement().direction(
                    message.input.x,
                    message.input.y
                );

                client.getMsgQ().remove(message);
            }
        } catch(InterruptedException ie) {
            App.logger.error(ie);
            Thread.currentThread().interrupt(); /* Should not ignore interruptions */
        } finally {
            client.getMutex().release();
            App.logger.debug("mutex released");
        }

        client.send(Message.serialize(pollInputs()));
	}

	public static void register(SquareEntity entity) {
        if (entity instanceof Hider) {
            if (players.isEmpty()) {
                firstPlayer = (Hider) entity;
            }
            players.put(entity.getID(), (Hider) entity);
        } else {
            seeker = (Seeker) entity;
        }
	}

    public static void registerAll(List<SquareEntity> entities) {
        entities.forEach(NetworkManager::register);
	}

	public static void unregister(SquareEntity entity) {
		if (entity instanceof Seeker) {
            seeker = null;
        } else {
            players.remove(entity.getID());
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
