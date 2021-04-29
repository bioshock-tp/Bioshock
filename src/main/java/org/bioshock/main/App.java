package org.bioshock.main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bioshock.audio.AudioManager;
import org.bioshock.engine.core.GameLoop;
import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.input.InputManager;
import org.bioshock.gui.MainController;
import org.bioshock.rendering.RenderManager;
import org.bioshock.scenes.GameScene;
import org.bioshock.scenes.SceneManager;
import org.bioshock.utils.FontManager;
import org.bioshock.utils.LanguageManager;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * 
 * The class that starts the game
 *
 */
public class App extends Application {
    /**
     * The logger used across the entire game
     */
    public static final Logger logger = LogManager.getLogger(App.class);
    /**
     * The name of the App
     */
    private static String name;
    /**
     * The number of players for online play
     */
    private static int playerCount = 2;
    /**
     * TODO
     */
    private static Scene fxmlScene;
    /**
     * Boolean representing whether the game is networked or not
     */
    private static boolean networked;
    /**
     * TODO
     */
    private static ResourceBundle bundle;
    /**
     * TODO
     */
    private static Locale locale;

    @Override
    public void start(Stage stage) {
        Thread.setDefaultUncaughtExceptionHandler((thread, exception) ->
            App.logger.error(
                "{}\n{}",
                exception,
                Arrays.toString(exception.getStackTrace()).replace(',', '\n')
            )
        );
        assert(playerCount > 0);

        AudioManager.initialiseBackgroundAudio();
        LanguageManager.initialiseLanguageSettings();
        FontManager.loadFonts();

        WindowManager.initialise(stage);
        initFXMLScene();

        stage.setScene(fxmlScene);
        stage.show();
    }
    
    /**
     * Start the actual game
     * @param primaryStage The main stage in the window
     * @param initScene The initial gameScene for loading the game
     * @param isNetworked Boolean to represent whether the game is networked or not
     */
    public static void startGame(
        Stage primaryStage,
        GameScene initScene,
        boolean isNetworked
    ) {
        try {
            networked = isNetworked;

            SceneManager.initialise(primaryStage, initScene);
            InputManager.initialise();

            if (!networked) {
                App.setPlayerCount(1);
            }

            new GameLoop().start();
        } catch (Exception e) {
            App.logger.error(
                "{}\n{}",
                e,
                Arrays.toString(e.getStackTrace()).replace(',', '\n')
            ); /* Necessary as GUI invocation overwrites exceptions */
        }
    }

    /**
     * TODO
     */
    public static void win() {
        RenderManager.endGame();
        AudioManager.playWinSfx();
        RenderManager.displayText("You Win!");
    }

    /**
     * TODO
     */
    public static void lose() {
        RenderManager.endGame();
        AudioManager.playLoseSfx();
        RenderManager.displayText("You Lose!");
    }

    /**
     * TODO
     * @param fxml
     */
    public static void setFXMLRoot(String fxml) {
        fxmlScene.setRoot(loadFXML(fxml));
    }

    /**
     * TODO
     */
    private static void initFXMLScene() {
        fxmlScene = new Scene(Objects.requireNonNull(loadFXML("main")));
    }

    /**
     * TODO
     * @param fxml
     * @return
     */
    private static Parent loadFXML(String fxml) {
        try {
            URL location = MainController.class.getResource(fxml + ".fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(location);
            return fxmlLoader.load();
        } catch (IOException e) {
            App.logger.error("Error loading FXML: {}", fxml, e);
            exit(-1);
            return null; /* Prevents no return value warning */
        }
    }
    
    /**
     * Set the player count
     * @param playerCount The new player count
     */
    public static void setPlayerCount(int playerCount) {
        App.playerCount = playerCount;
    }

    /**
     * 
     * @return The current player count 
     */
    public static int playerCount() {
        return playerCount;
    }

    /**
     * 
     * @return If the game is networked or not
     */
    public static boolean isNetworked() {
        return networked;
    }

    /**
     * TODO
     * @param bundle
     */
    public static void setBundle(ResourceBundle bundle) {
        App.bundle = bundle;
    }

    /**
     * TODO
     * @param locale
     */
    public static void setLocale(Locale locale) {
        App.locale = locale;
    }

    /**
     * TODO
     * @return
     */
    public static ResourceBundle getBundle() {
        return bundle;
    }

    /**
     * 
     * @return The name of the Application
     */
    public static String getName() {
        return name;
    }

    /**
     * Set the name of the Application
     * @param name The new name
     */
    public static void setName(String name) {
        App.name = name;
    }

    /**
     * 
     * @param code The exit code
     */
    public static void exit(int code) {
        Platform.exit();
        System.exit(code);
    }

    /**
     * The main function
     * Can't be directly called else Java throws an exception
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }
}
