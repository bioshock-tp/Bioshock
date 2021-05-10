package org.bioshock.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bioshock.audio.AudioManager;
import org.bioshock.engine.core.GameLoop;
import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.input.InputManager;
import org.bioshock.gui.MainController;
import org.bioshock.networking.Account;
import org.bioshock.networking.NetworkManager;
import org.bioshock.rendering.RenderManager;
import org.bioshock.scenes.GameScene;
import org.bioshock.scenes.SceneManager;
import org.bioshock.utils.Difficulty;
import org.bioshock.utils.FontManager;
import org.bioshock.utils.LanguageManager;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;

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

    private static int playerCount = 1;

    /**
     * The current FXML scene displayed
     */
    private static Scene fxmlScene;

    /**
     * True if game is networked
     */
    private static boolean networked;

    /**
     * The resource bundle being used
     */
    private static ResourceBundle bundle;

    /**
     * The user's locale/language
     */
    private static Locale locale;

    /**
     * The FXMLLoader responsible for GUI
     */
    private static FXMLLoader fxmlLoader;

    /**
     * The difficulty of the game
     */
    private static Difficulty difficulty;


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
        new FontManager();

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
        GameScene initScene
    ) {
        try {
            SceneManager.initialise(primaryStage, initScene);
            InputManager.initialise();

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
     * <ul>
     *  <li>Blurs the game and slows entities</li>
     *  <li>Plays a sound</li>
     *  <li>Displays "You Win/Lose!"</li>
     * </ul>
     * After a key is pressed, the scoreboard will be displayed
     * @param victory True if game is won
     */
    public static void end(boolean victory) {
        RenderManager.endGame();
        String textToDisplay;
        //bug with lines 156 to 172 causing collected items displayed as 1 above the collected amount. Also prevents winning text from appearing
        //TODO fix this if statement
        if (victory && !NetworkManager.me().isDead()){
            try {
                URL url = new URL("http://recklessgame.net:8034/increaseScore");
                String jsonInputString = "{\"Token\":\"" + Account.getToken() + "\",\"Score\":\"" + Integer.toString(Account.getScoreToInc()) + "\"}";
                byte[] postDataBytes = jsonInputString.getBytes("UTF-8");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("PUT");
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setDoOutput(true);
                con.getOutputStream().write(postDataBytes);
                Reader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                Account.setScore(Account.getScoreToInc() + Account.getScore());
                Account.setScoreToInc(0);
            } catch (IOException e) {
                App.logger.error(e);
            }
        }
        if (victory) {
            AudioManager.playWinSfx();
            textToDisplay = App.getBundle().getString("WIN_TEXT");
        } else {
            AudioManager.playLoseSfx();
            textToDisplay = App.getBundle().getString("LOSE_TEXT");
        }

        Runnable anyKeyContinue = () -> {
            Label label = new Label("Press Any Key to Continue");
            label.setFont(new Font(50));
            label.setTranslateY(
                GameScene.getGameScreen().getHeight() / 2 - 100
            );

            SceneManager.getPane().getChildren().add(label);

            InputManager.stop();
            InputManager.onPress(KeyCode.ESCAPE, () -> App.exit(0));

            SceneManager.getScene().addEventHandler(
                KeyEvent.KEY_PRESSED,
                e -> {
                    RenderManager.displayText();
                    label.setVisible(false);
                    SceneManager.getMainGame().showScoreboard(true);
                }
            );
        };

        RenderManager.displayText(textToDisplay, anyKeyContinue);
    }


    /**
     * Switches current scene to FXML file specified.
     * @param fxml The FXML file you want to switch to.
     */
    public static void setFXMLRoot(String fxml) {
        fxmlScene.setRoot(loadFXML(fxml));
    }


    /**
     * Initialises the current scene with the main FXML file.
     */
    private static void initFXMLScene() {
        fxmlScene = new Scene(Objects.requireNonNull(loadFXML("main")));
    }


    /**
     * Loads the FXML file from resources.
     * @param fxml The name of the FXML file.
     * @return The FXML file specified.
     */
    private static Parent loadFXML(String fxml) {
        try {
            URL location = MainController.class.getResource(fxml + ".fxml");
            fxmlLoader = new FXMLLoader(location);
            return fxmlLoader.load();
        } catch (IOException e) {
            App.logger.error("Error loading FXML: {}", fxml, e);
            exit(-1);
            return null; /* Prevents no return value warning */
        }
    }


    /**
     * @param networked True if game is multiplayer
     */
    public static void setNetworked(boolean networked) {
        App.networked = networked;
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
     * @param bundle The resource bundle to set.
     */
    public static void setBundle(ResourceBundle bundle) {
        App.bundle = bundle;
    }


    /**
     * Sets the current locale/language.
     * @param locale The locale to set.
     */
    public static void setLocale(Locale locale) {
        App.locale = locale;
    }


    /**
     * Gets the current resource bundle.
     * @return The current resource bundle.
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
     * @return The current controller used for the game's GUI
     */
    public static GameScene getFXMLController() {
        return fxmlLoader.getController();
    }


    /**
     * Closes the game
     *
     * @return The difficulty of the game
     */
    public static Difficulty getDifficulty() {
        return difficulty;
    }


    /**
     * Sets the difficulty of the game
     * @param difficulty The difficulty
     */
    public static void setDifficulty(Difficulty difficulty) {
        App.difficulty = difficulty;
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
     * Calls javafx.launch()<p />
     * Must be called from an external class
     * @param args As defined by {@link #launch(String...)}
     */
    public static void main(String[] args) {
        launch(args);
    }
}
