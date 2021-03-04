package org.bioshock.engine.scene;

import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.entity.EntityManager;
import org.bioshock.engine.input.InputManager;
import org.bioshock.scenes.GameScene;
import org.bioshock.scenes.MainGame;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.Window;

public final class SceneManager {
    private static Stage stage;
    private static GameScene currentScene;

    private SceneManager() {}

    public static void initialize(Stage primaryStage, GameScene initialScene) {
		stage = primaryStage;
        currentScene = initialScene;
	}

	public static void setScene(GameScene scene) {
        currentScene = scene;

        EntityManager.unregisterAll();

        InputManager.changeScene();

        stage.setScene(currentScene);

        currentScene.renderEntities();

        if (currentScene instanceof MainGame) {
            WindowManager.setFullScreen(true);
            ((MainGame) currentScene).setStarted(true);
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
        if (currentScene instanceof MainGame) {
            return ((MainGame) currentScene).isStarted();
        } else {
            return false;
        }
	}
}
