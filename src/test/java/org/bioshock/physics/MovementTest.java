package org.bioshock.physics;

import org.bioshock.entities.players.Hider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import javafx.geometry.Point2D;

public class MovementTest {
    @Test
    public void moveToTest() {
        int targetX = 10;
        int targetY = 15;

        Hider hider = new Hider();

        Movement movement = hider.getMovement();
        movement.moveTo(targetX, targetY);

        /* moveTo() will not move an entity faster than that entity's speed*/
        int x = (int) Math.min(movement.getSpeed(), targetX);
        int y = (int) Math.min(movement.getSpeed(), targetY);

        Assertions.assertEquals(
            new Point2D(x, y),
            hider.getPosition(),
            "Position incorrect after movement"
        );
    }
}
