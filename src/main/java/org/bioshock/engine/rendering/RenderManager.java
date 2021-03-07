package org.bioshock.engine.rendering;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.bioshock.engine.entity.Entity;
import org.bioshock.engine.scene.SceneManager;
import org.bioshock.main.App;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public final class RenderManager {
    private static ArrayList<Entity> entities = new ArrayList<>();

    private RenderManager() {
    }

    /**
     * A method that attempts to render every entity registered to the
     * RenderManager in Ascending Y order but cannot render if it has no canvas
     * to render entities on before rendering it sets the entire canvas to
     * LIGHTGREY
     */
    public static void tick() {
        Canvas canvas = SceneManager.getCanvas();
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Set Background to LightGrey
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // renders each entity with the renderer defined in the map
        for (Entity entity : entities) {
            if (entity.isEnabled()) {
                try {
                    Method rend = entity.getRenderer().getDeclaredMethods()[0];
                    rend.invoke(null, gc, entity);
                } catch (Exception e) {
                    App.logger.error(
                        "Render function not defined for {}",
                        entity.getRenderer()
                    );
                }
			}
		}
	}

	/**
	 * Registers an entity to the RenderManager and Stores it in ascending
     * order with regards to it's Y value given in it's render component
	 * @param entityToAdd
	 */
	public static void register(Entity entityToAdd) {
		if (entities.isEmpty()) {
			entities.add(entityToAdd);
		} else {
			int i;
			Entity currEnt = entities.get(0);
			for (
                i = 1;
                (currEnt.getZ() < entityToAdd.getZ()) && i < entities.size();
                i++
            ) {
				currEnt = entities.get(i);
			}

			entities.add(i, entityToAdd);
		}
	}

    public static void registerAll(List<Entity> entities) {
        entities.forEach(RenderManager::register);
	}

    /**
	 * Unregisters an entity from the RenderManager
	 * @param entity Entity to remove
     * @return True if entity was registered
     */
	public static boolean unregister(Entity entity) {
		return entities.remove(entity);
	}

	public static void unregisterAll(List<Entity> entities) {
        entities.forEach(RenderManager::unregister);
	}
}