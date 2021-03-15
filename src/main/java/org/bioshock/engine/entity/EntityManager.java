package org.bioshock.engine.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bioshock.engine.ai.SeekerAI;
import org.bioshock.engine.networking.NetworkManager;
import org.bioshock.main.App;

public final class EntityManager {
    private static List<Entity> entities = new ArrayList<>();
    private static List<Hider> players = new ArrayList<>();
    private static SeekerAI seeker;

    private EntityManager() {}

    public static void tick(double timeDelta) {
        entities.forEach(entitiy -> entitiy.safeTick(timeDelta));
    }

    public static void multiTick(double timeDelta) {
        Thread[] threads = new Thread[players.size()];

        Iterator<Hider> pIter = players.listIterator();

        for (int i = 0; i < players.size(); i++) {
            threads[i] = new Thread(() -> pIter.next().safeTick(timeDelta));
            threads[i].start();
        }

        joinAll(threads);

        if (seeker != null) seeker.safeTick(timeDelta);
    }

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

    public static void register(Entity entity) {

        if (
            entity.getNetworkC().isNetworked()
            && entity instanceof SquareEntity
        ) {
            NetworkManager.register((SquareEntity) entity);
        }

        entities.add(entity);
        if (entity instanceof Hider) players.add((Hider) entity);
        if (entity instanceof SeekerAI) seeker = (SeekerAI) entity;
    }

    public static void registerAll(Entity... toAdd) {
        Arrays.asList(toAdd).forEach(EntityManager::register);
    }

    public static void unregister(Entity entity) {
        if (
            entity.getNetworkC().isNetworked()
            && entity instanceof SquareEntity
        ) {
            NetworkManager.unregister((SquareEntity) entity);
        }

        entities.remove(entity);
        players.remove(entity);
        if (entity == seeker) seeker = null;
    }

    public static void unregisterAll() {
        entities.forEach(entity -> {
            if (
                entity.getNetworkC().isNetworked()
                && entity instanceof SquareEntity
            ) {
                NetworkManager.unregister((SquareEntity) entity);
            }
        });
        seeker = null;
        entities.clear();
        players.clear();
    }

    public static void unregisterAll(Entity... toRemove) {
        Arrays.asList(toRemove).forEach(EntityManager::unregister);
    }

    public static boolean isManaged(Entity entity, Entity... toCheck) {
        return entities.contains(entity)
            && entities.containsAll(Arrays.asList(toCheck));
    }

    public static Entity[] getEntities() {
        return entities.toArray(new Entity[0]);
    }

    public static List<Entity> getEntityList() {
        return entities;
    }

    public static SeekerAI getSeeker() {
        return seeker;
    }

    public static List<Hider> getPlayers() {
        return players;
    }
}
