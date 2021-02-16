package org.bioshock.engine.ai;

import org.bioshock.engine.physics.Movement;
import org.bioshock.engine.sprites.Player;
import org.bioshock.engine.sprites.Sprite;
import org.bioshock.main.App;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class Enemy extends Player {
    public Enemy(int x, int y, int w, int h, Color c, double r) {
        super(x, y, w, h, c, r);
    }

    public void followPlayer(Sprite sprite){
        Shape intersects = Shape.intersect(fov, sprite.getSpr());
        if(intersects.getBoundsInLocal().getWidth() != -1){
            double sx = sprite.getX();
            double sy = sprite.getY();

            Point2D movement = new Point2D(sx - getX(), sy - getY()).normalize();

            setCentreXY(getX() + (movement.getX() * Movement.SPEED / 10), getY() + (movement.getY() * Movement.SPEED / 10));
        }
    }
}
