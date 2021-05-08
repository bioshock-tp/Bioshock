package org.bioshock.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.prefs.Preferences;

import org.bioshock.main.App;
import org.bioshock.networking.Account;
import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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

        try {

            URL url = new URL("http://recklessgame.net:8034/register");
            String jsonInputString = "{\"Name\":\"" + nameField.getText() + "\",\"Password\":\"" + passwordField.getText() + "\",\"PasswordConfirmation\":\"" + passwordConfirmationField.getText() + "\"}";
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
                passwordConfirmationField.setText("");

            }

        }
        catch (MalformedURLException ex){
            messageLabel.setText("Internal Error");
        } catch (IOException e) {
            messageLabel.setText("Internal Error");
        }
    }

    /**
     * Initialise the register.
     */
    public void initialize() {

        backButton.setText(App.getBundle().getString("BACK_ACCOUNT_MENU_TEXT"));
        registerButton.setText(App.getBundle().getString("REGISTER_BUTTON_TEXT"));
        nameLabel.setText(App.getBundle().getString("ACCOUNT_NAME") + ":");
        passwordLabel.setText(App.getBundle().getString("ACCOUNT_PASSWORD") + ":");
        passwordConfirmationLabel.setText(App.getBundle().getString("ACCOUNT_PASSWORD_CONFIRMATION") + ":");

    }

}
