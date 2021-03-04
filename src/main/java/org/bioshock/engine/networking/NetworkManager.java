package org.bioshock.engine.networking;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bioshock.engine.ai.Seeker;
import org.bioshock.engine.entity.Entity;
import org.bioshock.engine.entity.EntityManager;
import org.bioshock.engine.entity.Hider;
import org.bioshock.engine.scene.SceneManager;
import org.bioshock.main.App;
import org.bioshock.networking.Messages;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

public class NetworkManager {
    private static Map<KeyCode, Boolean> keyPressed =
        new EnumMap<>(KeyCode.class);

    private static String me = UUID.randomUUID().toString();
    private static int noOfPlayers;
    private static Map<String, Hider> players = new HashMap<>();

    private static boolean inGame = false;

    private static GameState gameState = null;
    private static EmptyClient client;

    private NetworkManager() {}

    public static void initialise() {
        keyPressed.put(KeyCode.W, false);
        keyPressed.put(KeyCode.A, false);
        keyPressed.put(KeyCode.S, false);
        keyPressed.put(KeyCode.D, false);

        client = new EmptyClient();
        client.connect();
    }

    // TDDO
    private static Messages.ClientInput pollInputs() {
        int x = (int) players.get(me).getX() + Boolean.compare(keyPressed.get(KeyCode.D), keyPressed.get(KeyCode.A));
        int y = (int) players.get(me).getY() + Boolean.compare(keyPressed.get(KeyCode.S), keyPressed.get(KeyCode.W));

        Seeker ai = EntityManager.getSeeker();
        Point2D pos = ai.getPosition();
        Point2D direc = ai.getMovement().getDirection();

        Point2D newPos = pos.add(direc);

        // App.logger.debug("x {} y {}", x, y);
        App.logger.debug("ai local - x {} y {}", newPos.getX(), newPos.getY());

        return new Messages.ClientInput(x, y, newPos.getX(), newPos.getY());
    }

	public static void tick() {
        //TODO: lockstep(...)
        if(client.isConnected() && SceneManager.isGameStarted()) {
            try {
                client.getMutex().acquire();
                try {
                    for (var ms : client.getMsgQ()) {
                        if (ms instanceof Messages.InQueue) {
                            var d = (Messages.InQueue) ms;
                            if(d.n == d.numberOfPlayers) {
                                // pass data from d into constructor to initialize game state
                                noOfPlayers = d.numberOfPlayers;
                                List<Hider> localPlayers = EntityManager.getPlayers();
                                if (localPlayers.size() != noOfPlayers) {
                                    App.logger.error(
                                        "Wrong number of players expected {}, actual {}",
                                        noOfPlayers,
                                        localPlayers.size()
                                    );
                                    App.exit();
                                }

                                for (int i = 0; i < noOfPlayers; i++) {
                                    localPlayers.get(i).setID(d.uuids[i]);
                                    players.put(d.uuids[i], localPlayers.get(i));
                                }

                                inGame = true;
                            }
                        } else if (ms instanceof Messages.ServerInputState && inGame) {
                            var d = (Messages.ServerInputState) ms;
                            for (int i = 0; i < players.size(); i++) {
                                // App.logger.debug("x {} y {} delta {}", d.inputs[i].x, d.inputs[i].y, d.delta);

                                if (i == 0) {
                                    App.logger.debug("x {} y {} delta {}", d.inputs[i].aiX, d.inputs[i].aiY, d.delta);
                                    EntityManager.getSeeker().getMovement().move(new Point2D(
                                        (d.inputs[i].aiX * d.delta),
                                        (d.inputs[i].aiY * d.delta)
                                    ));
                                }

                                players.get(d.names[i]).getMovement().move(new Point2D(
                                    (d.inputs[i].x * d.delta),
                                    (d.inputs[i].y * d.delta)
                                ));
                            }
                        }
                    }
                    client.getMsgQ().clear();
                } finally {
                    client.getMutex().release();
                }
            } catch(InterruptedException ie) {
                App.logger.error(ie);
                Thread.currentThread().interrupt(); /* Shoud not ignore interuptions */
            }
            if (inGame) {
                client.send(Messages.Serializer.serialize(pollInputs()));
            }
        }
	}

	public static void register(Entity entity) {
		// TODO Auto-generated method stub

	}

    public static void registerAll(List<Entity> entities) {
        entities.forEach(NetworkManager::register);
	}

	public static void unregister(Entity entity) {
		// TODO Auto-generated method stub

	}

	public static void unregisterAll(List<Entity> entities) {
       entities.forEach(NetworkManager::unregister);
	}

    public static void setKeysPressed(KeyCode key, boolean pressed) {
        keyPressed.put(key, pressed);
    }

	public static String getMe() {
		return me;
	}
}
