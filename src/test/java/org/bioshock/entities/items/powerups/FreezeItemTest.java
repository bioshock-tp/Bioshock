package org.bioshock.entities.items.powerups;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import org.bioshock.audio.AudioManager;
import org.bioshock.components.NetworkC;
import org.bioshock.entities.EntityManager;
import org.bioshock.entities.players.Hider;
import org.bioshock.entities.players.SeekerAI;
import org.bioshock.main.TestingApp;
import org.bioshock.scenes.SceneManager;
import org.bioshock.utils.GlobalConstants;
import org.bioshock.utils.Size;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FreezeItemTest {

    @BeforeEach
    void setUp() {
        TestingApp.launchJavaFXThread();
    }

    @Test
    void test() {
        //Create item
        FreezeItem freezeItem = new FreezeItem(0);

        //Initialise the hider and seeker
        Hider hider = new Hider(
                new Point3D(70, 70, 0),
                new NetworkC(false),
                new Size(10, 10),
                10,
                Color.RED
        );
        EntityManager.register(hider);
        double x = SceneManager.getMap().getRooms().get(0).getRoomCenter().getX();
        double y = SceneManager.getMap().getRooms().get(0).getRoomCenter().getY();
        SeekerAI seeker = new SeekerAI(
                new Point3D(
                        x - (double) GlobalConstants.UNIT_WIDTH / 2,
                        y - (double) GlobalConstants.UNIT_HEIGHT / 2,
                        0.25
                ),
                new NetworkC(true),
                new Size(
                        GlobalConstants.UNIT_WIDTH,
                        GlobalConstants.UNIT_HEIGHT
                ),
                520,
                Color.INDIANRED
        );
        EntityManager.register(seeker);

        //Apply check start

        assertTrue(seeker.getMovement().getSpeed() > 0);

        freezeItem.apply(hider);

        assertEquals(0, seeker.getMovement().getSpeed());

        //Revert check start

        freezeItem.revert(hider);

        assertTrue(seeker.getMovement().getSpeed() > 0);

    }

}