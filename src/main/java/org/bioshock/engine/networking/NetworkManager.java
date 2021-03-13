package org.bioshock.engine.networking;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bioshock.engine.ai.SeekerAI;
import org.bioshock.engine.entity.Hider;
import org.bioshock.engine.entity.SquareEntity;
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
	private static SeekerAI seeker;

    private static boolean inGame = false;

    public static boolean isInGame() {
		return inGame;
	}

	private static Client client = new Client();
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
                    App.logger.debug("Register In player");

                    Message m = client.getInitialMessages().poll();

                    Hider hider = playerList.get(m.playerNumber - 1);
                    hider.setID(m.UUID);
                    loadedPlayers.putIfAbsent(m.UUID, hider);
                }

                loadedPlayers.get(me).getMovement().initMovement();

                inGame = true;

                App.logger.info("Networking initialised");

                return null;
            }
        });

        initThread.start();
    }

    // TDDO
    private static Message pollInputs() {
        int x = (int) loadedPlayers.get(me).getX();
        int y = (int) loadedPlayers.get(me).getY();

        Point2D aiPos = seeker.getPosition();
        double aiX = aiPos.getX();
        double aiY = aiPos.getY();

        Message.ClientInput input = new Message.ClientInput(x, y, aiX, aiY);

        /* getMe only null when NetworkManager uninitialised */
        return new Message(-1, me, input, getMe().isDead());
    }

    public static void tick() {
        if (!inGame || playerList.isEmpty()) return;

        if (loadedPlayers.get(me) != null) {
            client.send(Message.serialise(pollInputs()));
        }

        try {
            client.getMutex().acquire();

            for (Hider hider : playerList) {
                Message message = client.getMessageQ().get(hider.getID());
                ClientInput input = message.input;

                if (input == null || hider == loadedPlayers.get(me)) continue;

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

                loadedPlayers.get(hider.getID()).setDead(message.dead);
            }
        } catch(InterruptedException ie) {
            Thread.currentThread().interrupt();
        } finally {
            client.getMutex().release();
        }
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

    public static void registerAll(List<SquareEntity> entities) {
        entities.forEach(NetworkManager::register);
	}

	public static void unregister(SquareEntity entity) {
        if (entity instanceof Hider) {
            playerList.remove(entity);
            loadedPlayers.remove(entity.getID());
        }
		else if (entity instanceof SeekerAI) {
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

    public static Hider getMe() {
        Hider me;
        if ((me = loadedPlayers.get(getMyID())) == null) {
            App.logger.error(
                "Tried to get local player before Network initialised"
            );
        }

        return me;
    }

	public static String getMyID() {
		return me;
	}

    public static Object getMutex() {
        return gameStartedMutex;
    }

    public static Object getMessageMutex() {
        return awaitingMessage;
    }

    public static Map<String, Hider> getLoadedPlayers() {
		return loadedPlayers;
	}

}
