package org.bioshock.gui;

import java.util.Objects;

import org.bioshock.main.App;
import org.bioshock.networking.NetworkManager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RegisterController {
    public Button backButton;
    public Button registerButton;
    public Label nameLabel;
    public Label passwordLabel;
    public Label passwordConfirmationLabel;
    public TextField nameField;
    public PasswordField passwordField;
    public PasswordField passwordConfirmationField;
    public Label messageLabel;


    @FXML
    public void switchToAccountView() {
        messageLabel.setText("");
        nameField.setText("");
        passwordField.setText("");
        passwordConfirmationField.setText("");
        App.setFXMLRoot("account");
    }


    @FXML
    public void registerAccount() {
        String response = NetworkManager.registerAccount(
            nameField.getText(),
            passwordField.getText(),
            passwordConfirmationField.getText()
        );

        nameField.setText("");
        passwordField.setText("");
        passwordConfirmationField.setText("");

        messageLabel.setText(response);
    }


    @FXML
    public void initialize() {
        backButton.setText(App.getBundle().getString("BACK_ACCOUNT_MENU_TEXT"));
        registerButton.setText(App.getBundle().getString("REGISTER_BUTTON_TEXT"));
        nameLabel.setText(App.getBundle().getString("ACCOUNT_NAME") + ":");
        passwordLabel.setText(App.getBundle().getString("ACCOUNT_PASSWORD") + ":");
        passwordConfirmationLabel.setText(App.getBundle().getString("ACCOUNT_PASSWORD_CONFIRMATION") + ":");

        Image registerImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/register.png")));
        ImageView registerImageView = new ImageView(registerImage);
        registerImageView.setPreserveRatio(true);
        registerImageView.setFitWidth(17);
        registerButton.setGraphic(registerImageView);

        Image backImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/arrow.png")));
        ImageView backImageView = new ImageView(backImage);
        backImageView.setPreserveRatio(true);
        backImageView.setFitWidth(17);
        backButton.setGraphic(backImageView);
    }
}
