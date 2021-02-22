package org.bioshock.engine.physics;

import org.bioshock.engine.entity.SquareEntity;

import javafx.geometry.Point2D;

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

    public void setSpeed(int newSpeed) {
        speed = newSpeed;
    }

    public int getSpeed() {
        return speed;
    }
}
