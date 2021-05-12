package org.bioshock.physics;

import java.util.Map;
import java.util.Set;

import org.bioshock.engine.input.InputManager;
import org.bioshock.entities.Entity;
import org.bioshock.entities.EntityManager;
import org.bioshock.main.App;
import org.bioshock.utils.Point;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

public class Movement {
    private double speed = 8;

    private Point2D oldPosition;

    /**
     * Displacement as set by the
     * {@link org.bioshock.networking.NetworkManager NetworkManager}
     */
    private Point2D displacement;

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

        Map<KeyCode, Integer> wasd = InputManager.getWasd();

        Integer d = wasd.getOrDefault(KeyCode.D, 0);
        Integer a = wasd.getOrDefault(KeyCode.A, 0);
        Integer s = wasd.getOrDefault(KeyCode.S, 0);
        Integer w = wasd.getOrDefault(KeyCode.W, 0);

        direction((d - a) * speed, (s - w) * speed);

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
        oldPosition = new Point2D(entity.getX(), entity.getY());

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
        if (oldPosition == null) {
            App.logger.fatal("Collision without movement from {}", entity);
            return;
        }

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


    /**
     * Hard sets direction values
     * @param x The new x direction value
     * @param y The new y direction value
     */
    public void setDirection(double x, double y) {
        this.xDirection = x;
        this.yDirection = y;
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
     * @param x The displacement in x relative to previous tick
     * @param y The displacement in y relative to previous tick
     */
	public void setDisplacement(double x, double y) {
        displacement = new Point2D(x, y);
	}


    /**
     * @param movementPaused True if key input should not move player
     */
    public void pauseMovement(boolean movementPaused) {
        this.movementPaused = movementPaused;
    }

    public Point getDirection() {
        return new Point(xDirection, yDirection);
    }


    /**
     * @return The movement relative to previous game tick
     */
    public Point2D getDisplacement() {
        if (displacement != null) {
            return displacement;
        }
        else if (oldPosition != null) {
            return entity.getPosition().subtract(oldPosition);
        } else {
            return new Point2D(0, 0);
        }
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
