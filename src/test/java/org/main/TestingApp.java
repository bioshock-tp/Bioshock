package org.main;

import java.util.concurrent.CountDownLatch;

import org.bioshock.engine.core.GameLoop;
import org.bioshock.engine.core.WindowManager;
import org.bioshock.entities.EntityManager;
import org.bioshock.entities.map.RoomEntity;
import org.bioshock.entities.map.maps.GenericMap;
import org.bioshock.scenes.LoadingScreen;
import org.bioshock.scenes.SceneManager;
import org.bioshock.utils.GlobalConstants;
import org.bioshock.utils.LanguageManager;
import org.bioshock.utils.Size;

import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TestingApp extends Application {
    /**
     * Latch used to wait until JavaFX initialisation is complete
     */
    private static CountDownLatch latch = new CountDownLatch(1);

    /**
     * The thread used to run the {@link Application}
     */
    private static Thread javaFXThread;
    /**
     * The game loop this testing app calls
     */
    GameLoop gameLoop;
    /**
     * Stores if the gameLoop is running or not
     */
    boolean loopRunning = false;


    @Override
    public void start(Stage primaryStage) throws Exception {
        WindowManager.initialise(primaryStage);

        LanguageManager.initialiseLanguageSettings();

        LoadingScreen loadingScene = new LoadingScreen("TESTING", () -> {});

        SceneManager.initialise(primaryStage, loadingScene);

        SceneManager.setMap(
            new GenericMap(
                new Point3D(0, 0, 0),
                1,
                new Size(9, 11),
                new Size(1, 1),
                Color.RED,
                GlobalConstants.SIMPLE_MAP,
                0
            )
        );


        SceneManager.getMap().getRooms().forEach(room -> {
            RoomEntity roomEntity = new RoomEntity(room);
            EntityManager.register(roomEntity);
        });

        gameLoop = new GameLoop();
        playGameLoop();
        
       
        latch.countDown();
    }
    
    /**
     * Method that starts the game loop if it isn't already started
     */
    public void playGameLoop() {
        if(!loopRunning) {
            gameLoop.start();
            loopRunning = true;
        }
    }
    
    /**
     * Method that stops the game loop
     */
    public void stopGameLoop() {
        if(loopRunning) {
            gameLoop.stop();
            loopRunning = false;
        }
    }

    /**
     * Launches the JavaFX thread
     */
    public static void launchJavaFXThread() {
        if (javaFXThread != null) return;

        javaFXThread = new Thread(() -> Application.launch(TestingApp.class));
        javaFXThread.start();

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
