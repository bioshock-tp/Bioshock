package org.bioshock.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.bioshock.main.App;
import org.bioshock.scenes.LoadingScreen;

import java.util.Objects;

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

    /**
     * Initialise the local game menu.
     */
    public void initialize() {
        launchButton.setText(App.getBundle().getString("LAUNCH_BUTTON_TEXT"));
        backButton.setText(App.getBundle().getString("BACK_NEW_GAME_BUTTON_TEXT"));
        developmentLabel.setText(App.getBundle().getString("SINGLE_PLAYER_BUTTON_TEXT") + " " + App.getBundle().getString("IN_DEVELOPMENT_TEXT"));

        Image backImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("arrow.png")));
        ImageView backImageView = new ImageView(backImage);
        backImageView.setPreserveRatio(true);
        backImageView.setFitWidth(17);
        backButton.setGraphic(backImageView);

        Image launchImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("rocket.png")));
        ImageView launchImageView = new ImageView(launchImage);
        launchImageView.setPreserveRatio(true);
        launchImageView.setFitWidth(18);
        launchButton.setGraphic(launchImageView);
    }

    public void launchGame(ActionEvent actionEvent) {
        Stage stage = (Stage) launchButton.getScene().getWindow();
        App.startGame(stage, new LoadingScreen(false, App.getBundle().getString("SINGLE_PLAYER_LOADING_TEXT")), false);
    }
}
