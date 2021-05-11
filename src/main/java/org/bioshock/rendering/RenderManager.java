package org.bioshock.rendering;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.bioshock.entities.Entity;
import org.bioshock.entities.EntityManager;
import org.bioshock.entities.players.Hider;
import org.bioshock.entities.players.SeekerAI;
import org.bioshock.main.App;
import org.bioshock.physics.Movement;
import org.bioshock.scenes.GameScene;
import org.bioshock.scenes.SceneManager;
import org.bioshock.utils.GlobalConstants;
import org.bioshock.utils.Size;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;


public final class RenderManager {
    /**
     * Duration of text display animation
     */
    private static final int BLUR_LENGTH = 3000;

    /**
     * Says if entities should be clipped to the FOV of the current player or not
     */
    private static boolean clip = true;

    /**
     * A list of entities that want to be rendered
     */
    private static List<Entity> entities = new ArrayList<>();

    /**
     * The position of the camera
     */
    private static Point2D cameraPos = new Point2D(0, 0);

    /**
     * The amount to scale objects drawn on the canvas
     */
    private static Point2D scale = new Point2D(1, 1);

    /**
     * The current zoom on factor
     */
    private static double zoom = 1;

    /**
     * How much bigger to make every rendered object so they join up correctly
     * with floating point error
     */
    private static double padding = 1;

    /**
     * Label to display text in the centre of the screen
     */
    private static Label label;


    /**
     * RenderManger is a static class
     */
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
            // Only render entities if the renderArea is somewhere on screen or
            // it is set to always render
            if (
                entity.alwaysRender() ||
                intersects(entity.getRenderArea(), screen)
            ) {
                try {
                    //Invoke render on every entity with their specific renderer
                    //They don't have their own copy of the renderer so there is only
                    //one type of each renderer
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


    /**
     * @return True if s1 and s2 intersect
     */
    private static boolean intersects(Shape s1, Shape s2) {
        return Shape.intersect(s1, s2).getBoundsInLocal().getWidth() != -1;
    }


    /**
     * Registers an entity to the RenderManager and stores it in ascending
     * order with regards to it's Z value
     * @param entity The entity to register
     */
    public static void register(Entity entity) {
        if (entity.getRendererC() == null || entities.contains(entity)) {
            return;
        }

        SceneManager.getPane().getChildren().add(entity.getHitbox());

        if (entities.isEmpty()) {
            entities.add(entity);
        } else {
            Entity currentEntity = entities.get(0);

            int i;
            final int N = entities.size();
            /* If entities identical Z values, insert the newer entity first */
            for (i = 0; currentEntity.getZ() < entity.getZ() && i < N; i++) {
                currentEntity = entities.get(i);
            }

            entities.add(i, entity);
        }
    }


    /**
     * Registers all of the entities in the list
     * @param entities List of entities to register
     */
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


    /**
     * Unregisters every entity in the list
     * @param entities List of entities to remove
     */
    public static void unregisterAll(Collection<Entity> entities) {
        entities.forEach(RenderManager::unregister);
    }


    /**
     * Unregisters every entity
     */
    public static void unregisterAll() {
        entities.forEach(RenderManager::unregister);
    }


    /**
     * Blurs the canvas and slows the player
     */
    public static void endGame() {
        RenderManager.setClip(false);

        GaussianBlur blur = new GaussianBlur(0);
        SceneManager.getCanvas().setEffect(blur);
        Timeline blurTimeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(
            Duration.millis(BLUR_LENGTH),
            new KeyValue(blur.radiusProperty(), 10)
        );
        blurTimeline.getKeyFrames().add(keyFrame);
        blurTimeline.play();

        Timeline slowTimeline = new Timeline();

        List<Movement> movements = EntityManager.getPlayers().stream()
            .map(Hider::getMovement)
            .collect(Collectors.toCollection(ArrayList::new)
        );

        movements.addAll(EntityManager.getSeekers().stream()
            .map(SeekerAI::getMovement)
            .collect(Collectors.toList())
        );

        KeyFrame slowFrame = new KeyFrame(Duration.millis(1), e ->
            movements.forEach(movement -> {
                double slowFactor = movement.getSpeed() / BLUR_LENGTH;
                movement.setSpeed(movement.getSpeed() - slowFactor);
            })
        );

        slowTimeline.getKeyFrames().add(slowFrame);
        slowTimeline.setCycleCount(BLUR_LENGTH);
        slowTimeline.setOnFinished(e ->
            movements.forEach(movement -> movement.setSpeed(0))
        );
        slowTimeline.play();
    }


    /**
     * Displays a string in large text across the centre of the screen
     * (with fade in effect)
     * @param string Text to display
     */
    public static void displayText(String string) {
        displayText(string, () -> {});
    }


    /**
     * Displays a string in large text across the centre of the screen <p />
     * Once text is displayed calls callback function
     * (with fade in effect)
     * @param string Text to display
     * @param callback Function to call after text is displayed
     */
    public static void displayText(String string, Runnable callback) {
        if (label != null) SceneManager.getPane().getChildren().remove(label);

        label = new Label(string);
        label.setOpacity(0);

        SceneManager.getPane().getStylesheets().add(
            GlobalConstants.STYLESHEET_PATH
        );

        label.getStyleClass().add("paragraph");

        FadeTransition fadeTransition = new FadeTransition(
            Duration.millis(BLUR_LENGTH),
            label
        );
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.setOnFinished(e -> callback.run());

        fadeTransition.play();

        SceneManager.getPane().getChildren().add(label);
    }


    /**
     * Displays a string in large text across the centre of the screen
     * @param string Text to display
     */
    public static void initLabel(String string) {
        if (label != null) SceneManager.getPane().getChildren().remove(label);

        label = new Label(string);

        SceneManager.getPane().getStylesheets().add(
            GlobalConstants.STYLESHEET_PATH
        );

        label.getStyleClass().add("countdown");

        SceneManager.getPane().getChildren().add(label);
    }


    /**
     * Removes label (if exists) from screen
     */
    public static void displayText() {
        if (label != null) SceneManager.getPane().getChildren().remove(label);
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


    /**
     * Moves the x position of the camera by adding x to the current x
     * coordinate of the camera
     * @param x The amount to add to the x coordinate
     */
    public static void moveCameraX(double x) {
        cameraPos.add(x, 0);
    }


    /**
     * Moves the y position of the camera by adding y to the current y
     * coordinate of the camera
     * @param y The amount to add to the y coordinate
     */
    public static void moveCameraY(double y) {
        cameraPos.add(0, y);
    }


    /**
     * Set the new position of the camera
     * @param cameraPos the new position of the camera
     */
    public static void setCameraPos(Point2D cameraPos) {
        RenderManager.cameraPos = cameraPos;
    }


    /**
     * Set the scale of the renderManager
     * @param scale The new scale
     */
    public static void setScale(Point2D scale) {
        RenderManager.scale = scale;
    }


    /**
     * @return The current camera position
     */
    public static Point2D getCameraPos() {
        return cameraPos;
    }


    /**
     * Used to make all widths look the same no matter the size of the window
     * @param w the logical width to get the render width of
     * @return The width to render the given object on the current canvas
     */
    public static double getRenWidth(double w) {
        return w * scale.getX() * zoom + padding;
    }


    /**
     * The same as {@link #getRenWidth(double)} but doesn't apply zoom to the
     * rendering
     * @see #getRenWidth(double)
     */
    public static double getRenWidthUnzoomed(double width) {
        return width * scale.getX() + padding;
    }


    /**
     * Gets the x coordinate on the canvas something should be rendered at
     * based of the logical x coordinate and the position of the camera
     * @param x The logical x coordinate
     * @return The x coordinate on the canvas
     */
    public static double getRenX(double x) {
        return getRenWidth(x - cameraPos.getX());
    }


    /**
     * Used to make all heights look the same no matter the size of the window
     * @param h the logical height to get the render height of
     * @return The height to render the given object on the current canvas
     */
    public static double getRenHeight(double h) {
        return h * scale.getY() * zoom + padding;
    }


    /**
     * The same as {@link #getRenHeight(double)} but doesn't apply zoom to the
     * rendering
     * @see #getRenHeight(double)
     */
    public static double getRenHeightUnzoomed(double h) {
        return h * scale.getY() + padding;
    }


    /**
     * Gets the y coordinate on the canvas something should be rendered at
     * based of the logical y coordinate and the position of the camera
     * @param y The logical y coordinate
     * @return The y coordinate on the canvas
     */
    public static double getRenY(double y) {
        return getRenHeight(y - cameraPos.getY());
    }


    /**
     * @return The current scale
     */
    public static Point2D getScale() {
        return scale;
    }


    /**
     * @return If clip is turned on or not
     */
    public static boolean clips() {
        return clip;
    }


    /**
     * @param clip The new value of clip
     */
    public static void setClip(boolean clip) {
        RenderManager.clip = clip;
    }


    /**
     * @return the current zoom factor
     */
    public static double getZoom() {
        return zoom;
    }


    /**
     * @param zoom The new zoom factor
     */
    public static void setZoom(double zoom) {
        RenderManager.zoom = zoom;
    }


    /**
     * @return The current label
     */
    public static Label getLabel() {
        return label;
    }
}
