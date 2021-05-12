package org.bioshock.physics;

import java.util.Set;

import org.bioshock.engine.input.InputManager;
import org.bioshock.entities.Entity;
import org.bioshock.entities.EntityManager;
import org.bioshock.utils.Point;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

public class Movement {
    private double speed = 8;

    private Point2D oldPosition;

    private double xDirection = 0;
    private double yDirection = 0;

    private Entity entity;

    /**
     * True if key input should not move player
     */
    private boolean movementPaused;


    public Movement(Entity entity) {
        this.entity = entity;
    }

    public void tick(double delta) {
        oldPosition = new Point2D(entity.getX(), entity.getY());

        direction(
            (InputManager.getWasd().getOrDefault(KeyCode.D, 0) - InputManager.getWasd().getOrDefault(KeyCode.A, 0)) * speed,
            (InputManager.getWasd().getOrDefault(KeyCode.S, 0)
            - InputManager.getWasd().getOrDefault(KeyCode.W, 0)) * speed
        );

        if (
            entity == EntityManager.getCurrentPlayer()
            && (xDirection != 0 || yDirection != 0)
        ) {
            moveTo(entity.getPosition().add(getDirection()));
        }
    }

    private void move(Point2D trans) {
        Point2D target = trans.add(entity.getPosition());

        double x = entity.getX();
        double y = entity.getY();

        double dispX = trans.getX();
        if (x != target.getX()) x += dispX / Math.abs(dispX);

        double dispY = trans.getY();
        if (y != target.getY()) y += dispY / Math.abs(dispY);

        entity.setPosition(x, y);
    }

    public void moveTo(Point2D target) {
        for (int i = 0; i < speed; i++) {
            move(target.subtract(entity.getPosition()));
        }
    }

    public void moveTo(double x, double y) {
        moveTo(new Point2D(x, y));
    }


    /**
     * Moves to position of previous tick
     * @param collisions A {@code Set} of entities, {@link #entity} is
     * colliding with
     */
    public void moveBack(Set<Entity> collisions) {
        double newX = entity.getX();

        /* Check if x value was cause of collision */
        entity.setX(oldPosition.getX());

        if (collisions.stream().anyMatch(e -> entity.intersects(e))) {
            /* In this case x was not cause, so check y */
            entity.setPosition(newX, oldPosition.getY());

            if (collisions.stream().anyMatch(e -> entity.intersects(e))) {
                /* In this case both x and y were cause */
                entity.setPosition(oldPosition.getX(), oldPosition.getY());
            }
        }
    }

    public void initMovement() {
        InputManager.initMovement();
    }

    public void direction(double newXDirection, double newYDirection) {
        if (Math.abs(xDirection + newXDirection) <= speed) {
            xDirection = newXDirection == 0 ? 0 : xDirection + newXDirection;
        }

        if (Math.abs(yDirection + newYDirection) <= speed) {
            yDirection = newYDirection == 0 ? 0 : yDirection + newYDirection;
        }
    }

    public void direction(Point2D targ) {
        direction(targ.getX(), targ.getY());
    }

    public void setSpeed(double newSpeed) {
        if (xDirection > 0) {
            xDirection = newSpeed;
        }
        else if (xDirection < 0) {
            xDirection = -newSpeed;
        }

        if (yDirection > 0) {
            yDirection = newSpeed;
        }
        else if (yDirection < 0) {
            yDirection = -newSpeed;
        }

        speed = newSpeed > 0.05 ? newSpeed : 0;
    }


    /**
     * @param movementPaused True if key input should not move player
     */
    public void pauseMovement(boolean movementPaused) {
        this.movementPaused = movementPaused;
    }


    /**
     * Sets both directions to 0
     */
    public void stopPlayer() {
        direction(0, 0);
    }


    public Point getDirection() {
        return new Point(xDirection, yDirection);
    }

    public static double getFacingRotate(Point2D trans) {
        double angle = Math.atan2(trans.getX(), -trans.getY()) * 180 / Math.PI;

        if (angle < 0) {
            angle = Math.abs(angle);
        }

        else {
            angle = 360 - angle;
        }

        return angle;
    }

    public double getSpeed() {
        return speed;
    }


    /**
     * @return True if movement is paused
     */
    public boolean movementPaused() {
        return movementPaused;
    }
}
