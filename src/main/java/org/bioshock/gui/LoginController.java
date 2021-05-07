package org.bioshock.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.bioshock.main.App;
import org.bioshock.networking.Account;
import org.bioshock.scenes.LoadingScreen;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.prefs.Preferences;

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
        try {

            URL url = new URL("http://recklessgame.net:8034/login");
            String jsonInputString = "{\"Name\":\"" + nameField.getText() + "\",\"Password\":\"" + passwordField.getText() + "\"}";
            byte[] postDataBytes = jsonInputString.getBytes("UTF-8");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setDoOutput(true);
            con.getOutputStream().write(postDataBytes);
            Reader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (int c; (c = in.read()) >= 0;)
                sb.append((char)c);
            String response = sb.toString();
            JSONObject myResponse = new JSONObject(response.toString());
            Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);

            if(myResponse.has("Message")){
                messageLabel.setText(myResponse.getString("Message"));
            }
            else{
                messageLabel.setText("Internal Error");
            }
            if(myResponse.has("Status") && myResponse.getInt("Status") == 200){
                prefs.put("playerName", nameField.getText());
                Account.setUserNam(nameField.getText());
                if(myResponse.has("Score")){
                    Account.setScore(myResponse.getInt("Score"));
                }
                if(myResponse.has("Token")){
                    Account.setToken(myResponse.getString("Token"));
                }
                nameField.setText("");
                passwordField.setText("");

            }

        }
        catch (MalformedURLException ex){
            messageLabel.setText("Internal Error");
        } catch (IOException e) {
            messageLabel.setText("Internal Error");
        }
    }

    @FXML
    public void logoutAccount() {

        Account.setToken("");
        Account.setUserNam("");
        Account.setScore(0);
        nameField.setText("");
        passwordField.setText("");
        messageLabel.setText("You are logged out");
    }

    /**
     * Initialise the Login Page.
     */
    public void initialize() {

        backButton.setText(App.getBundle().getString("BACK_ACCOUNT_MENU_TEXT"));
        loginButton.setText(App.getBundle().getString("LOGIN_BUTTON_TEXT"));
        logoutButton.setText(App.getBundle().getString("LOGOUT_BUTTON_TEXT"));
        nameLabel.setText(App.getBundle().getString("ACCOUNT_NAME") + ":");
        passwordLabel.setText(App.getBundle().getString("ACCOUNT_PASSWORD") + ":");

    }

}
