package org.bioshock.gui;

import org.bioshock.main.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

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
    }

    @FXML
    public void openSettingsView(ActionEvent actionEvent) {
        App.setFXMLRoot("settings");
    }
}
