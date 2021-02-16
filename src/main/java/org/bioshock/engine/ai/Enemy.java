package org.bioshock.engine.ai;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class Enemy extends Sprite {

    Point2D movement = new Point2D(0,0);

    public Enemy(int x, int y, int w, int h, double r, Color c) {
        super(x, y, w, h, r, c);
    }

    public void followPlayer(Sprite s){

        Shape intersects = Shape.intersect(fov, s.spr);
        if(intersects.getBoundsInLocal().getWidth() != -1){
            double sx = s.getCentre().getX();
            double sy = s.getCentre().getY();

            movement = new Point2D(sx - getCentre().getX(), sy - getCentre().getY()).normalize();

            setCentreXY((getCentre().getX() + movement.getX() * speed), (getCentre().getY() + movement.getY() * speed));

        }


    }

	public Shape getFov() {
		return fov;
	}
}
