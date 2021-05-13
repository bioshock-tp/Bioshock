package org.bioshock.entities.items.powerups;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;

import org.bioshock.components.NetworkC;
import org.bioshock.entities.EntityManager;
import org.bioshock.entities.players.Hider;
import org.bioshock.main.TestingApp;
import org.bioshock.utils.Size;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

class SpeedItemTest {

    @BeforeEach
    void setUp() {
        TestingApp.launchJavaFXThread();
    }

    @Test
    void test() throws IllegalAccessException {
        //Create item
        SpeedItem speedItem = new SpeedItem(0);

        //Initialise the hider and seeker
        Hider hider = new Hider(
                new Point3D(70, 70, 0),
                new NetworkC(false),
                new Size(10, 10),
                10,
                Color.RED
        );
        EntityManager.register(hider);

        //Apply check start
        double startSpeed = hider.getMovement().getSpeed();

        Field newSpeed;
        try {
            newSpeed = SpeedItem.class.getDeclaredField("NEW_SPEED");
        } catch (NoSuchFieldException | SecurityException e) {
            fail("NEW_SPEED field undefined");
            return;
        }
        newSpeed.setAccessible(true);

        assertEquals(startSpeed, hider.getMovement().getSpeed());

        speedItem.apply(hider);

        assertEquals(newSpeed.getDouble(speedItem), hider.getMovement().getSpeed());

        //Revert check start

        speedItem.revert(hider);

        assertEquals(startSpeed, hider.getMovement().getSpeed());
    }
}