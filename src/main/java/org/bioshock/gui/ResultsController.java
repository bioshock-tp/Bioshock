package org.bioshock.gui;

import org.bioshock.main.App;
import org.bioshock.networking.Account;
import org.bioshock.networking.Results;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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
