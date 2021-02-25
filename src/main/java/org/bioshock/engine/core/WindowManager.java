package org.bioshock.engine.core;

import org.bioshock.engine.input.InputManager;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class WindowManager {
	
	private static Rectangle2D screenSize = Screen.getPrimary().getBounds();
	private static final String NAME = "BuzzKill";
	private static final boolean INITFULLSCREEN = false;
	private static final boolean INITMAXIMISED = true;
    private static Stage window;

    private WindowManager() {}

	public static void initialize(Stage stage) {
        window = stage;
		window.setTitle(NAME);
		window.setFullScreen(INITFULLSCREEN);		
		window.setMaximized(INITMAXIMISED);
		
		window.setFullScreenExitHint("");
		window.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		
        InputManager.onPressListener(
            KeyCode.F11, WindowManager::toggleFullScreen
        );

        InputManager.onPressListener(KeyCode.ESCAPE, () -> {
            Platform.exit();
            System.exit(0);
        });
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
