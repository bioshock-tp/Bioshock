package org.bioshock.rendering;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bioshock.entities.Entity;
import org.bioshock.entities.EntityManager;
import org.bioshock.entities.players.Hider;
import org.bioshock.main.App;
import org.bioshock.scenes.GameScene;
import org.bioshock.scenes.SceneManager;
import org.bioshock.utils.Size;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public final class RenderManager {
    private static boolean clip = true;

    private static List<Entity> entities = new ArrayList<>();
    private static Point2D cameraPos = new Point2D(0, 0);
    private static Point2D scale = new Point2D(1, 1);
    private static double zoom = 1;
    private static double padding = 1;


    private RenderManager() {}

    /**
     * A method that attempts to render every entity registered to the
     * RenderManager in Ascending Y order but cannot render if it has no canvas
     * to render entities on before rendering it sets the entire canvas to
     * Colour.LIGHTGRAY
     */
    public static void tick() {
        Canvas canvas = SceneManager.getCanvas();
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // clear the entire canvas
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		Size screenSize = GameScene.getGameScreen();
        Rectangle screen = new Rectangle(
            cameraPos.getX(),
            cameraPos.getY(),
            screenSize.getWidth() / zoom,
            screenSize.getHeight() / zoom
        );

        // renders each entity
        entities.stream().filter(Entity::isEnabled).forEach(entity -> {
            if (
                entity.isAlwaysRender() ||
                intersects(entity.getRenderArea(), screen)
            ) {
                try {
                    Arrays.asList(entity.getRenderer().getDeclaredMethods())
                        .stream()
                        .filter(method -> method.getName().equals("render"))
                        .findFirst()
                        .get()
                        .invoke(null, gc, entity);
                } catch (
                    InvocationTargetException
                    | IllegalAccessException
                    | IllegalArgumentException e
                ) {
                    App.logger.error(
                        "Render function not correctly defined for {}",
                        entity.getRenderer(),
                        e
                    );
                }
            }
        });
    }

    private static boolean intersects(Shape s1, Shape s2) {
        return Shape.intersect(s1, s2).getBoundsInLocal().getWidth() != -1;
    }


    /**
     * Registers an entity to the RenderManager and stores it in ascending
     * order with regards to it's Z value given in it's render component
     * @param entity
     */
    public static void register(Entity entity) {
        if (entity.getRendererC() == null) {
            return;
        }

        SceneManager.getPane().getChildren().add(entity.getHitbox());

        if (entities.isEmpty()) {
            entities.add(entity);
        } else {
            Entity currentEntity = entities.get(0);

            int i;
            final int N = entities.size();
            for (i = 0; currentEntity.getZ() < entity.getZ() && i < N; i++) {
                currentEntity = entities.get(i);
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


    /**
     * Used in renderers of entities that should only be visible whilst within
     * the FOV of the player
     * @param gc The {@link GraphicsContext} to draw on
     */
    public static void clipToFOV(GraphicsContext gc) {
        Hider player = EntityManager.getCurrentPlayer();
        if (clip && player != null) {
            double x = player.getX();
            double y = player.getY();
            double radius = player.getRadius();
            double width = player.getWidth();
            double height = player.getHeight();

            gc.beginPath();
            gc.arc(
                getRenX(x + width / 2),
                getRenY(y + height / 2),
                getRenWidth(radius),
                getRenHeight(radius),
                0,
                360
            );
            gc.closePath();
            gc.clip();
        }
    }

    public static void moveCameraX(double x) {
        cameraPos.add(x, 0);
    }

    public static void moveCameraY(double y) {
        cameraPos.add(0, y);
    }

    public static void setCameraPos(Point2D cameraPos) {
        RenderManager.cameraPos = cameraPos;
    }

    public static void setScale(Point2D scale) {
        RenderManager.scale = scale;
    }

    public static Point2D getCameraPos() {
        return cameraPos;
    }

    public static double getRenWidth(double w) {
        return w * scale.getX() * zoom + padding;
    }

    public static double getRenWidthUnzoomed(double w) {
    	return w * scale.getX() + padding;
    }

    public static double getRenX(double x) {
        return getRenWidth(x - cameraPos.getX());
    }

    public static double getRenHeight(double h) {
        return h * scale.getY() * zoom + padding;
    }

    public static double getRenHeightUnzoomed(double h) {
    	return h * scale.getY() + padding;
    }

    public static double getRenY(double y) {
        return getRenHeight(y - cameraPos.getY());
    }

    public static Point2D getScale() {
        return scale;
    }

    public static boolean clips() {
        return clip;
    }

    public static void setClip(boolean clip) {
        RenderManager.clip = clip;
    }

	public static double getZoom() {
		return zoom;
	}

	public static void setZoom(double zoom) {
		RenderManager.zoom = zoom;
	}
}
