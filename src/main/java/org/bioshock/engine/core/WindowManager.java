package org.bioshock.engine.core;

import org.bioshock.engine.input.InputManager;
import org.bioshock.main.App;
import org.bioshock.scenes.GameScene;
import org.bioshock.scenes.SceneManager;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

/**
 *
 * A class used to manage a stage
 */
public class WindowManager {
	/**
	 * A boolean to represent whether to start the game in fullscreen or not
	 */
    private static final boolean INITFULLSCREEN = false;

    /**
     * A boolean to represent whether to start the game maximised or not
     */
    private static final boolean INITMAXIMISED = true;

    /**
     * The stage the game is running in
     */
    private static Stage window;


    /**
     * Private as WindowManager is meant to be used as a static class
     */
    private WindowManager() {}


    /**
     * Initialise the window manager
     * @param stage The current javafx stage that needs to be managed
     */
    public static void initialise(Stage stage) {
    	//Set the current stage to the initial values and set the title
    	//to the title stored in App
        window = stage;
        window.setTitle(App.getName());
        window.setFullScreen(INITFULLSCREEN);
        window.setMaximized(INITMAXIMISED);

        //Remove the ability to exit out of fullscreen using javafx
        window.setFullScreenExitHint("");
        window.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        //Make it so when F11 is pressed it toggles the game to fullscreen
        InputManager.onPress(
            KeyCode.F11, WindowManager::toggleFullScreen
        );

        //Make it so when you press escape the game exits
        InputManager.onPress(KeyCode.ESCAPE, () -> App.exit(0));

        //Make it so if the width or height of the stage is changed the canvas
        //is scaled appropriately
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            GameScene scene = SceneManager.getScene();
            if (scene != null) {
                Canvas canvas = SceneManager.getScene().getCanvas();
                if (canvas != null) {
                    canvas.setWidth(newVal.floatValue());
                    scene.scaleCanvas();
                }
            }
        });
        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            GameScene scene = SceneManager.getScene();
            if (scene != null) {
                Canvas canvas = SceneManager.getScene().getCanvas();
                if (canvas != null) {
                    canvas.setHeight(newVal.floatValue());
                    scene.scaleCanvas();
                }
            }
        });
    }


    /**
     * Toggles the window's full screen property
     */
    private static void toggleFullScreen() {
        window.setFullScreen(!window.isFullScreen());
    }


    /**
     * @param fullscreen true if window should be full screen
     */
    public static void setFullScreen(boolean fullscreen) {
        window.setFullScreen(fullscreen);
    }


    /**
     * @return the current window width
     */
    public static double getWindowWidth() {
        return window.getWidth();
    }


    /**
     * @return the current window height
     */
    public static double getWindowHeight() {
        return window.getHeight();
    }


    /**
     * @return the current stage/window
     */
    public static Stage getWindow() {
        return window;
    }
}
