package org.bioshock.gui;

import org.bioshock.main.App;
import org.bioshock.networking.NetworkManager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController {
    public Button backButton;
    public Button loginButton;
    public Button logoutButton;
    public Label nameLabel;
    public Label passwordLabel;
    public TextField nameField;
    public TextField passwordField;
    public Label messageLabel;


    @FXML
    public void switchToAccountView() {
        messageLabel.setText("");
        nameField.setText("");
        passwordField.setText("");
        App.setFXMLRoot("account");
    }


    @FXML
    public void loginAccount() {
        String response = NetworkManager.login(
            nameField.getText(),
            passwordField.getText()
        );

        nameField.setText("");
        passwordField.setText("");
        messageLabel.setText(response);
    }


    @FXML
    public void logoutAccount() {
        NetworkManager.logout();
        nameField.setText("");
        passwordField.setText("");
        messageLabel.setText("You are logged out");
    }


    @FXML
    public void initialize() {
        backButton.setText(App.getBundle().getString("BACK_ACCOUNT_MENU_TEXT"));
        loginButton.setText(App.getBundle().getString("LOGIN_BUTTON_TEXT"));
        logoutButton.setText(App.getBundle().getString("LOGOUT_BUTTON_TEXT"));
        nameLabel.setText(App.getBundle().getString("ACCOUNT_NAME") + ":");
        passwordLabel.setText(App.getBundle().getString("ACCOUNT_PASSWORD") + ":");
    }
}
