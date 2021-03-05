package org.bioshock.scenes;

import org.bioshock.engine.scene.SceneManager;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class LoadingScreen extends GameScene {
	public LoadingScreen () {
        super();

		Label label = new Label("Team Project");
		label.setStyle("-fx-font: 100 arial;");
		label.setTextFill(Color.WHITE);

        getPane().getChildren().add(label);

		setCursor(Cursor.NONE);
		setBackground(new Background(new BackgroundFill(
            Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY
        )));

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), label);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), label);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setCycleCount(1);

        fadeIn.play();

        fadeIn.setOnFinished(e -> fadeOut.play());

        fadeOut.setOnFinished(e -> SceneManager.setScene(new MainGame()));
	}
}
