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
 * The class that starts the game
 */
public class App extends Application {

    /**
     * The logger used across the entire game
     */
    public static final Logger logger = LogManager.getLogger(App.class);

    /**
     * The name of the game
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
     * True if game is networked
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
     * Initialises more of the game's core managers and starts gameplay
     * @param primaryStage The main stage in the window
     * @param initScene The initial {@link GameScene} of the game
     * @param isNetworked True if game is networked
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
     * @param playerCount The new player count
     */
    public static void setPlayerCount(int playerCount) {
        App.playerCount = playerCount;
    }


    /**
     * @return The current player count
     */
    public static int playerCount() {
        return playerCount;
    }


    /**
     * @return True if game is networked
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
     * @return The name of the game
     */
    public static String getName() {
        return name;
    }


    /**
     * @param name The new name of the game
     */
    public static void setName(String name) {
        App.name = name;
    }


    /**
     * Closes the game
     * @param code The exit code
     */
    public static void exit(int code) {
        Platform.exit();
        System.exit(code);
    }


    /**
     * Calls javafx.launch()<p />
     * Must be called from an external class
     * @param args As defined by {@link #launch(String...)}
     */
    public static void main(String[] args) {
        launch(args);
    }
}
