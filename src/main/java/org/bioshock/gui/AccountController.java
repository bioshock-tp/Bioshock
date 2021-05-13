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
import org.bioshock.networking.Results;
import org.json.JSONObject;

import javafx.fxml.FXML;
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
    private void switchToResultsView(){
        try {

            URL url = new URL("http://recklessgame.net:8034/getTop5Scores");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setDoOutput(true);
            Reader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (int c; (c = in.read()) >= 0;)
                sb.append((char)c);
            String response = sb.toString();
            JSONObject myResponse = new JSONObject(response.toString());

            if(myResponse.has("Status") && myResponse.getInt("Status") == 200){
                if(myResponse.has("Score1") && myResponse.has("Score2") && myResponse.has("Score3") && myResponse.has("Score4") && myResponse.has("Score5")
                        && myResponse.has("Name1") && myResponse.has("Name2") && myResponse.has("Name3") && myResponse.has("Name4") && myResponse.has("Name5")){

                    Results.setFirstName(myResponse.getString("Name1"));
                    Results.setSecondName(myResponse.getString("Name2"));
                    Results.setThirdName(myResponse.getString("Name3"));
                    Results.setFourthName(myResponse.getString("Name4"));
                    Results.setFifthName(myResponse.getString("Name5"));
                    Results.setFirstScore(Integer.toString(myResponse.getInt("Score1")));
                    Results.setSecondScore(Integer.toString(myResponse.getInt("Score2")));
                    Results.setThirdScore(Integer.toString(myResponse.getInt("Score3")));
                    Results.setFourthScore(Integer.toString(myResponse.getInt("Score4")));
                    Results.setFifthScore(Integer.toString(myResponse.getInt("Score5")));
                }

            }

        }
        catch (MalformedURLException ex){
        } catch (IOException e) {
        }
        finally {
            App.setFXMLRoot("results");
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
