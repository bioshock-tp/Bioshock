package org.bioshock.engine.physics;

import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.entity.EntityManager;
import org.bioshock.engine.entity.Point;
import org.bioshock.engine.entity.SquareEntity;

import javafx.geometry.Point2D;
import javafx.scene.transform.Rotate;

public class Movement {
    private double speed = 5;

    private int xDirection = 0;
    private int yDirection = 0;

    private SquareEntity entity;

    public Movement(SquareEntity entity) {
        this.entity = entity;
    }

    private Point2D getDirection() {
		return new Point(xDirection, yDirection);
	}

    public void tick(double timeDelta) {
        if (xDirection != 0 || yDirection != 0) move(getDirection());
    }

    public void move(Point2D trans) {
        Point2D target = trans.add(entity.getPosition());

        double x = entity.getX();
        double y = entity.getY();

        double dispX = target.getX() - x;
        if (x != target.getX()) {
            x += dispX / Math.abs(dispX) * speed;
        }

        double dispY = target.getY() - y;
        if (y != target.getY()) {
            y += dispY / Math.abs(dispY) * speed;
        }

        while (x < 0) x++;
        while (y < 0) y++;
        while (x + entity.getWidth() > WindowManager.getWindowWidth()) x--;
        while (y + entity.getHeight() > WindowManager.getWindowHeight()) y--;

        double oldX = entity.getX();
        double oldY = entity.getY();
        entity.setPosition(x, y);

        final double newX = x;
        EntityManager.getEntityList().forEach(child -> {
            if (child == entity) return;

            if (entity.intersects(child)) {

                /* Check if x value was cause of collision */
                entity.setX(oldX);

            }
            if (entity.intersects(child)) {

                /* In this case x was not cause, so check y */
                entity.setX(newX);
                entity.setY(oldY);

            }
            if (entity.intersects(child)) {

                /* In this case both x and y were cause */
                entity.setPosition(oldX, oldY);
            }
        });
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
        Rotate rotate = entity.getRotate();
        Point2D pos = entity.getCentre();

        rotate.setPivotX(pos.getX());
        rotate.setPivotY(pos.getY());

        rotate.setAngle(newDegree);
    }

    public void setRotation(double newDegree, Point2D pivot) {
        Rotate rotate = entity.getRotate();

        rotate.setPivotX(pivot.getX());
        rotate.setPivotY(pivot.getY());

        rotate.setAngle(newDegree);
    }

    public void setSpeed(double newSpeed) {
        speed = newSpeed;
    }

    public double getSpeed() {
        return speed;
    }
}
