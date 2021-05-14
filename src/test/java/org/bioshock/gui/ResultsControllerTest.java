package org.bioshock.gui;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.bioshock.main.TestingApp;
import org.bioshock.networking.NetworkManager;
import org.junit.Before;
import org.junit.Test;

import javafx.scene.control.Label;


public class ResultsControllerTest {

    private ResultsController resultsControllerUnderTest;

    @Before
    public void setUp() throws Exception {
        TestingApp.launchJavaFXThread();
        resultsControllerUnderTest = new ResultsController();
    }

    @Test
    public void testInitialize() {
        if(!NetworkManager.requestHighScores()) { return; }

        resultsControllerUnderTest.FirstName = new Label();
        resultsControllerUnderTest.SecondName = new Label();
        resultsControllerUnderTest.ThirdName = new Label();
        resultsControllerUnderTest.FourthName = new Label();
        resultsControllerUnderTest.FifthName = new Label();

        resultsControllerUnderTest.FirstScore = new Label();
        resultsControllerUnderTest.SecondScore = new Label();
        resultsControllerUnderTest.ThirdScore = new Label();
        resultsControllerUnderTest.FourthScore = new Label();
        resultsControllerUnderTest.FifthScore = new Label();

        resultsControllerUnderTest.generateScores();

        Label firstNameLabel = resultsControllerUnderTest.FirstName;
        Label secondNameLabel = resultsControllerUnderTest.SecondName;
        Label thirdNameLabel = resultsControllerUnderTest.ThirdName;
        Label fourthNameLabel = resultsControllerUnderTest.FourthName;
        Label fifthNameLabel = resultsControllerUnderTest.FifthName;

        Label firstScoreLabel = resultsControllerUnderTest.FirstScore;
        Label secondScoreLabel = resultsControllerUnderTest.SecondScore;
        Label thirdScoreLabel = resultsControllerUnderTest.ThirdScore;
        Label fourthScoreLabel = resultsControllerUnderTest.FourthScore;
        Label fifthScoreLabel = resultsControllerUnderTest.FifthScore;

        List<Label> nameLabels = List.of(
            firstNameLabel, secondNameLabel, thirdNameLabel, fourthNameLabel, fifthNameLabel
        );

        List<Label> scoreLabels = List.of(
            firstScoreLabel, secondScoreLabel, thirdScoreLabel, fourthScoreLabel, fifthScoreLabel
        );

        for (Label name : nameLabels) {
            assertNotEquals("", name.getText());
        }

        for (Label score : scoreLabels) {
            assertNotEquals("", score.getText());
        }

    }
}
