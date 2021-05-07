package org.bioshock.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.bioshock.main.App;

import java.util.Objects;

public class HelpController {
    public Button backButton;
    public Label aboutLabel;
    public Label aboutDescriptionLabel;
    public Label controlsLabel;
    public Label controlsDescriptionLabel;
    public Label aimLabel;
    public Label aimDescriptionLabel;

    @FXML
    private void switchToMainView() {
        App.setFXMLRoot("main");
    }


    /**
     * Initialise the help menu.
     */
    public void initialize() {
        backButton.setText(App.getBundle().getString("BACK_MAIN_MENU_BUTTON_TEXT"));
        aboutLabel.setText(App.getBundle().getString("ABOUT_TEXT"));
        aboutDescriptionLabel.setText(App.getBundle().getString("ABOUT_DESCRIPTION_TEXT"));
        aimLabel.setText(App.getBundle().getString("AIM_TEXT"));
        aimDescriptionLabel.setText(App.getBundle().getString("AIM_DESCRIPTION_TEXT"));
        controlsLabel.setText(App.getBundle().getString("CONTROLS_TEXT"));
        controlsDescriptionLabel.setText(App.getBundle().getString("CONTROLS_DESCRIPTION_TEXT"));

        Image backImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/arrow.png")));
        ImageView backImageView = new ImageView(backImage);
        backImageView.setPreserveRatio(true);
        backImageView.setFitWidth(17);
        backButton.setGraphic(backImageView);
    }
}
