package org.bioshock.gui;

import javafx.scene.control.Label;
import org.bioshock.main.App;
import org.bioshock.scenes.LoadingScreen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.bioshock.scenes.DisplayScreen;
import org.bioshock.utils.GlobalStrings;

public class LocalGameController {
    public Button backButton;
    public Button launchButton;
    public Label developmentLabel;

    @FXML
    private void switchToMainView() {
        App.setFXMLRoot("main");
    }

    @FXML
    public void switchToNewGameView(ActionEvent actionEvent) {
        App.setFXMLRoot("new_game");
    }

    public void initialize() {
        launchButton.setText(GlobalStrings.LAUNCH_BUTTON_TEXT);
        backButton.setText(GlobalStrings.BACK_NEW_GAME_BUTTON_TEXT);
        developmentLabel.setText(GlobalStrings.SINGLE_PLAYER_BUTTON_TEXT + " " + GlobalStrings.IN_DEVELOPMENT_TEXT);
    }

    public void launchGame(ActionEvent actionEvent) {
        Stage stage = (Stage) launchButton.getScene().getWindow();
        App.startGame(stage, new LoadingScreen(false, GlobalStrings.SINGLE_PLAYER_LOADING_TEXT, DisplayScreen.LOADING), false);
    }
}
