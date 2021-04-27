package org.bioshock.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.bioshock.main.App;

import java.util.Objects;

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
        localGameButton.setText(App.getBundle().getString("SINGLE_PLAYER_BUTTON_TEXT"));
        onlineGameButton.setText(App.getBundle().getString("ONLINE_BUTTON_TEXT"));
        backButton.setText(App.getBundle().getString("BACK_MAIN_MENU_BUTTON_TEXT"));

        Image localGameImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/single_user.png")));
        ImageView localGameImageView = new ImageView(localGameImage);
        localGameImageView.setPreserveRatio(true);
        localGameImageView.setFitWidth(16);
        localGameButton.setGraphic(localGameImageView);

        Image onlineGameImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/multi_users.png")));
        ImageView onlineGameImageView = new ImageView(onlineGameImage);
        onlineGameImageView.setPreserveRatio(true);
        onlineGameImageView.setFitWidth(23);
        onlineGameButton.setGraphic(onlineGameImageView);

        Image backImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/arrow.png")));
        ImageView backImageView = new ImageView(backImage);
        backImageView.setPreserveRatio(true);
        backImageView.setFitWidth(17);
        backButton.setGraphic(backImageView);
    }
}
