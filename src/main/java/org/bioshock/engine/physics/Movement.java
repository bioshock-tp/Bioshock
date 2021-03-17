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

    public void tick(double timeDelta) {
        if (xDirection != 0 || yDirection != 0) {
            moveTo(entity.getPosition().add(getDirection()));
        }
    }

    private void move(Point2D trans) {
        Point2D target = trans.add(entity.getPosition());

        double x = entity.getX();
        double y = entity.getY();

        if (x != target.getX()) {
            double dispX = trans.getX();
            x += dispX / Math.abs(dispX);
        }

        if (y != target.getY()) {
            double dispY = trans.getY();
            y += dispY / Math.abs(dispY);
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

                if (entity.intersects(child)) {
                    /* In this case x was not cause, so check y */
                    entity.setPosition(newX, oldY);

                    if (entity.intersects(child)) {
                        /* In this case both x and y were cause */
                        entity.setPosition(oldX, oldY);
                    }
                }
            }
        });
    }



    public void moveTo(Point2D target) {
        for (int i = 0; i < speed; i++) {
            move(target.subtract(entity.getPosition()));
        }
    }

    public void moveTo(double x, double y) {
        moveTo(new Point(x, y));
    }

    public void direction(double newXDirection, double newYDirection) {
        if (Math.abs(xDirection + newXDirection) <= speed) {
            xDirection += newXDirection;
        }

        if (Math.abs(yDirection + newYDirection) <= speed) {
            yDirection += newYDirection;
        }
    }

    public void direction(Point2D targ) {
        direction(targ.getX(), targ.getY());
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

    private Point getDirection() {
        return new Point(xDirection, yDirection);
    }

    public static double getFacingRotate(Point2D trans) {
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
