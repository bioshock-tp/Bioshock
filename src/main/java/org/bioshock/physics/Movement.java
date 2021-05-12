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

/**
 * Responsible for moving {@link Entity Entities} in game
 */
public class Movement {
    /**
     * The {@link Entity} this {@code Movement} is responsible for controlling
     */
    private Entity entity;

    /**
     * How many pixels {@link #entity} moves per tick
     */
    private double speed = 8;

    /**
     * The position {@link #entity} was in during the previous tick
     */
    private Point2D oldPosition;

    /**
     * Displacement as set by the
     * {@link org.bioshock.networking.NetworkManager NetworkManager}
     */
    private Point2D displacement;

    /**
     * How much {@link #entity} should move in the x axis during the next tick
     */
    private double xDirection = 0;

    /**
     * How much {@link #entity} should move in the x axis during the next tick
     */
    private double yDirection = 0;

    /**
     * True if key input should not move player
     */
    private boolean movementPaused;


    /**
     * Initialises the {@link Entity} to control
     * @param entity The {@link Entity} this {@code Movement} is responsible
     * for controlling
     */
    public Movement(Entity entity) {
        this.entity = entity;
    }


    /**
     * Adds key listeners to the WASD keys
     * @see {@link InputManager#initMovement()}
     */
    public void initMovement() {
        InputManager.initMovement();
    }


    /**
     * Updates {@link #direction(double, double) direction} {@link #entity}
     * should move during this tick based on WASD key presses, and then moves
     * the entity accordingly.
     * @param delta The time elapsed since the last game update
     */
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


    /**
     * Moves {@link #entity} a single pixel according to the translation
     * parameter
     * @param translation The translation to be applied
     */
    private void move(Point2D translation) {
        Point2D target = translation.add(entity.getPosition());

        double x = entity.getX();
        double y = entity.getY();

        double dispX = translation.getX();
        if (x != target.getX()) x += dispX / Math.abs(dispX);

        double dispY = translation.getY();
        if (y != target.getY()) y += dispY / Math.abs(dispY);

        entity.setPosition(x, y);
    }


    /**
     * Moves {@link #entity} to a given position at a rate of {@link #speed}
     * per call
     * @param target The desired position to move to
     */
    public void moveTo(Point2D target) {
        oldPosition = new Point2D(entity.getX(), entity.getY());

        for (int i = 0; i < speed; i++) {
            move(target.subtract(entity.getPosition()));
        }
    }


    /**
     * Calls {@link #moveTo(Point2D)} with the given {@code doubles}
     * @param x The x coordinate to pass
     * @param y The y coordinate to pass
     * @see #moveTo(Point2D)
     */
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


    /**
     * Adds the provided x and y values current direction x and y values
     * @param newXDirection To be added to {@link #xDirection}
     * @param newYDirection To be added to {@link #yDirection}
     */
    public void direction(double newXDirection, double newYDirection) {
        if (Math.abs(xDirection + newXDirection) <= speed) {
            xDirection = newXDirection == 0 ? 0 : xDirection + newXDirection;
        }

        if (Math.abs(yDirection + newYDirection) <= speed) {
            yDirection = newYDirection == 0 ? 0 : yDirection + newYDirection;
        }
    }


    /**
     * Passes the parameter to {@link #direction(Point2D)}
     * @param target To be passed to {@link #direction(Point2D)}
     * @see #direction(Point2D)
     */
    public void direction(Point2D target) {
        direction(target.getX(), target.getY());
    }


    /**
     * Overwrites the current {@link #direction(Point2D) direction} values
     * @param x The new x direction value
     * @param y The new y direction value
     * @see #direction(Point2D)
     */
    public void setDirection(double x, double y) {
        this.xDirection = x;
        this.yDirection = y;
    }


    /**
     * @param newSpeed The new number of pixels to move per game update
     */
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


    /**
     * @return How much {@link #entity} should move in the next game tick
     */
    public Point2D getDirection() {
        return new Point2D(xDirection, yDirection);
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


    /**
     * Calculates the angle in degrees of the given vector relative to the x
     * axis
     * @param vector
     * @return
     */
    public static double getFacingRotate(Point2D vector) {
        double angleRadians = Math.atan2(vector.getX(), -vector.getY());
        double angleDegrees = angleRadians * 180 / Math.PI;

        if (angleDegrees < 0) {
            angleDegrees = Math.abs(angleDegrees);
        }

        else {
            angleDegrees = 360 - angleDegrees;
        }

        return angleDegrees;
    }


    /**
     * @return How many pixels entity moves per tick
     */
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
