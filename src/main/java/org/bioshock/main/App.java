package org.bioshock.main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bioshock.audio.AudioManager;
import org.bioshock.engine.core.GameLoop;
import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.input.InputManager;
import org.bioshock.gui.MainController;
import org.bioshock.scenes.GameScene;
import org.bioshock.scenes.SceneManager;
import org.bioshock.utils.GlobalStrings;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class App extends Application {
    public static final String NAME = GlobalStrings.BUZZ_TEXT + GlobalStrings.KILL_TEXT;
    public static final Logger logger = LogManager.getLogger(App.class);
    private static int playerCount = 2;
    private static Scene fxmlScene;
    private static boolean networked;
    protected static ResourceBundle bundle;
    protected static Locale locale;

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

        WindowManager.initialise(stage);
        initFXMLScene();

        AudioManager.initialiseBackgroundAudio();

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
            InputManager.onPress(KeyCode.C, () ->
                App.logger.debug(SceneManager.getScene())
            );

            if (!networked) {
                App.setPlayerCount(1);

                InputManager.onPress(KeyCode.R, () -> {
                    App.logger.debug("Resetting Scene...");
                    try {
                        GameScene scene = SceneManager.getScene();
                        scene.destroy();
                        Class<? extends GameScene> sceCl = scene.getClass();
                        GameScene nSce = sceCl.getDeclaredConstructor().newInstance();
                        SceneManager.setScene(nSce);
                    } catch (Exception e) {
                        App.logger.error("Error whilst changing scene: ", e);
                    }
                });
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

    protected static void loadLang(String lang) {
        locale = new Locale(lang);
        bundle = ResourceBundle.getBundle("org.bioshock.utils.lang", locale);
    }

    public static void exit(int code) {
        Platform.exit();
        System.exit(code);
    }

    public static void main(String[] args) {
        launch();
    }
}
