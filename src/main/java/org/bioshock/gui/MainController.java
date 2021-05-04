package org.bioshock.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.bioshock.main.App;

import java.util.Objects;

public class MainController {
    public Button newGameButton;
    public Button quitButton;
    public Button settingsButton;
    public Button helpButton;

    @FXML
    private void openNewGameView() {
        App.setFXMLRoot("new_game");
    }

    @FXML
    private void openHelpView() {
        App.setFXMLRoot("help");
    }

    @FXML
    public void handleQuitButton(ActionEvent actionEvent) {
        Stage stage = (Stage) quitButton.getScene().getWindow();
        stage.close();
        App.exit(0);
    }

    @FXML
    public void openSettingsView(ActionEvent actionEvent) {
        App.setFXMLRoot("settings");
    }

    /**
     * Initialise the main menu.
     */
    public void initialize() {
        newGameButton.setText(App.getBundle().getString("NEW_GAME_BUTTON_TEXT"));
        quitButton.setText(App.getBundle().getString("QUIT_GAME_BUTTON_TEXT"));
        settingsButton.setText(App.getBundle().getString("SETTINGS_BUTTON_TEXT"));
        helpButton.setText(App.getBundle().getString("HELP_BUTTON_TEXT"));

        Image gearImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/gear.png")));
        ImageView gearImageView = new ImageView(gearImage);
        gearImageView.setPreserveRatio(true);
        gearImageView.setFitWidth(18);
        settingsButton.setGraphic(gearImageView);

        Image quitImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/door.png")));
        ImageView quitImageView = new ImageView(quitImage);
        quitImageView.setPreserveRatio(true);
        quitImageView.setFitWidth(18);
        quitButton.setGraphic(quitImageView);

        Image gameImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/gamepad.png")));
        ImageView gameImageView = new ImageView(gameImage);
        gameImageView.setPreserveRatio(true);
        gameImageView.setFitWidth(18);
        newGameButton.setGraphic(gameImageView);

        Image helpImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/help.png")));
        ImageView helpImageView = new ImageView(helpImage);
        helpImageView.setPreserveRatio(true);
        helpImageView.setFitWidth(18);
        helpButton.setGraphic(helpImageView);
    }
}
