package org.bioshock.engine.scene;

import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.entity.EntityManager;
import org.bioshock.engine.input.InputManager;
import org.bioshock.engine.networking.NetworkManager;
import org.bioshock.main.App;
import org.bioshock.scenes.GameScene;
import org.bioshock.scenes.MainGame;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public final class SceneManager {
    private static Stage stage;
    private static GameScene currentScene;
    private static boolean isGameStarted = false;

    private SceneManager() {}

    public static void initialize(Stage primaryStage, GameScene initialScene) {
		stage = primaryStage;
        currentScene = initialScene;
	}

	public static void setScene(GameScene scene) {
        currentScene = scene;

        WindowManager.setFullScreen(true);

        EntityManager.unregisterAll();

        InputManager.changeScene();

        stage.setScene(currentScene);

        currentScene.renderEntities();

        if (currentScene instanceof MainGame) {
            isGameStarted = true;

            Object mutex = NetworkManager.getMutex();
            synchronized(mutex) {
                mutex.notifyAll();
            }

            App.logger.debug("Notified networking thread");
        }
	}

	public static Scene getScene() {
		return currentScene;
	}

    public static StackPane getPane() {
		return currentScene.getPane();
	}

	public static Canvas getCanvas() {
		return currentScene.getCanvas();
	}

	public static boolean isGameStarted() {
        return isGameStarted;
	}
}
