package org.bioshock.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.bioshock.main.App;
import org.bioshock.utils.GlobalStrings;

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

    public void initialize() {
        localGameButton.setText(GlobalStrings.SINGLE_PLAYER_BUTTON_TEXT);
        onlineGameButton.setText(GlobalStrings.ONLINE_BUTTON_TEXT);
        backButton.setText(GlobalStrings.BACK_MAIN_MENU_BUTTON_TEXT);
    }
}
