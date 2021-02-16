package org.bioshock.engine.core;

import org.bioshock.engine.input.InputManager;

import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class WindowInitializer {
	
	private static Rectangle2D screenSize = null;
	private static final String gameName = "BuzzKill";
	private static final boolean startFullscreen = true;
	private static final boolean startMaximised = true;
	private static final KeyCode exitFullscreen = KeyCode.F11;

	public static void initWindow(Stage window) {
		window.setTitle(gameName);
		window.setFullScreen(startFullscreen);		
		window.setMaximized(startMaximised);
		
		window.setFullScreenExitHint("");
		window.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		
		AnimationTimer inputTick = new AnimationTimer() {
			
			@Override
			public void handle(long now) {
				if (InputManager.checkKeyUp(exitFullscreen)) {
					window.setFullScreen(!window.isFullScreen());
				}				
			}
		};
		
		inputTick.start();
	}
	
	public static double getWindowWidth() {
		if (screenSize == null) {
			screenSize = Screen.getPrimary().getBounds();
		}
		return screenSize.getWidth();
	}

	public static double getWindowHeight() {
		if (screenSize == null) {
			screenSize = Screen.getPrimary().getBounds();
		}
		return screenSize.getHeight();
	}
}
