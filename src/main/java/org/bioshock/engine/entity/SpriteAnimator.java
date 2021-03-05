package org.bioshock.engine.entity;

import javafx.animation.Animation;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SpriteAnimator extends Application {
    private static final Image IMAGE = new Image(SpriteAnimator.class.getResourceAsStream("alex_left_run.png"));

    private static final int COLUMNS  =   6;
    private static final int COUNT    =  6;
    private static final int OFFSET_X =  0;
    private static final int OFFSET_Y =  0;
    private static final int WIDTH    = 16;
    private static final int HEIGHT   = 32;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        //primaryStage.setTitle("Alex Running");

        final ImageView imageView = new ImageView(IMAGE);
        imageView.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));

        final Animation animation = new SpriteAnimation(
                imageView,
                Duration.millis(600),
                COUNT, COLUMNS,
                OFFSET_X, OFFSET_Y,
                WIDTH, HEIGHT
        );
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();

        primaryStage.setScene(new Scene(new Group(imageView)));
        primaryStage.show();
    }

    public static Image getImage() {
        return IMAGE;
    }
}
