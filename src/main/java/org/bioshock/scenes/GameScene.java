package org.bioshock.scenes;

import java.util.ArrayList;
import java.util.List;

import org.bioshock.engine.core.WindowManager;
import org.bioshock.entities.Entity;
import org.bioshock.entities.EntityManager;
import org.bioshock.rendering.RenderManager;
import org.bioshock.utils.Size;

import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;


/**
 * The base class for this game's scenes
 */
public abstract class GameScene extends Scene {
    /**
     * The {@code StackPane} this scene adds to
     */
    private StackPane pane;

    /**
     * The {@code Canvas} this scene draws on
     */
    private Canvas canvas = new Canvas(
        WindowManager.getWindowWidth(),
        WindowManager.getWindowHeight()
    );

    private static Size gameScreen = new Size(1920, 1080); //1080p 16:9

    /**
     * The entities that extend {@link Entity} to be drawn on the
     * {@link #canvas}
     */
    protected List<Entity> children = new ArrayList<>();


    /**
     * Creates a new {@code GameScene} using the {@code StackPane} provided by
     * {@link #GameScene()}
     * @param pane the {@code StackPane} to initialise the {@link #pane} field
     * to
     */
    private GameScene(StackPane pane) {
        super(pane);
        this.pane = pane;
        pane.getChildren().add(canvas);
        scaleCanvas();
    }


    /**
     * Initialises the {@code StackPane} to use in
     * {@link #GameScene(StackPane)}
     * so that it can be used in the {@code super} call and be assigned to the
     * {@link #pane} field
     */
    protected GameScene() {
        this(new StackPane());
    }


    /**
     * Initialises the scene and registers the scene's {@link #children} to
     * both the {@link org.bioshock.entities.EntityManager EntityManager}
     * and the
     * {@link org.bioshock.rendering.RenderManager RenderManager}. <p />
     * Called in
     * {@link org.bioshock.scenes.SceneManager#setScene(GameScene)
     * SceneManager.setScene(GameScene)}
     */
    public void initScene() {
        registerEntities();
        renderEntities();
    }


    /**
     * Render tick method that can be overridden if the scene needs it
     * @param timeDelta currently only takes 0 as it isn't utilised
     */
    public void renderTick(double timeDelta) {}


    /**
     * Logic tick method that can be overridden if the scene needs it
     * @param timeDelta the time for a logic tick in seconds
     */
    public void logicTick(double timeDelta) {}


    /**
     * Registers the scene's {@link #children} to the
     * {@link org.bioshock.entities.EntityManager EntityManager}
     * @see #renderEntities()
     */
    public void registerEntities() {
        children.forEach(EntityManager::register);
    }


    /**
     * Registers the scene's {@link #children} to the
     * {@link org.bioshock.rendering.RenderManager RenderManager}
     * @see #registerEntities()
     */
    public void renderEntities() {
        children.forEach(RenderManager::register);
    }


    /**
     * Unregisters the scene's {@link #children} from the
     * {@link org.bioshock.entities.EntityManager EntityManager}
     * @see #destroyEntities()
     */
    public void unregisterEntities() {
        children.forEach(EntityManager::unregister);
    }


    /**
     * Unregisters the scene's {@link #children} from the
     * {@link org.bioshock.rendering.RenderManager RenderManager}
     * @see #unregisterEntities()
     */
    public void destroyEntities() {
        children.forEach(RenderManager::unregister);
    }


    /**
     * Scales the canvas to the current Window Width
     */
    public void scaleCanvas() {
        RenderManager.setScale(new Point2D(
            WindowManager.getWindowWidth() / gameScreen.getWidth(),
            WindowManager.getWindowHeight() / gameScreen.getHeight())
        );
    }


    /**
     * Sets the background of the scene
     * @param background the {@code Background} of the scene
     */
    public void setBackground(Background background) {
        pane.setBackground(background);
    }


    /**
     * @return StackPane the scene's {@link #pane}
     * @see #getCanvas()
     */
    public StackPane getPane() {
        return pane;
    }


    /**
     * @return Canvas the scene's {@link #canvas}
     * @see #getPane()
     * @see Canvas#getGraphicsContext2D()
     */
    public Canvas getCanvas() {
        return canvas;
    }


    /**
     *
     * @return The size of the game screen
     */
    public static Size getGameScreen() {
        return gameScreen;
    }


    /**
     * Destroys the scene and unregisters the scene's {@link #children} from
     * both the {@link org.bioshock.entities.EntityManager EntityManager}
     * and the
     * {@link org.bioshock.rendering.RenderManager RenderManager}.
     * Called in
     * {@link org.bioshock.scenes.SceneManager#setScene(GameScene)
     * SceneManager.setScene(GameScene)}
     */
    public void destroy() {
        destroyEntities();
        unregisterEntities();
    }
}
