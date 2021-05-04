package org.bioshock.scenes;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.input.InputManager;
import org.bioshock.entities.map.maps.Map;

public final class SceneManager {
    private static Stage stage;
    private static GameScene currentScene;
    private static boolean inLobby = false;
    private static boolean inGame = false;
    private static boolean initialised = false;
    private static Map gameMap;
    private static long seed;
    private static MainGame mainGame;

    private SceneManager() {}

    public static void initialise(Stage primaryStage, GameScene initialScene) {
        if (initialised) return;
        initialised = true;

		stage = primaryStage;
        setScene(initialScene);
	}

	public static void setScene(GameScene scene) {
        if (currentScene != null) currentScene.destroy();
        currentScene = scene;

        WindowManager.setFullScreen(true);

        InputManager.changeScene();

        stage.setScene(currentScene);

        currentScene.initScene(seed);
	}

    public static void setInLobby(boolean b) {
        inLobby = b;
    }

    public static void setInGame(boolean inGame) {
        SceneManager.inGame = inGame;
    }

    public static void setMap(Map map) {
        gameMap = map;
    }

    public static void setSeed(long newSeed) {
        seed = newSeed;
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

	public static boolean inLobby() {
        return inLobby;
	}

    public static boolean inGame() {
        return inGame;
    }

    public static Map getMap() {
        return gameMap;
    }

    public static void setMainGame(MainGame mainGame) {
        SceneManager.mainGame = mainGame;
    }

    public static MainGame getMainGame() {
        return mainGame;
    }
}
