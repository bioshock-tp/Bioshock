package org.bioshock.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bioshock.engine.core.GameLoop;
import org.bioshock.engine.core.WindowInitializer;
import org.bioshock.engine.input.InputChecker;
import org.bioshock.engine.input.JavaFXInputChecker;
import org.bioshock.engine.scene.GameScene;
import org.bioshock.engine.scene.SceneController;
import org.bioshock.scenes.LoadingScreen;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public final class App extends Application{
	private Scene root;
	private GameScene initialScene = new LoadingScreen();
	
	public static final Logger logger = LogManager.getLogger(App.class);

	@Override
	public void start(Stage primaryStage) throws Exception {
		InputChecker.initialize(new JavaFXInputChecker(primaryStage));
		WindowInitializer.initWindow(primaryStage);
		
		root = new Scene(new Pane(), WindowInitializer.getWindowWidth(), WindowInitializer.getWindowHeight());
		SceneController.initialize(root, initialScene);
		primaryStage.setScene(root);
		primaryStage.show();
		
		GameLoop loop = new GameLoop();
		loop.start();
	}
	
	public static void main(String[] args) {
		launch();
	}
}
