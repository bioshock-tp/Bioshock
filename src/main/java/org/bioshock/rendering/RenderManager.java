package org.bioshock.rendering;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bioshock.engine.core.FrameRate;
import org.bioshock.entities.Entity;
import org.bioshock.entities.EntityManager;
import org.bioshock.entities.SquareEntity;
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
import javafx.util.Pair;

public final class RenderManager {
    private static boolean clip = true;

    private static List<Entity> entities = new ArrayList<>();
    private static Point2D cameraPos = new Point2D(0,0);
    private static Point2D scale = new Point2D(1.0, 1.0);
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
        Rectangle screen = new Rectangle(cameraPos.getX(), cameraPos.getY(), screenSize.getWidth(), screenSize.getHeight());

        // renders each entity
        entities.stream().filter(Entity::isEnabled).forEach(entity -> {
            if (
                entity.isAlwaysRender() ||
                intersects(entity.getRenderArea(), screen)
            ) {
                try {
                    Method rend = entity.getRenderer().getDeclaredMethods()[0];
                    rend.invoke(null, gc, entity);
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
        Shape intersects = Shape.intersect(
                s1,
                s2
            );

        return (intersects.getBoundsInLocal().getWidth() != -1);
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
            for (i = 0; (currEnt.getZ() < entity.getZ()) && i < N; i++) {
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

    public static void updateScreenSize() {
        FrameRate.updatePosition();
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
        return w * scale.getX() + padding;
    }

    public static double getRenX(double x) {
        return getRenWidth(x - cameraPos.getX());
    }

    public static double getRenHeight(double h) {
        return h * scale.getY() + padding;
    }

    public static double getRenY(double y) {
        return getRenHeight(y - cameraPos.getY());
    }

    public static Point2D getScale() {
        return scale;
    }

    public static boolean isClip() {
        return clip;
    }

    public static void setClip(boolean clip) {
        RenderManager.clip = clip;
    }
}
