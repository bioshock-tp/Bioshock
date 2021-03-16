package org.bioshock.main;

import java.util.Arrays;

import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.scene.SceneManager;
import org.bioshock.scenes.LoadingScreen;
import org.bioshock.scenes.MainGame;

import javafx.stage.Stage;

public class Debug extends App {
    @Override
    public void start(Stage stage) {
        Thread.setDefaultUncaughtExceptionHandler((thread, exception) ->
            App.logger.error(
                "{}\n{}",
                exception,
                Arrays.toString(exception.getStackTrace()).replace(',', '\n')
            )
        );

        App.setPlayerCount(1);
		WindowManager.initialise(stage);
        startGame(stage, new LoadingScreen(false), false);
        SceneManager.setScene(new MainGame());

        stage.show();
	}

    public static void main(String[] args) {
        launch();
    }
}
