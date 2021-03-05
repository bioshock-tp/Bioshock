package org.bioshock.main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bioshock.audio.AudioController;
import org.bioshock.engine.core.GameLoop;
import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.input.InputManager;
import org.bioshock.engine.scene.SceneManager;
import org.bioshock.gui.MainController;
import org.bioshock.scenes.LoadingScreen;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public final class App extends Application {
	public static final Logger logger = LogManager.getLogger(App.class);
    private static Scene fxmlScene;

	@Override
	public void start(Stage stage) throws URISyntaxException {
		WindowManager.initialize(stage);
        initFXMLScene();
		stage.setScene(fxmlScene);
		stage.show();
		AudioController audioController = new AudioController(true);
	}

    public static void startGame(Stage primaryStage) {
		SceneManager.initialize(primaryStage, new LoadingScreen());
        InputManager.initialize();
        InputManager.onPressListener(KeyCode.C, () ->
            App.logger.debug(SceneManager.getScene()));

		primaryStage.setScene(SceneManager.getScene());
		primaryStage.show();

		GameLoop loop = new GameLoop();
		loop.start();
	}

	public static void exit() {
        Platform.exit();
        System.exit(0);
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
            exit();
            return null; /* Prevents no return value warning */
        }
	}

	public static void main(String[] args) {
		launch();
	}
}
