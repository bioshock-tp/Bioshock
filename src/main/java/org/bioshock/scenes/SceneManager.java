package org.bioshock.scenes;

import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.input.InputManager;
import org.bioshock.entities.map.maps.Map;
import org.bioshock.main.App;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public final class SceneManager {
    /**
     * The current stage
     */
    private static Stage stage;

    /**
     * The current GameScene
     */
    private static GameScene currentScene;

    /**
     * Boolean representing if you are waiting in a lobby or not
     */
    private static boolean inLobby = false;

    /**
     * Boolean representing if you are in the game or not
     */
    private static boolean inGame = false;

    /**
     * Boolean to track if the game has been initialised or not
     */
    private static boolean initialised = false;

    /**
     * The current game map
     */
    private static Map gameMap;

    /**
     * The seed used to initialise the game scene
     */
    private static long seed;
    private static MainGame mainGame;


    /**
     * SceneManager is a static class
     */
    private SceneManager() {}


    /**
     * Initialise the SceneManager
     * @param primaryStage
     * @param initialScene
     */
    public static void initialise(Stage primaryStage, GameScene initialScene) {
        if (initialised) return;
        initialised = true;

		stage = primaryStage;
        setScene(initialScene);
	}


    /**
     * @param scene The new scene to be used in the game
     */
	public static void setScene(GameScene scene) {
        if (currentScene != null) currentScene.destroy();
        currentScene = scene;

        WindowManager.setFullScreen(true);

        InputManager.changeScene();

        stage.setScene(currentScene);

        currentScene.initScene();
	}


	/**
	 * @param inLobby True if in lobby
	 */
    public static void setInLobby(boolean inLobby) {
        SceneManager.inLobby = inLobby;
    }


    /**
     * @param inGame True if in game
     */
    public static void setInGame(boolean inGame) {
        SceneManager.inGame = inGame;
    }


    /**
     * @param map The new map of the game
     */
    public static void setMap(Map map) {
        gameMap = map;
    }


    /**
     * @param newSeed The new seed used for map generation
     */
    public static void setSeed(long newSeed) {
        seed = newSeed;
    }


    /**
     * @param mainGame The {@link MainGame} scene instance to be used game-wide
     */
    public static void setMainGameInstance(MainGame mainGame) {
        SceneManager.mainGame = mainGame;
    }


    /**
     * @return The seed to be used for map generation
     */
    public static long getSeed() {
        return seed;
    }


    /**
     * @return the current gameScene
     */
	public static GameScene getScene() {
		return currentScene;
	}


	/**
	 * @return the current stack pane
	 */
    public static StackPane getPane() {
		return currentScene.getPane();
	}


    /**
     * @return  the current canvas
     */
	public static Canvas getCanvas() {
		return currentScene.getCanvas();
	}


    /**
     * @return MainGame if {@link #currentScene} is MainGame, otherwise null
     */
    public static MainGame getMainGame() {
        if (currentScene instanceof MainGame) {
            return (MainGame) currentScene;
        }
        else {
            App.logger.error("Tried to get MainGame whilst not in MainGame");
            return null;
        }
    }


    /**
     * @return The {@link MainGame} scene instance to be used game-wide
     */
    public static MainGame getMainGameInstance() {
        return mainGame;
    }


    /**
     * @return inLobby
     */
	public static boolean inLobby() {
        return inLobby;
	}


	/**
	 * @return inGame
	 */
    public static boolean inGame() {
        return inGame;
    }


    /**
     * @return The current game map
     */
    public static Map getMap() {
        return gameMap;
    }
}
