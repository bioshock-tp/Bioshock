package org.bioshock.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.bioshock.main.App;
import org.bioshock.networking.Account;
import org.bioshock.networking.Results;
import org.bioshock.scenes.LoadingScreen;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.http.HttpClient;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.prefs.Preferences;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONObject;

public class ResultsController {
    public Label FirstName;
    public Label SecondName;
    public Label ThirdName;
    public Label FourthName;
    public Label FifthName;
    public Label FirstScore;
    public Label SecondScore;
    public Label ThirdScore;
    public Label FourthScore;
    public Label FifthScore;
    public Label userDetailsLabel;
    public Label tableDescription;
    public Button backButton;

    @FXML
    public void switchToAccountView() {

        App.setFXMLRoot("account");

    }

    /**
     * Initialise the results.
     */
    public void initialize() {

        FirstName.setText(Results.getFirstName());
        SecondName.setText(Results.getSecondName());
        ThirdName.setText(Results.getThirdName());
        FourthName.setText(Results.getFourthName());
        FifthName.setText(Results.getFifthName());
        FirstScore.setText(Results.getFirstScore());
        SecondScore.setText(Results.getSecondScore());
        ThirdScore.setText(Results.getThirdScore());
        FourthScore.setText(Results.getFourthScore());
        FifthScore.setText(Results.getFifthScore());

        backButton.setText(App.getBundle().getString("BACK_ACCOUNT_MENU_TEXT"));
        tableDescription.setText(App.getBundle().getString("TOP_5_DESCRIPTION"));
        userDetailsLabel.setText(App.getBundle().getString("HI_MESSAGE") + " " + Account.getUserName() + "," + App.getBundle().getString("YOUR_SCORE") + " " + Account.getScore() + ".");


    }

}
