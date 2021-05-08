package org.bioshock.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bioshock.entities.players.Hider;
import org.bioshock.entities.players.SeekerAI;
import org.bioshock.main.App;
import org.bioshock.networking.NetworkManager;

/**
 * A class that keeps track of all entities that need to be ticked
 */
public final class EntityManager {
	/**
	 * A list of all currently managed entities
	 */
    private static List<Entity> entities = new ArrayList<>();

    /**
     * A list of all currently managed players/hiders
     *
     * (A sublist of entities)
     */
    private static List<Hider> players = new ArrayList<>();

    /**
     * A list of all currently managed seekers
     *
     * (A sublist of entities)
     */
    private static List<SeekerAI> seekers = new ArrayList<>();


    /**
     * Private constructor as EntityManager is meant to used as a static class
     */
    private EntityManager() {}


    /**
     * Method that calls safeTick on every managed entity
     * @param timeDelta the amount of time to update the entity in seconds
     */
    public static void tick(double timeDelta) {
        entities.forEach(entitiy -> entitiy.safeTick(timeDelta));
    }


    /**
     * An experimental method that calls tick on hider on a different thread
     * and then calls tick on every seeker
     * @param timeDelta the amount of time to update the entity in seconds
     */
    public static void multiTick(double timeDelta) {
        Thread[] threads = new Thread[players.size()];

        Iterator<Hider> pIter = players.listIterator();

        // Start every thread
        for (int i = 0; i < players.size(); i++) {
            threads[i] = new Thread(() -> pIter.next().safeTick(timeDelta));
            threads[i].start();
        }

        joinAll(threads);

        for (SeekerAI seeker: seekers) {
            if (seeker != null) seeker.safeTick(timeDelta);
        }

    }


    /**
     * Join every thread given in the thread array
     * @param threads the array of threads to join
     */
    private static void joinAll(Thread[] threads) {
        for (Thread thread : threads) {
            try {
                if (thread != null) thread.join();
            } catch (InterruptedException e) {
                App.logger.error("InterruptedException");
                Thread.currentThread().interrupt();
            }
        }
    }


    /**
     * register an entity
     * and if it is networked and its a SquareEntity register it to the network manager
     * @param entity the entity to register
     */
    public static void register(Entity entity) {
        if (
            App.isNetworked()
            && entity.getNetworkC().isNetworked()
            && entity instanceof SquareEntity
        ) {
            NetworkManager.register((SquareEntity) entity);
        }

        entities.add(entity);
        if (entity instanceof Hider) players.add((Hider) entity);
        if (entity instanceof SeekerAI) seekers.add((SeekerAI) entity);
    }


    /**
     * registers every entity on the list
     * @param toAdd the array of entities to register
     */
    public static void registerAll(Entity... toAdd) {
        Arrays.asList(toAdd).forEach(EntityManager::register);
    }


    /**
     * Unregisters the entity from the EntityManager and the Network manager
     * @param entity the entity to unregister
     */
    public static void unregister(Entity entity) {
        NetworkManager.unregister(entity);

        entities.remove(entity);
        players.remove(entity);
        if (entity == seekers) seekers = null;
    }

    /**
     * Unregisters every entity currently managed by the EntityManager
     */
    public static void unregisterAll() {
        entities.forEach(NetworkManager::unregister);

        entities.clear();
        players.clear();
        seekers = null;
    }

    /**
     * Unregisters every entity in the list
     * @param toRemove the list of entities to unregister
     */
    public static void unregisterAll(Entity... toRemove) {
        Arrays.asList(toRemove).forEach(EntityManager::unregister);
    }

    /**
     * @param entities
     * @return True if the all the entities are currently managed. False if no
     * entity was provided
     */
    public static boolean isManaged(Entity... entities) {
        return entities.length > 0
            && EntityManager.entities.containsAll(Arrays.asList(entities));
    }

    /**
     * @return an array of all managed entities
     */
    public static Entity[] getEntities() {
        return entities.toArray(new Entity[0]);
    }

    /**
     * @return a list of all managed entities
     */
    public static List<Entity> getEntityList() {
        return entities;
    }

    /**
     * @return a list of all managed seekers
     */
    public static List<SeekerAI> getSeekers() {
        return seekers;
    }

    /**
     * @return a list of all managed players
     */
    public static List<Hider> getPlayers() {
        return players;
    }

    /**
     * @return the player controlled by the player on this game
     */
    public static Hider getCurrentPlayer() {
        Hider me = null;
        if (!players.isEmpty()) {
            if (!App.isNetworked()) {
                me = players.get(0);
            }
            else {
                me = NetworkManager.me();
            }
        }
        if (me == null) App.logger.error("No players registered");
        return me;
    }
}
