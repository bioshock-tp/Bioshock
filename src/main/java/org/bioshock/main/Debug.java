package org.bioshock.main;

import org.bioshock.engine.core.GameLoop;
import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.input.InputManager;
import org.bioshock.engine.scene.SceneManager;
import org.bioshock.scenes.MainGame;

import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class Debug extends App {
    @Override
    public void start(Stage stage) {
		WindowManager.initialize(stage);
        startGame(stage);
	}

    public static void startGame(Stage primaryStage) {
        SceneManager.initialize(primaryStage, new MainGame());
        InputManager.initialize();
        InputManager.onPress(KeyCode.C, () ->
            App.logger.debug(SceneManager.getScene()));

		primaryStage.setScene(SceneManager.getScene());
		primaryStage.show();

		GameLoop loop = new GameLoop();
		loop.start();
	}

    public static void main(String[] args) {
        launch();
    }
}
