package org.bioshock.engine.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bioshock.engine.networking.NetworkManager;
import org.bioshock.engine.rendering.RenderManager;

public final class EntityManager {
	private static ArrayList<Entity> rendered = new ArrayList<>();

    private EntityManager() {}
	
	public static void tick(double timeDelta) {
		for (Entity entity : rendered) {
			entity.safeTick(timeDelta);
		}
	}
	
	public static void register(Entity entity) {
        NetworkManager.register(entity);
        RenderManager.register(entity);
        rendered.add(entity);
	}

	public static void registerAll(Entity... toAdd) {
        NetworkManager.registerAll(Arrays.asList(toAdd));
        RenderManager.registerAll(Arrays.asList(toAdd));
        rendered.addAll(Arrays.asList(toAdd));
	}

	public static void unregister(Entity entity) {
        NetworkManager.unregister(entity);
        RenderManager.unregister(entity);
        rendered.remove(entity);
	}

    public static void unregisterAll() {
        NetworkManager.unregisterAll(rendered);
        RenderManager.unregisterAll(rendered);
        rendered.clear();
    }

    public static void unregisterAll(Entity... entities) {
        NetworkManager.unregisterAll(Arrays.asList(entities));
        RenderManager.unregisterAll(Arrays.asList(entities));
        rendered.removeAll(Arrays.asList(entities));
    }

    public static boolean areRendered(Entity entity, Entity... entities) {
        return rendered.contains(entity)
            && rendered.containsAll(Arrays.asList(entities));
    }

    public static Entity[] getEntities() {
        return rendered.toArray(new Entity[0]);
    }

    public static List<Entity> getEntityList() {
        return rendered;
    }
}
