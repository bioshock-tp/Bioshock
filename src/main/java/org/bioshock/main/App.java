package org.bioshock.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.bioshock.engine.core.GameLoop;
import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.input.InputManager;
import org.bioshock.engine.scene.SceneManager;
import org.bioshock.scenes.LoadingScreen;

import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public final class App extends Application{
	public static final Logger logger = LogManager.getLogger(App.class);

	@Override
	public void start(Stage primaryStage) throws Exception {
		WindowManager.initialize(primaryStage);
		SceneManager.initialize(primaryStage, new LoadingScreen());
        InputManager.initialize();
        InputManager.addKeyListener(KeyCode.C, () -> App.logger.debug(SceneManager.getScene()));

		primaryStage.setScene(SceneManager.getScene());
		primaryStage.show();
		
		GameLoop loop = new GameLoop();
		loop.start();
	}
	
	public static void main(String[] args) {
		launch();
	}
}
