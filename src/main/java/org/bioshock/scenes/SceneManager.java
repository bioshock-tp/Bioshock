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
     * Set the displayed scene to the given GameScene
     * @param scene
     */
	public static void setScene(GameScene scene) {
        if (currentScene != null) currentScene.destroy();
        currentScene = scene;

        WindowManager.setFullScreen(true);

        InputManager.changeScene();

        stage.setScene(currentScene);

        currentScene.initScene(seed);
	}
	
	/**
	 * Set in lobby to the given boolean
	 * @param b
	 */
    public static void setInLobby(boolean b) {
        inLobby = b;
    }

    /**
     * Set in game to the given boolean
     * @param inGame
     */
    public static void setInGame(boolean inGame) {
        SceneManager.inGame = inGame;
    }
    
    /**
     * Set the game map to the given gameMap
     * @param map
     */
    public static void setMap(Map map) {
        gameMap = map;
    }

    /**
     * Set the seed to the given seed
     * @param newSeed
     */
    public static void setSeed(long newSeed) {
        seed = newSeed;
    }

    /**
     * Getter 
     * @return the current gameScene
     */
	public static GameScene getScene() {
		return currentScene;
	}

	/**
	 * Getter 
	 * @return the current stack pane
	 */
    public static StackPane getPane() {
		return currentScene.getPane();
	}

    /**
     * Getter
     * @return  the current canvas
     */
	public static Canvas getCanvas() {
		return currentScene.getCanvas();
	}

	/**
	 * Getter
	 * @return the currentScene and cast it to a Lobby and 
	 * throw an error if it isn't an instance of Lobby
	 */
    public static Lobby getLobby() {
        if (!(currentScene instanceof Lobby)) {
            App.logger.error("Tried to access lobby when not in one");
            return null;
        } else {
            return (Lobby) currentScene;
        }
    }

    /**
     * 
     * @return inLobby
     */
	public static boolean inLobby() {
        return inLobby;
	}

	/**
	 * 
	 * @return inGame
	 */
    public static boolean inGame() {
        return inGame;
    }

    /**
     * 
     * @return The current game map
     */
    public static Map getMap() {
        return gameMap;
    }

}
