package org.bioshock.main;

import org.bioshock.engine.core.WindowManager;
import org.bioshock.scenes.MainGame;

import javafx.stage.Stage;

public class Debug extends App {
    @Override
    public void start(Stage stage) {
		WindowManager.initialise(stage);
        startGame(stage, new MainGame());
	}

    public static void main(String[] args) {
        launch();
    }
}
