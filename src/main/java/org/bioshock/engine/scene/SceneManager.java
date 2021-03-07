package org.bioshock.engine.scene;

import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.entity.EntityManager;
import org.bioshock.engine.entity.Hider;
import org.bioshock.engine.input.InputManager;
import org.bioshock.engine.networking.NetworkManager;
import org.bioshock.main.App;
import org.bioshock.scenes.GameScene;
import org.bioshock.scenes.MainGame;

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
        App.logger.debug("1");
        EntityManager.unregisterAll();
        App.logger.debug("1");
        InputManager.changeScene();
        App.logger.debug("1");
        stage.setScene(currentScene);

        currentScene.renderEntities();

        if (currentScene instanceof MainGame) {
            isGameStarted = true;

            if (App.isNetworked()) {
                Object mutex = NetworkManager.getMutex();
                synchronized(mutex) {
                    mutex.notifyAll();
                }
                App.logger.debug("Notified networking thread");
            } else {
                assert(App.PLAYERCOUNT == 1);
                Hider hider = EntityManager.getPlayers().get(0);
                hider.initMovement();
            }
        }
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
}
