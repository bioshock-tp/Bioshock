package org.bioshock.scenes;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import org.bioshock.utils.GlobalStrings;

public class LoadingScreen extends GameScene {
	public LoadingScreen (boolean isNetworked, String loadingText) {
        super();

        VBox verticalBox = new VBox();
        verticalBox.setAlignment(Pos.CENTER);
		Label buzzLabel = new Label(GlobalStrings.BUZZ_TEXT);
        buzzLabel.setTextFill(Color.WHITE);
        Label killLabel = new Label(GlobalStrings.KILL_TEXT);
        killLabel.setTextFill(Color.web("0xC50909"));
        TextFlow titleFlow = new TextFlow(buzzLabel, killLabel);
        titleFlow.setStyle("-fx-font-family: \"Helvetica\"; -fx-font-size: 72px; -fx-font-weight: bold; -fx-text-alignment: center;");
        Label infoLabel = new Label(loadingText);
        infoLabel.setStyle("-fx-font-family: \"Helvetica\"; -fx-font-size: 36px; -fx-text-alignment: center;");
        infoLabel.setTextFill(Color.WHITE);
        verticalBox.getChildren().addAll(titleFlow, infoLabel);
//        titleFlow.setTextAlignment(Pos.CENTER);

        getPane().getChildren().add(verticalBox);

		setCursor(Cursor.NONE);
		setBackground(new Background(new BackgroundFill(
            Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY
        )));

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), verticalBox);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);


        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1.5), verticalBox);
        fadeOut.setDelay(Duration.seconds(1));
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setCycleCount(1);

        fadeIn.play();

        fadeIn.setOnFinished(e -> fadeOut.play());

        if (isNetworked) {
            fadeOut.setOnFinished(e -> SceneManager.setScene(new Lobby()));
        } else {
            fadeOut.setOnFinished(e -> SceneManager.setScene(new MainGame(0)));
        }
	}
}
