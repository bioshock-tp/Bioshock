package org.bioshock.engine.core;

import org.bioshock.engine.input.InputManager;
import org.bioshock.main.App;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class WindowManager {
	private static final boolean INITFULLSCREEN = false;
	private static final boolean INITMAXIMISED = true;

	private static Bounds screenSize = new BoundingBox(0, 0, 1920, 1080);
    private static Stage window;

    private WindowManager() {}

	public static void initialize(Stage stage) {
        window = stage;
		window.setTitle(App.NAME);
		window.setFullScreen(INITFULLSCREEN);
		window.setMaximized(INITMAXIMISED);

		window.setFullScreenExitHint("");
		window.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        InputManager.onPressListener(
            KeyCode.F11, WindowManager::toggleFullScreen
        );

        InputManager.onPressListener(KeyCode.ESCAPE, App::exit);
    }

	private static void toggleFullScreen() {
        window.setFullScreen(!window.isFullScreen());
    }

    public static void setFullScreen(boolean b) {
        window.setFullScreen(b);
    }

    public static double getWindowWidth() {
		return screenSize.getWidth();
	}

	public static double getWindowHeight() {
		return screenSize.getHeight();
	}

	public static Stage getWindow() {
		return window;
	}
}
