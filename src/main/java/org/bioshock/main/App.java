package org.bioshock.main;

import org.bioshock.engine.ai.*;
import org.bioshock.networking.*;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.util.List;
import java.util.stream.Collectors;


/**
 * JavaFX App
 */
public class App extends Application {

    private Pane root = new Pane();
    private double t = 0;
    private Sprite player1 = new Sprite(300,400,40,40,200,Color.BLUE);
    private Sprite player2 = new Sprite(400,300,40,40,200,Color.RED);
    //private Enemy enemy = new Enemy(10, 10, 40,40,300,Color.INDIANRED);

    private Parent buildContent(){
        root.setStyle("-fx-background-color: black");
        root.setPrefSize(600,800);

        root.getChildren().addAll(player1, player2);
        /*AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                update();
            }
        };

        timer.start();*/

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };

        timer.start();

        return root;
    }

    /*private void update(){
        Shape intersects = Shape.intersect(enemy.getFov(), player.getSpr());
        if(intersects.getBoundsInLocal().getWidth() != -1){
            enemy.followPlayer(player);
        }
    }*/

    private void update() {
        t += 0.016;
    };

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(buildContent());

        /*scene.setOnMouseClicked(e -> {
            player.setCentreXY(e.getX(), e.getY());
        });*/

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case A:
                    player1.moveLeft();
                    break;
                case D:
                    player1.moveRight();
                    break;
                case W:
                    player1.moveUp();
                    break;
                case S:
                    player1.moveDown();
                    break;
                case J:
                    player2.moveLeft();
                    break;
                case L:
                    player2.moveRight();
                    break;
                case I:
                    player2.moveUp();
                    break;
                case K:
                    player2.moveDown();
                    break;
            }
        });

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws URISyntaxException {
        WebSocketClient client = new EmptyClient(new URI("ws://localhost:8887"));
        //ws://51.15.109.210:8080/lobby
        //WebSocketClient client = new EmptyClient(new URI("ws://51.15.109.210:8080/lobby"));
        client.connect();
        launch();
    }

}