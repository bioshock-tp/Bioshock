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

        currentScene.initScene();
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
	 * @return Lobby if {@link #currentScene} is Lobby, otherwise null
	 */
    public static Lobby getLobby() {
        if (currentScene instanceof Lobby) {
            return (Lobby) currentScene;
        } else {
            App.logger.error("Tried to get Lobby whilst not in Lobby");
            return null;
        }
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
