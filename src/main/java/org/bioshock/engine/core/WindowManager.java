package org.bioshock.engine.core;

import org.bioshock.engine.input.InputManager;
import org.bioshock.main.App;

import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class WindowManager {
	private static final boolean INITFULLSCREEN = false;
	private static final boolean INITMAXIMISED = true;

	private static Rectangle2D screenSize = Screen.getPrimary().getBounds();
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
