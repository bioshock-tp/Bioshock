package org.bioshock.engine.core;

import static org.bioshock.main.App.exit;

import org.bioshock.engine.input.InputManager;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class WindowManager {
	private static Bounds screenSize = new BoundingBox(0, 0, 1920, 1080);
	private static final String NAME = "BuzzKill";
	private static final boolean INITFULLSCREEN = false;
	private static final boolean INITMAXIMISED = true;
    private static Stage window;

    private WindowManager() {}

	public static void initialise(Stage stage) {
        window = stage;
		window.setTitle(NAME);
		window.setFullScreen(INITFULLSCREEN);
		window.setMaximized(INITMAXIMISED);

		window.setFullScreenExitHint("");
		window.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        InputManager.onPress(KeyCode.F11, WindowManager::toggleFullScreen);

        InputManager.onPress(KeyCode.ESCAPE, () -> exit(0));
    }

	private static void toggleFullScreen() {
        window.setFullScreen(!window.isFullScreen());
    }

    public static int getWindowWidth() {
		return (int) screenSize.getWidth();
	}

	public static int getWindowHeight() {
		return (int) screenSize.getHeight();
	}

	public static Stage getWindow() {
		return window;
	}
}
