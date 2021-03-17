package org.bioshock.main;

import java.util.Arrays;

import org.bioshock.engine.core.WindowManager;
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
        startGame(stage, new MainGame(), false);

        stage.show();
	}

    public static void main(String[] args) {
        launch();
    }
}
