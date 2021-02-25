package org.bioshock.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.bioshock.main.App;

public class OnlineGameController {

    public Button backButton;

    @FXML
    private void switchToMainView() {
        App.setFXMLRoot("main");
    }

    @FXML
    public void switchToNewGameView(ActionEvent actionEvent) {
        App.setFXMLRoot("new_game");
    }
}
