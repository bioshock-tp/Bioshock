package org.bioshock;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {

    private Pane root = new Pane();
    private Sprite player = new Sprite(300,400,40,40,200,Color.PINK);
    private Enemy enemy = new Enemy(10, 10, 40,40,300,Color.INDIANRED);

    private Parent buildContent(){
        root.setStyle("-fx-background-color: black");
        root.setPrefSize(600,800);


        root.getChildren().addAll(player, enemy);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                update();
            }
        };

        timer.start();

        return root;
    }

    private void update(){
        Shape intersects = Shape.intersect(enemy.fov, player.spr);
        if(intersects.getBoundsInLocal().getWidth() != -1){
            enemy.followPlayer(player);
        }
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(buildContent());

        scene.setOnMouseClicked(e -> {
            player.setCentreXY(e.getX(), e.getY());
        });

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}