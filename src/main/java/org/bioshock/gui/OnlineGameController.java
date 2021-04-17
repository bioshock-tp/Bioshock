package org.bioshock.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bioshock.main.App;
import org.bioshock.scenes.LoadingScreen;

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
    }

    public void launchGame(ActionEvent actionEvent) {
        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        String playerName = nameField.getText();
        prefs.put("playerName", playerName);
        // TODO: Add code to handle player name here
        Stage stage = (Stage) launchButton.getScene().getWindow();
        App.startGame(stage, new LoadingScreen(true, App.getBundle().getString("ONLINE_LOADING_TEXT")), true);
    }
}
