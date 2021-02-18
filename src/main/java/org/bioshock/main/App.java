package org.bioshock.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bioshock.engine.ai.Enemy;
//import org.bioshock.engine.physics.Movement;
import org.bioshock.engine.sprites.*;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {
    private Pane root = new Pane();
    
    /*
    private static Player player = new Player(300,400,40,40,Color.PINK,200);
    private static Enemy enemy = new Enemy(10,10,40,40,Color.INDIANRED,300);*/
    private static Wall wall = new Wall(100,100,100,200,Color.RED);

    //private static final SquareEntityWithFov[] sprites = {player, enemy, wall};

    public static final Logger logger = LogManager.getLogger(App.class);

    private Parent buildContent(){
        root.setStyle("-fx-background-color: black");
        root.setPrefSize(600,800);

        //root.getChildren().addAll(sprites);

        return root;
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(buildContent());

       /* Movement playerMovement = player.getMovement();

        scene.setOnKeyPressed(e -> {
            KeyCode key = e.getCode();

            if (key == KeyCode.W) {
                logger.debug("W Pressed");
                playerMovement.move(new Point2D(0, Movement.SPEED));
            }
            if (key == KeyCode.A) {
                logger.debug("A Pressed");
                playerMovement.move(new Point2D(-Movement.SPEED, 0));
            }
            if (key == KeyCode.S) {
                logger.debug("S Pressed");
                playerMovement.move(new Point2D(0, -Movement.SPEED));
            }
            if (key == KeyCode.D) {
                logger.debug("D Pressed");
                playerMovement.move(new Point2D(Movement.SPEED, 0));
            }
        });
*/
        stage.setScene(scene);
        stage.show();
    }

    /*public static SquareEntityWithFov[] getSprites() {
        return sprites;
    }*/

    public static void main(String[] args) {
        launch();
    }
}