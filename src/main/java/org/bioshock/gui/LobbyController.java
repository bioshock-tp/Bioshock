package org.bioshock.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.bioshock.main.App;
import org.bioshock.networking.NetworkManager;
import org.bioshock.scenes.GameScene;
import org.bioshock.scenes.MainGame;

import java.util.Objects;
import java.util.prefs.Preferences;

public class LobbyController extends GameScene {
    public Label playerCountLabel;
    public Button backButton;
    public Label lobbyLabel;
    private MainGame mainGame;

//    public LobbyController() {
//
//        initialiseScene();
//
//        //Lobby gameLobby = new Lobby();
//
//        playerCountLabel.setText("0/" + App.playerCount());
//
//        mainGame = new MainGame();
//
////        App.setFXMLRoot("lobby");
//    }

    @FXML
    public void switchToNewGameView(ActionEvent actionEvent) {
        App.setFXMLRoot("online_game");
    }

    /**
     * Initialise the online game menu.
     */
    public void initialize() {
        initialiseScene();

        //Lobby gameLobby = new Lobby();

        playerCountLabel.setText("0/" + App.playerCount());

//        mainGame = new MainGame();

//        Stage stage = (Stage) backButton.getScene().getWindow();
        //App.startGame(stage, new Lobby(), true);

//        SceneManager.setInLobby(true);

        NetworkManager.initialise(this);
    }

//    @Override
//    public void initScene(long seed) {
//
//    }

    public void updatePlayerCount() {
        App.logger.debug("updating player count");
        playerCountLabel.setText(String.format(
            "%d/%d",
            NetworkManager.playerCount(), App.playerCount()
        ));

        if (NetworkManager.playerCount() == App.playerCount()) {
            Stage stage = (Stage) lobbyLabel.getScene().getWindow();
            App.startGame(stage, App.getMainGame(), true);
        }
    }

    private void initialiseScene() {
        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        String playerName = prefs.get("playerName", App.getBundle().getString("DEFAULT_PLAYER_NAME_TEXT"));
        backButton.setText(App.getBundle().getString("BACK_NEW_GAME_BUTTON_TEXT"));
        lobbyLabel.setText(playerName + " " + App.getBundle().getString("ONLINE_BUTTON_TEXT") + " " + App.getBundle().getString("IN_DEVELOPMENT_TEXT"));

        Image backImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/arrow.png")));
        ImageView backImageView = new ImageView(backImage);
        backImageView.setPreserveRatio(true);
        backImageView.setFitWidth(17);
        backButton.setGraphic(backImageView);
    }

}
