package org.bioshock.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.bioshock.main.App;

import java.io.IOException;

public class LocalGameController {
    public Button backButton;
    public Button launchButton;

    @FXML
    private void switchToMainView() throws IOException {
        App.setRoot("main");
    }

    @FXML
    public void switchToNewGameView(ActionEvent actionEvent) throws IOException {
        App.setRoot("new_game");
    }

    public void launchGame(ActionEvent actionEvent) throws Exception {
        Stage stage = (Stage) launchButton.getScene().getWindow();
        App.startGame(stage);
    }
}
