package org.bioshock.gui;

import java.util.Objects;

import org.bioshock.main.App;
import org.bioshock.scenes.LoadingScreen;
import org.bioshock.scenes.MainGame;
import org.bioshock.scenes.SceneManager;
import org.bioshock.utils.Difficulty;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class LocalGameController {
    @FXML
    public Button backButton;
    @FXML
    public Button launchButton;
    @FXML
    public Label developmentLabel;
    @FXML
    public Label difficultyLabel;
    @FXML
    public ComboBox<Difficulty> difficultyComboBox;

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
    @FXML
    public void initialize() {
        launchButton.setText(App.getBundle().getString("LAUNCH_BUTTON_TEXT"));
        backButton.setText(App.getBundle().getString("BACK_NEW_GAME_BUTTON_TEXT"));
        developmentLabel.setText(App.getBundle().getString("SINGLE_PLAYER_LOADING_TEXT"));

        difficultyLabel.setText(App.getBundle().getString("DIFFICULTY_TEXT") + ": ");
        difficultyComboBox.getItems().setAll(Difficulty.values());
        difficultyComboBox.getSelectionModel().selectFirst();

        Image backImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/arrow.png")));
        ImageView backImageView = new ImageView(backImage);
        backImageView.setPreserveRatio(true);
        backImageView.setFitWidth(17);
        backButton.setGraphic(backImageView);

        Image launchImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/rocket.png")));
        ImageView launchImageView = new ImageView(launchImage);
        launchImageView.setPreserveRatio(true);
        launchImageView.setFitWidth(18);
        launchButton.setGraphic(launchImageView);
    }

    public void launchGame(ActionEvent actionEvent) {
        App.setDifficulty(difficultyComboBox.getValue());
        Stage stage = (Stage) launchButton.getScene().getWindow();
        App.setPlayerCount(1);
        App.setNetworked(false);
        SceneManager.setMainGameInstance(new MainGame());
        App.startGame(stage, new LoadingScreen(App.getBundle().getString("SINGLE_PLAYER_LOADING_TEXT")));
    }
}
