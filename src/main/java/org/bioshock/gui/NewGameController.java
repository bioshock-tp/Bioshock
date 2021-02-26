package org.bioshock.gui;

import org.bioshock.main.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class NewGameController {
    public Button backButton;
    public Button localGameButton;
    public Button onlineGameButton;

    @FXML
    private void switchToMainView() {
        App.setFXMLRoot("main");
    }

    @FXML
    public void switchToLocalGameView(ActionEvent actionEvent) {
        App.setFXMLRoot("local_game");
    }

    @FXML
    public void switchToOnlineGameView(ActionEvent actionEvent) {
        App.setFXMLRoot("online_game");
    }
}
