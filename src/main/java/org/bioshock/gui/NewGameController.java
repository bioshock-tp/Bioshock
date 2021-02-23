package org.bioshock.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.bioshock.main.App;

import java.io.IOException;

public class NewGameController {
    public Button backButton;
    public Button localGameButton;
    public Button onlineGameButton;

    @FXML
    private void switchToMainView() throws IOException {
        App.setRoot("main");
    }

    @FXML
    public void switchToLocalGameView(ActionEvent actionEvent) throws IOException {
        App.setRoot("local_game");
    }

    @FXML
    public void switchToOnlineGameView(ActionEvent actionEvent) throws IOException {
        App.setRoot("online_game");
    }
}
