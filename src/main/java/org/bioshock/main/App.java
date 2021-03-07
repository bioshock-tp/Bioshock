package org.bioshock.main;

import java.io.IOException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bioshock.audio.AudioController;
import org.bioshock.audio.MusicController;
import org.bioshock.audio.settings.MusicSettings;
import org.bioshock.engine.core.GameLoop;
import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.input.InputManager;
import org.bioshock.engine.scene.SceneManager;
import org.bioshock.gui.MainController;
import org.bioshock.scenes.GameScene;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class App extends Application {
	public static final Logger logger = LogManager.getLogger(App.class);
    private static Scene fxmlScene;
	private MusicController musicController;

	@Override
	public void start(Stage stage) {
		WindowManager.initialise(stage);
        initFXMLScene();

        AudioController.initialise();
		playBackgroundMusic();
		stage.setScene(fxmlScene);
		stage.show();
    }

    public static void startGame(Stage primaryStage, GameScene initScene) {
		SceneManager.initialise(primaryStage, initScene);
        InputManager.initialise();
        InputManager.onPress(KeyCode.C, () ->
            App.logger.debug(SceneManager.getScene())
        );

        InputManager.onPress(KeyCode.R, () -> {
            App.logger.debug("Resetting Scene...");
            try {
                SceneManager.getScene().destroy();
                GameScene scene = SceneManager.getScene();
                Class<? extends GameScene> sceCl = scene.getClass();
                GameScene nSce = sceCl.getDeclaredConstructor().newInstance();
                SceneManager.setScene(nSce);
            } catch (Exception e) {
                App.logger.error(
                    "Error whilst changing scene: {}",
                    e.getMessage()
                );
            }
        });

		primaryStage.setScene(SceneManager.getScene());
		primaryStage.show();

		new GameLoop().start();
	}

	public static void exit(int code) {
        Platform.exit();
        System.exit(code);
	}

	public void stopBackgroundMusic() {
		musicController = AudioController.loadMusicController(
            "background-music"
        );
		musicController.stop();
	}

	public void playBackgroundMusic() {
		musicController = AudioController.loadMusicController(
            "background-music"
        );
		final MusicSettings settings = new MusicSettings();
		settings.setVolume(0.5);
		settings.setCycleCount(-1);
		musicController.play(settings);
	}

	public static void setFXMLRoot(String fxml) {
		fxmlScene.setRoot(loadFXML(fxml));
	}

    private static void initFXMLScene() {
        fxmlScene = new Scene(loadFXML("main"));
    }

    private static Parent loadFXML(String fxml) {
        try {
            URL location = MainController.class.getResource(fxml + ".fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(location);
            return fxmlLoader.load();
        } catch (IOException e) {
            App.logger.error("Error loading FXML");
            exit(-1);
            return null; /* Prevents no return value warning */
        }
	}

	public static void main(String[] args) {
		launch();
	}
}
