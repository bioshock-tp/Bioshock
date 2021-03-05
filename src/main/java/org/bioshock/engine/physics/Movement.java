package org.bioshock.engine.physics;

import org.bioshock.engine.entity.SquareEntity;

import javafx.geometry.Point2D;
import javafx.scene.transform.Rotate;

public class Movement {
    private int speed = 10;

    private int xDirection = 0;
    private int yDirection = 0;

    private SquareEntity entity;

    public Movement(SquareEntity entity) {
        this.entity = entity;
    }

    private Point2D getDirection() {
		return new Point2D(xDirection, yDirection);
	}

    public void tick(double timeDelta) {
        if (xDirection != 0 || yDirection != 0) move(getDirection());
    }

    public void move(Point2D trans) {
        Point2D target = trans.add(entity.getPosition());
        int x = entity.getX();
        int y = entity.getY();

        if (x != target.getX()) {
            int disp = (int) target.getX() - x;
            x += disp / Math.abs(disp) * speed;
        }
        if (y != target.getY()) {
            int disp = (int) target.getY() - y;
            y += disp / Math.abs(disp) * speed;
        }
        entity.setPosition(x, y);
    }

    public void direction(int newXDirection, int newYDirection) {
        int newX = Math.abs(xDirection + newXDirection);
        if (newX <= speed) xDirection += newXDirection;

        int newY = Math.abs(yDirection + newYDirection);
        if (newY <= speed) yDirection += newYDirection;
    }


    public double getFacingRotate(Point2D trans){
        return Math.atan2(trans.getX(), -trans.getY())*180/Math.PI;
    }

    public void setRotation(double newDegree) {
        Rotate rotate = entity.getRotation();
        Point2D pos = entity.getCentre();

        rotate.setPivotX(pos.getX());
        rotate.setPivotY(pos.getY());

        rotate.setAngle(newDegree);
    }

    public void setRotation(double newDegree, Point2D pos) {
        Rotate rotate = entity.getRotation();

        rotate.setPivotX(pos.getX());
        rotate.setPivotY(pos.getY());

        rotate.setAngle(newDegree);
    }

    public void setSpeed(int newSpeed) {
        speed = newSpeed;
    }

    public int getSpeed() {
        return speed;
    }
}
