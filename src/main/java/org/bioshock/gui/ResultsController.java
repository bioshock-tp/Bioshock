package org.bioshock.gui;

import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.bioshock.main.App;
import org.bioshock.networking.NetworkManager;
import org.bioshock.utils.JSON;
import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.Objects;

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


    @FXML
    public void initialize() {
        JSONObject highScores = NetworkManager.getHighScores();

        List<Label> nameLabels = List.of(
            FirstName, SecondName, ThirdName, FourthName, FifthName
        );

        List<Label> scoreLabels = List.of(
            FirstScore, SecondScore, ThirdScore, FourthScore, FifthScore
        );

        for (int i = 0; i < nameLabels.size(); i++) {
            String number = Integer.toString(i + 1);
            String nameKey = "Name" + number;
            String name = highScores.getString(nameKey);
            nameLabels.get(i).setText(name);
            String scoreKey = "Score" + Integer.toString(i + 1);
            String score = highScores.getString(scoreKey);
            scoreLabels.get(i).setText(score);
        }

        Image backImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/arrow.png")));
        ImageView backImageView = new ImageView(backImage);
        backImageView.setPreserveRatio(true);
        backImageView.setFitWidth(17);
        backButton.setGraphic(backImageView);



        backButton.setText(
            App.getBundle().getString("BACK_ACCOUNT_MENU_TEXT")
        );
        tableDescription.setText(
            App.getBundle().getString("TOP_5_DESCRIPTION")
        );

        JSONObject account = NetworkManager.getMyAccount();
        if (account.has(JSON.NAME) && account.has(JSON.SCORE)) {
            userDetailsLabel.setText(
                App.getBundle().getString("HI_MESSAGE")  + " "
                + account.getString(JSON.NAME) + ", "
                + App.getBundle().getString("YOUR_SCORE") + " "
                + account.getString(JSON.SCORE) + "."
            );
        }
    }
}
