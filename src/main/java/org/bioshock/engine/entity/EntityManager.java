package org.bioshock.engine.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bioshock.engine.networking.NetworkManager;
import org.bioshock.engine.rendering.RenderManager;

public final class EntityManager {
	private static ArrayList<Entity> entities = new ArrayList<>();

    private EntityManager() {}

	public static void tick(double timeDelta) {
		for (Entity entity : entities) {
			entity.safeTick(timeDelta);
		}
	}

	public static void register(Entity entity) {
        if (entity.getNetworkC().isNetworked()) {
            NetworkManager.register(entity);
        }
        if (entity.getRendererC() != null) {
            RenderManager.register(entity);
        }
        entities.add(entity);
	}

	public static void registerAll(Entity... toAdd) {
        Arrays.asList(toAdd).forEach(EntityManager::register);
	}

	public static void unregister(Entity entity) {
        if (entity.getNetworkC().isNetworked()) {
            NetworkManager.unregister(entity);
        }
        if (entity.getRendererC() != null) {
            RenderManager.unregister(entity);
        }
        entities.remove(entity);
	}

    public static void unregisterAll() {
        NetworkManager.unregisterAll(entities);
        RenderManager.unregisterAll(entities);
        entities.clear();
    }

    public static void unregisterAll(Entity... toAdd) {
        Arrays.asList(toAdd).forEach(EntityManager::unregister);
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
}
