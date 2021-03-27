package org.bioshock.scenes;

import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.input.InputManager;
import org.bioshock.entities.map.Map;
import org.bioshock.main.App;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public final class SceneManager {
    private static Stage stage;
    private static GameScene currentScene;
    private static boolean inLobby = false;
    private static boolean inGame = false;
    private static boolean initialised = false;
    private static Map gameMap;

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

        currentScene.initScene();
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

	public static GameScene getScene() {
		return currentScene;
	}

    public static StackPane getPane() {
		return currentScene.getPane();
	}

	public static Canvas getCanvas() {
		return currentScene.getCanvas();
	}

    public static Lobby getLobby() {
        if (!(currentScene instanceof Lobby)) {
            App.logger.error("Tried to access lobby when not in one");
            return null;
        } else {
            return (Lobby) currentScene;
        }
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
}
