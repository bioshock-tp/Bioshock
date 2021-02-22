package org.bioshock.scenes;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.bioshock.engine.scene.GameScene;

public class GameOverScreen  extends GameScene {
    public GameOverScreen(){
        Label label = new Label("GAME OVER");
        label.setStyle("-fx-font: 100 arial;");
        label.setTextFill(Color.RED);

        getChildren().add(label);
        setCursor(Cursor.NONE);
        setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), label);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), label);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setCycleCount(1);

        fadeIn.play();

        fadeIn.setOnFinished((e) -> {
            fadeOut.play();
        });
    }

    @Override
    public void unload() {
        super.unload();
        System.out.println("Unloading game over Screen");
    }

}
