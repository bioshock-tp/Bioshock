package org.bioshock.engine.physics;

import org.bioshock.engine.entity.EntityManager;
import org.bioshock.engine.entity.Point;
import org.bioshock.engine.entity.SquareEntity;
import org.bioshock.engine.input.InputManager;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.transform.Rotate;

public class Movement {
    private double speed = 5;

    private double xDirection = 0;
    private double yDirection = 0;

    private SquareEntity entity;

    public Movement(SquareEntity entity) {
        this.entity = entity;
    }

    private Point getDirection() {
		return new Point(xDirection, yDirection);
	}

    public void tick(double timeDelta) {
        if (xDirection != 0 || yDirection != 0) move(getDirection());
    }

    public void moveTo(double x, double y) {
        move(new Point2D(x, y).subtract(entity.getPosition()));
    }

    public void move(Point2D trans) {
        Point2D target = trans.add(entity.getPosition());

        double x = entity.getX();
        double y = entity.getY();

        if (x != target.getX()) {
            double disp = target.getX() - x;
            x += disp / Math.abs(disp) * speed;
        }

        if (y != target.getY()) {
            double disp = target.getY() - y;
            y += disp / Math.abs(disp) * speed;
        }

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

    public void initMovement() {
        InputManager.onPress(  KeyCode.W, () -> direction(0, -speed));
        InputManager.onPress(  KeyCode.A, () -> direction(-speed, 0));
        InputManager.onPress(  KeyCode.S, () -> direction(0,  speed));
        InputManager.onPress(  KeyCode.D, () -> direction(speed,  0));

        InputManager.onRelease(KeyCode.W, () -> direction(0,  speed));
        InputManager.onRelease(KeyCode.A, () -> direction(speed,  0));
        InputManager.onRelease(KeyCode.S, () -> direction(0, -speed));
        InputManager.onRelease(KeyCode.D, () -> direction(-speed, 0));
    }

    public void direction(double newXDirection, double newYDirection) {
        double newX = Math.abs(xDirection + newXDirection);
        if (newX <= speed) xDirection += newXDirection;

        double newY = Math.abs(yDirection + newYDirection);
        if (newY <= speed) yDirection += newYDirection;
    }

    public void direction(Point2D targ) {
        direction(targ.getX(), targ.getY());
    }

    public void updateFacing(Point2D trans) {
        double rotation = Math.atan2(trans.getX(), -trans.getY())*180/Math.PI;
        setRotation(rotation);
    }

    public void rotate(double degree) {
        Rotate rotate = entity.getRotate();
        Point2D pos = entity.getCentre();

        rotate.setPivotX(pos.getX());
        rotate.setPivotY(pos.getY());

        setRotation(entity.getRotate().getAngle() + degree);
    }

    public double getFacingRotate(Point2D trans) {
        return Math.atan2(trans.getX(), -trans.getY())*180/Math.PI;
    }

    public void setRotation(double newDegree) {
        Point2D pos = entity.getCentre();
        setRotation(newDegree, pos);
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
