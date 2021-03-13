package org.bioshock.engine.scene;

import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.entity.EntityManager;
import org.bioshock.engine.input.InputManager;
import org.bioshock.scenes.GameScene;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public final class SceneManager {
    private static Stage stage;
    private static GameScene currentScene;
    private static boolean isGameStarted = false;
    private static boolean initialised = false;

    private SceneManager() {}

    public static void initialise(Stage primaryStage, GameScene initialScene) {
        if (initialised) return;
        initialised = true;

		stage = primaryStage;
        setScene(initialScene);
	}

	public static void setScene(GameScene scene) {
        currentScene = scene;

        WindowManager.setFullScreen(true);

        EntityManager.unregisterAll();

        InputManager.changeScene();

        stage.setScene(currentScene);

        currentScene.renderEntities();
        currentScene.initScene();
	}

	public static GameScene getScene() {
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

	public static void setGameStarted(boolean isGameStarted) {
		SceneManager.isGameStarted = isGameStarted;
	}
}
