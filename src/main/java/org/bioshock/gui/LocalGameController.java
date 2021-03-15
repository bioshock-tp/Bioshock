package org.bioshock.gui;

import org.bioshock.main.App;
import org.bioshock.scenes.LoadingScreen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LocalGameController {
    public Button backButton;
    public Button launchButton;

    @FXML
    private void switchToMainView() {
        App.setFXMLRoot("main");
    }

    @FXML
    public void switchToNewGameView(ActionEvent actionEvent) {
        App.setFXMLRoot("new_game");
    }

    public void launchGame(ActionEvent actionEvent) {
        App.setPlayerCount(1);
        Stage stage = (Stage) launchButton.getScene().getWindow();
        App.startGame(stage, new LoadingScreen(false), false);
    }
}
