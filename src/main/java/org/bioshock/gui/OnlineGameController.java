package org.bioshock.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.bioshock.main.App;
import org.bioshock.scenes.LoadingScreen;

import java.util.Objects;
import java.util.prefs.Preferences;

public class OnlineGameController {
    public Button backButton;
    public Button launchButton;
    public Label developmentLabel;
    public Label nameLabel;
    public TextField nameField;

    @FXML
    private void switchToMainView() {
        App.setFXMLRoot("main");
    }

    @FXML
    public void switchToNewGameView(ActionEvent actionEvent) {
        App.setFXMLRoot("new_game");
    }

    public void initialize() {
        launchButton.setText(App.getBundle().getString("LAUNCH_BUTTON_TEXT"));
        backButton.setText(App.getBundle().getString("BACK_NEW_GAME_BUTTON_TEXT"));
        developmentLabel.setText(App.getBundle().getString("ONLINE_BUTTON_TEXT") + " " + App.getBundle().getString("IN_DEVELOPMENT_TEXT"));
        nameLabel.setText(App.getBundle().getString("PLAYER_NAME_TEXT") + ":");
        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        nameField.setText(prefs.get("playerName", App.getBundle().getString("DEFAULT_PLAYER_NAME_TEXT")));

        Image backImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/arrow.png")));
        ImageView backImageView = new ImageView(backImage);
        backImageView.setPreserveRatio(true);
        backImageView.setFitWidth(17);
        backButton.setGraphic(backImageView);

        Image launchImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/rocket.png")));
        ImageView launchImageView = new ImageView(launchImage);
        launchImageView.setPreserveRatio(true);
        launchImageView.setFitWidth(18);
        launchButton.setGraphic(launchImageView);
    }

    public void launchGame(ActionEvent actionEvent) {
        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        String playerName = nameField.getText();
        if (playerName.length() > 0 && playerName.length() <= 16) {
            prefs.put("playerName", playerName);
            // TODO: Add code to handle player name here
            Stage stage = (Stage) launchButton.getScene().getWindow();
            App.startGame(stage, new LoadingScreen(true, App.getBundle().getString("ONLINE_LOADING_TEXT")), true);
        }
    }
}
