package org.bioshock.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.bioshock.main.App;

import java.io.IOException;

public class MainController {

    public Button newGameButton;
    public Button quitButton;
    public Button settingsButton;

    @FXML
    private void openNewGameView() throws IOException {
        App.setRoot("new_game");
    }

    @FXML
    public void handleQuitButton(ActionEvent actionEvent) {
        Stage stage = (Stage) quitButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void openSettingsView(ActionEvent actionEvent) throws IOException {
        App.setRoot("settings");
    }
}
