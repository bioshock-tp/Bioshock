package org.bioshock.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.bioshock.main.App;
import org.bioshock.scenes.LoadingScreen;

public class LocalGameController {
    @FXML
    public Button backButton;
    @FXML
    public Button launchButton;
    @FXML
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
        launchButton.setText(App.getBundle().getString("LAUNCH_BUTTON_TEXT"));
        backButton.setText(App.getBundle().getString("BACK_NEW_GAME_BUTTON_TEXT"));
        developmentLabel.setText(App.getBundle().getString("SINGLE_PLAYER_BUTTON_TEXT") + " " + App.getBundle().getString("IN_DEVELOPMENT_TEXT"));
    }

    public void launchGame(ActionEvent actionEvent) {
        Stage stage = (Stage) launchButton.getScene().getWindow();
        App.startGame(stage, new LoadingScreen(false, App.getBundle().getString("SINGLE_PLAYER_LOADING_TEXT")), false);
    }
}
