package org.bioshock.engine.core;

import org.bioshock.engine.input.InputManager;
import org.bioshock.engine.scene.SceneManager;
import org.bioshock.main.App;
import org.bioshock.scenes.GameScene;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class WindowManager {
    private static final boolean INITFULLSCREEN = false;
    private static final boolean INITMAXIMISED = true;

    private static Stage window;

    private WindowManager() {}

    public static void initialise(Stage stage) {
        window = stage;
        window.setTitle(App.NAME);
        window.setFullScreen(INITFULLSCREEN);
        window.setMaximized(INITMAXIMISED);

        window.setFullScreenExitHint("");
        window.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        InputManager.onPress(
            KeyCode.F11, WindowManager::toggleFullScreen
        );

        InputManager.onPress(KeyCode.ESCAPE, () -> App.exit(0));

        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            GameScene s = SceneManager.getScene();
            if (s != null) {
                Canvas c = SceneManager.getScene().getCanvas();
                if (c != null) {
                    c.setWidth(newVal.floatValue());
                    s.scaleCanvas();
                }
            }
        });
        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            GameScene s = SceneManager.getScene();
            if (s != null) {
                Canvas c = SceneManager.getScene().getCanvas();
                if (c != null) {
                    c.setHeight(newVal.floatValue());
                    s.scaleCanvas();
                }
            }
        });
    }

    private static void toggleFullScreen() {
        window.setFullScreen(!window.isFullScreen());
    }

    public static void setFullScreen(boolean b) {
        window.setFullScreen(b);
    }

    public static double getWindowWidth() {
        return window.getWidth();
    }

    public static double getWindowHeight() {
        return window.getHeight();
    }

    public static Stage getWindow() {
        return window;
    }
}
