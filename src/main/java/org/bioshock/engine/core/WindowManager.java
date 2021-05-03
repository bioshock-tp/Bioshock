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
     * Changes the window fullscreen state to the opposite of what it is currently
     */
    private static void toggleFullScreen() {
        window.setFullScreen(!window.isFullScreen());
    }

    /**
     * Sets the window to be fullscreen or not
     * @param b whether to set the window to fullscreen or not
     * 		true = make window fullscreen
     * 		false = make window not fullscreen
     */
    public static void setFullScreen(boolean b) {
        window.setFullScreen(b);
    }

    /**
     * getter
     * @return the current window width
     */
    public static double getWindowWidth() {
        return window.getWidth();
    }

    /**
     * getter
     * @return the current window height
     */
    public static double getWindowHeight() {
        return window.getHeight();
    }

    /**
     * getter
     * @return the current stage/window
     */
    public static Stage getWindow() {
        return window;
    }
}
