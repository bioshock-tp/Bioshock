package org.bioshock.gui;

import org.bioshock.main.App;
import org.bioshock.networking.NetworkManager;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;

public class AccountController {
    public Button loginButton;
    public Button registerButton;
    public Button resultsButton;
    public Button backButton;


    @FXML
    private void switchToLoginView() {
        App.setFXMLRoot("login");
    }


    @FXML
    private void switchToRegisterView() {
        App.setFXMLRoot("register");
    }


    @FXML
    private void switchToResultsView() {
        resultsButton.getScene().setCursor(Cursor.WAIT);
        if (NetworkManager.requestHighScores()) {
            resultsButton.getScene().setCursor(Cursor.DEFAULT);
            App.setFXMLRoot("results");
        } else {
            resultsButton.getScene().setCursor(Cursor.DEFAULT);
            resultsButton.setText(
                App.getBundle().getString("UNAVAILABLE")
            );
            resultsButton.setDisable(true);
        }
    }


    @FXML
    public void switchToMainView() {
        App.setFXMLRoot("main");
    }


    /**
     * Initialise the Account Menu.
     */
    public void initialize() {
        loginButton.setText(App.getBundle().getString("LOGIN_BUTTON_TEXT"));
        registerButton.setText(App.getBundle().getString("REGISTER_BUTTON_TEXT"));
        resultsButton.setText(App.getBundle().getString("RESULTS_BUTTON_TEXT"));
        backButton.setText(App.getBundle().getString("BACK_MAIN_MENU_BUTTON_TEXT"));
    }
}
