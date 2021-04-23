package org.bioshock.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bioshock.entities.players.Hider;
import org.bioshock.entities.players.SeekerAI;
import org.bioshock.main.App;
import org.bioshock.networking.NetworkManager;
import org.bioshock.scenes.SceneManager;

public final class EntityManager {
    private static List<Entity> entities = new ArrayList<>();
    private static List<Hider> players = new ArrayList<>(App.playerCount());
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

    public static void register(Entity ent) {
        if (ent.getNetworkC().isNetworked() && ent instanceof SquareEntity) {
            NetworkManager.register((SquareEntity) ent);
        }

        entities.add(ent);
        if (ent instanceof Hider) players.add((Hider) ent);
        if (ent instanceof SeekerAI) seeker = (SeekerAI) ent;
    }

    public static void registerAll(Entity... toAdd) {
        Arrays.asList(toAdd).forEach(EntityManager::register);
    }

    public static void unregister(Entity ent) {
        NetworkManager.unregister(ent);

        entities.remove(ent);
        players.remove(ent);
        if (ent == seeker) seeker = null;
    }

    public static void unregisterAll() {
        entities.forEach(NetworkManager::unregister);

        entities.clear();
        players.clear();
        seeker = null;
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

    public static Hider getCurrentPlayer() {
        Hider me = null;
        if (SceneManager.inGame() && !players.isEmpty()) {
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
