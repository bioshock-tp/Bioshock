package org.bioshock.engine.sprites;

//import org.bioshock.engine.physics.Movement;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Player extends Sprite {
    protected Circle fov;
    protected double r;
    //protected Movement movement;

    public Player(int x, int y, int w, int h, Color c, double r) {
        super(x, y, w, h, c);
        this.r = r;
        //this.movement = new Movement(this);

        fov = new Circle(r);
        fov.setStroke(c);
        fov.setFill(Color.TRANSPARENT);

        getChildren().add(new StackPane(fov, spr));
    }

    /*public Movement getMovement() {
		return movement;
	}*/
}
