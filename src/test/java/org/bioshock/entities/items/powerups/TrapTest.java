package org.bioshock.entities.items.powerups;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import org.bioshock.components.NetworkC;
import org.bioshock.entities.EntityManager;
import org.bioshock.entities.players.Hider;
import org.bioshock.entities.players.SeekerAI;
import org.bioshock.scenes.SceneManager;
import org.bioshock.utils.GlobalConstants;
import org.bioshock.utils.Size;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.main.TestingApp;

import static org.junit.jupiter.api.Assertions.*;

class TrapTest {

    @BeforeEach
    void setUp() {
        TestingApp.launchJavaFXThread();
    }

    @Test
    void test() {
        //Create item
        Trap trap = new Trap(0);

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

        assertTrue(hider.getMovement().getSpeed() > 0);

        trap.apply(hider);

        assertEquals(0, hider.getMovement().getSpeed());

        //Revert check start

        trap.revert(hider);

        assertTrue(hider.getMovement().getSpeed() > 0);
    }
}