package org.bioshock.gui;

import java.util.Objects;
import java.util.prefs.Preferences;

import org.bioshock.main.App;
import org.bioshock.networking.NetworkManager;
import org.bioshock.scenes.GameScene;
import org.bioshock.scenes.LoadingScreen;
import org.bioshock.scenes.MainGame;
import org.bioshock.scenes.SceneManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class LobbyController extends GameScene {
    public Label playerCountLabel;
    public Button backButton;
    public Label lobbyLabel;

    @FXML
    public void switchToNewGameView(ActionEvent actionEvent) {
        App.setFXMLRoot("online_game");
        NetworkManager.reset();
    }

    /**
     * Initialise the online game menu.
     */
    public void initialize() {
        initialiseScene();
        playerCountLabel.setText("0/" + App.playerCount());

        SceneManager.setMainGame(new MainGame());

        NetworkManager.initialise(this);
    }


    public void updatePlayerCount(int playerCount) {
        playerCountLabel.setText(String.format(
            "%d/%d",
            playerCount, App.playerCount()
        ));

        if (playerCount == App.playerCount()) {
            Stage stage = (Stage) lobbyLabel.getScene().getWindow();
            App.startGame(
                stage,
                new LoadingScreen(
                    App.getBundle().getString("ONLINE_LOADING_TEXT")
                ),
                true
            );
        }
    }

    private void initialiseScene() {
        Preferences prefs = Preferences.userNodeForPackage(
            SettingsController.class
        );
        String playerName = prefs.get(
            "playerName",
            App.getBundle().getString("DEFAULT_PLAYER_NAME_TEXT")
        );
        backButton.setText(
            App.getBundle().getString(
                "BACK_NEW_GAME_BUTTON_TEXT"
            )
        );
        lobbyLabel.setText(
            playerName + " "
            + App.getBundle().getString("ONLINE_BUTTON_TEXT") + " "
            + App.getBundle().getString("IN_DEVELOPMENT_TEXT")
        );

        Image backImage = new Image(
            Objects.requireNonNull(
                getClass().getResourceAsStream("icons/arrow.png")
            )
        );

        ImageView backImageView = new ImageView(backImage);
        backImageView.setPreserveRatio(true);
        backImageView.setFitWidth(17);
        backButton.setGraphic(backImageView);
    }

}
