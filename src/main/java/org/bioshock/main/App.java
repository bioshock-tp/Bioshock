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

public class App extends Application {
    public static final Logger logger = LogManager.getLogger(App.class);

    private static String name;
    private static int playerCount = 2;
    private static Scene fxmlScene;
    private static boolean networked;
    private static ResourceBundle bundle;
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

    public static void win() {
        RenderManager.endGame();
        AudioManager.playWinSfx();
        RenderManager.displayText("You Win!");
    }

    public static void lose() {
        RenderManager.endGame();
        AudioManager.playLoseSfx();
        RenderManager.displayText("You Lose!");
    }

    public static void setFXMLRoot(String fxml) {
        fxmlScene.setRoot(loadFXML(fxml));
    }

    private static void initFXMLScene() {
        fxmlScene = new Scene(Objects.requireNonNull(loadFXML("main")));
    }

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

    public static void setPlayerCount(int playerCount) {
        App.playerCount = playerCount;
    }

    public static int playerCount() {
        return playerCount;
    }

    public static boolean isNetworked() {
        return networked;
    }

    public static void setBundle(ResourceBundle bundle) {
        App.bundle = bundle;
    }

    public static void setLocale(Locale locale) {
        App.locale = locale;
    }

    public static ResourceBundle getBundle() {
        return bundle;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        App.name = name;
    }

    public static void exit(int code) {
        Platform.exit();
        System.exit(code);
    }

    public static void main(String[] args) {
        launch();
    }
}
