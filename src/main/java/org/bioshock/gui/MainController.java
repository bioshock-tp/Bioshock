package org.bioshock.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.bioshock.main.App;

public class MainController {
    public Button newGameButton;
    public Button quitButton;
    public Button settingsButton;

    @FXML
    private void openNewGameView() {
        App.setFXMLRoot("new_game");
    }

    @FXML
    public void handleQuitButton(ActionEvent actionEvent) {
        Stage stage = (Stage) quitButton.getScene().getWindow();
        stage.close();
        App.exit(0);
    }

    @FXML
    public void openSettingsView(ActionEvent actionEvent) {
        App.setFXMLRoot("settings");
    }

    public void initialize() {
        newGameButton.setText(App.getBundle().getString("NEW_GAME_BUTTON_TEXT"));
        quitButton.setText(App.getBundle().getString("QUIT_GAME_BUTTON_TEXT"));
        settingsButton.setText(App.getBundle().getString("SETTINGS_BUTTON_TEXT"));
    }
}
