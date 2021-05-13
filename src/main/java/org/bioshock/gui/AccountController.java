package org.bioshock.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

        Image backImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/arrow.png")));
        ImageView backImageView = new ImageView(backImage);
        backImageView.setPreserveRatio(true);
        backImageView.setFitWidth(17);
        backButton.setGraphic(backImageView);

        Image loginImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/login.png")));
        ImageView loginImageView = new ImageView(loginImage);
        loginImageView.setPreserveRatio(true);
        loginImageView.setFitWidth(17);
        loginButton.setGraphic(loginImageView);

        Image registerImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/register.png")));
        ImageView registerImageView = new ImageView(registerImage);
        registerImageView.setPreserveRatio(true);
        registerImageView.setFitWidth(17);
        registerButton.setGraphic(registerImageView);

        Image scoresImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/scores.png")));
        ImageView scoresImageView = new ImageView(scoresImage);
        scoresImageView.setPreserveRatio(true);
        scoresImageView.setFitWidth(17);
        resultsButton.setGraphic(scoresImageView);
    }
}
