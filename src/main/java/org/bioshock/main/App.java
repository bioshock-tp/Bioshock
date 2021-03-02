package org.bioshock.main;

import javafx.scene.input.KeyCode;
import org.bioshock.engine.ai.*;
import org.bioshock.networking.*;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.decimal4j.immutable.Decimal5f;


/**
 * JavaFX App
 */
public class App extends Application {

    private HashSet<KeyCode> pressed = new HashSet<>();

    EmptyClient client;
    //boolean inQueue = false;
    boolean inGame = false;
    boolean detailsSent = false;

    private GameState gameState = null;

    private Pane root = new Pane();
    private double t = 0;
    private Sprite playerSprite1 = new Sprite(300,400,40,40,200,Color.BLUE);
    private Sprite playerSprite2 = new Sprite(400,300,40,40,200,Color.RED);
    //private Enemy enemy = new Enemy(10, 10, 40,40,300,Color.INDIANRED);

    private Parent buildContent(){
        root.setStyle("-fx-background-color: black");
        root.setPrefSize(600,800);

        root.getChildren().addAll(playerSprite1, playerSprite2);
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
        //t += 0.016;
        if(gameState != null) {
            //for(int i = 0; i < gameState.players.length; i++) {
            //    playerSprite1.setCentreXY(gameState.players[i].position.getXDouble(), gameState.players[i].position.getYDouble());
            //}
            playerSprite1.setCentreXY(gameState.players[0].position.getXDouble(), gameState.players[0].position.getYDouble());
            playerSprite2.setCentreXY(gameState.players[1].position.getXDouble(), gameState.players[1].position.getYDouble());
        }
        //TODO: lockstep(...)
        if(client.connected) {
            try {
                client.mutex.acquire();
                try {
                    for (var ms : client.msgQ) {
                        if (ms instanceof Messages.InQueue) {
                            var d = (Messages.InQueue) ms;
                            //System.out.println("timestamp: " + d.timestamp + "; " + "names: " + Arrays.toString(d.names) + "; " + "n: " + d.n + "; " + "N: " + d.N);
                            if(d.n == d.N){
                                // pass data from d into constructor to initialize game state
                                gameState = new GameState(d);
                            }
                        } else if (ms instanceof Messages.ServerInputState) {
                            var d = (Messages.ServerInputState) ms;
                            gameState = lockstep(d, gameState);
                        }
                    }
                    client.msgQ.clear();
                } finally {
                    client.mutex.release();
                }
            } catch(InterruptedException ie) {
                System.out.println(ie);
            }
            client.send(Messages.Serializer.serialize(pollInputs()));
        }

    };
    private int keyStrength(KeyCode k) {return pressed.contains(k)?1:0;}
    // TDDO
    private Messages.ClientInput pollInputs(){
        var x = keyStrength(KeyCode.D) - keyStrength(KeyCode.A);
        var y = keyStrength(KeyCode.W) - keyStrength(KeyCode.S);
        FixedPointVector v = new FixedPointVector(Decimal5f.valueOf(x), Decimal5f.valueOf(y)).normalizedPrime();
        return new Messages.ClientInput(v.getX(), v.getY());
    }
    @Override
    public void start(Stage stage) throws URISyntaxException {
        Scene scene = new Scene(buildContent());
        client = new EmptyClient(new URI("ws://51.15.109.210:8010/lobby"));
        client.connect();
                /*scene.setOnMouseClicked(e -> {
            player.setCentreXY(e.getX(), e.getY());
        });*/

        scene.setOnKeyPressed(e -> {
            pressed.add(e.getCode());
        });
        scene.setOnKeyReleased(e -> {
            pressed.remove(e.getCode());
        });
        stage.setScene(scene);
        stage.show();
    }
    //TODO: use fixed point numbers instead of float
    private static GameState lockstep (Messages.ServerInputState inputs, GameState oldGameState ) {
        System.out.println(oldGameState);
        return new GameState(inputs, oldGameState);
    }
    public static void main(String[] args) throws URISyntaxException {
        //WebSocketClient client = new EmptyClient(new URI("ws://localhost:8887"));
        //ws://51.15.109.210:8080/lobby

//        send(Messages.Serializer.Serialize(ClientInput(serilaize(poll()))));
        //var s = "{\"Case\":\"Details\",\"Fields\":[\"hidenseek\",\"mircea\",[]]}";
        //client.send(s.getBytes(StandardCharsets.UTF_8));


        launch();
    }

}