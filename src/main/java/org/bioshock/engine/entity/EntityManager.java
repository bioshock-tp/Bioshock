package org.bioshock.engine.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bioshock.engine.ai.SeekerAI;
import org.bioshock.engine.networking.NetworkManager;
import org.bioshock.engine.rendering.RenderManager;
import org.bioshock.engine.scene.SceneManager;

public final class EntityManager {
	private static ArrayList<Entity> entities = new ArrayList<>();
    private static ArrayList<Hider> players = new ArrayList<>();
    private static SeekerAI seeker;

    private EntityManager() {}

	public static void tick(double timeDelta) {
		for (Entity entity : entities) {
			entity.safeTick(timeDelta);
		}
	}

	public static void register(Entity entity) {
        if (
            entity.getNetworkC().isNetworked()
            && entity instanceof SquareEntity
        ) {
            NetworkManager.register((SquareEntity) entity);
        }

        if (entity.getRendererC() != null) {
            RenderManager.register(entity);
            if (entity instanceof SquareEntity) {
                SceneManager.getPane().getChildren().add(entity.getHitbox());
            }
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

        if (entity.getRendererC() != null) {
            RenderManager.unregister(entity);
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
            if (entity.getRendererC() != null) {
                RenderManager.unregister(entity);
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
