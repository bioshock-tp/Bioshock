package org.bioshock.engine.rendering;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bioshock.engine.core.FrameRate;
import org.bioshock.engine.entity.Entity;
import org.bioshock.engine.entity.SquareEntity;
import org.bioshock.engine.scene.SceneManager;
import org.bioshock.main.App;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public final class RenderManager {
    private static List<Entity> entities = new ArrayList<>();

    private RenderManager() {}

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

        // renders each entity
        entities.stream().filter(Entity::isEnabled).forEach(entity -> {
            try {
                Method rend = entity.getRenderer().getDeclaredMethods()[0];
                rend.invoke(null, gc, entity);
            } catch (Exception e) {
                App.logger.error(
                    "Render function not defined for {}",
                    entity.getRenderer()
                );
            }
        });
    }

    /**
     * Registers an entity to the RenderManager and Stores it in ascending
     * order with regards to it's Y value given in it's render component
     * @param entity
     */
    public static void register(Entity entity) {
        if (entity.getRendererC() == null) {
            return;
        }

        if (entity instanceof SquareEntity) {
            SceneManager.getPane().getChildren().add(entity.getHitbox());
        }

        if (entities.isEmpty()) {
            entities.add(entity);
        } else {
            Entity currEnt = entities.get(0);

            int i;
            final int N = entities.size();
            for (i = 1; (currEnt.getZ() < entity.getZ()) && i < N; i++) {
                currEnt = entities.get(i);
            }

            entities.add(i, entity);
        }
    }

    public static void registerAll(Collection<Entity> entities) {
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

    public static void unregisterAll(Collection<Entity> entities) {
        entities.forEach(RenderManager::unregister);
    }

    public static void updateScreenSize() {
        FrameRate.updatePosition();
    }
}
