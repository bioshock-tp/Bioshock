package org.bioshock.entities.items.powerups;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;

import org.bioshock.components.NetworkC;
import org.bioshock.entities.EntityManager;
import org.bioshock.entities.SquareEntity;
import org.bioshock.entities.players.Hider;
import org.bioshock.main.TestingApp;
import org.bioshock.utils.Size;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

class InvisibilityItemTest {

    @BeforeEach
    void setUp() {
        TestingApp.launchJavaFXThread();
    }

    @Test
    void test() throws IllegalAccessException {
        //Create item
        InvisibilityItem invisibilityItem = new InvisibilityItem(0);

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
        Field invisible;
        try {
            invisible = SquareEntity.class.getDeclaredField("invisible");
        } catch (NoSuchFieldException | SecurityException e) {
            fail("invisible field undefined");
            return;
        }
        invisible.setAccessible(true);

        assertFalse(invisible.getBoolean(hider));

        invisibilityItem.apply(hider);

        assertTrue(invisible.getBoolean(hider));

        //Revert check start

        invisibilityItem.revert(hider);

        assertFalse(invisible.getBoolean(hider));
    }
}