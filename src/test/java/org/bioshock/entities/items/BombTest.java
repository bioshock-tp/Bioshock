package org.bioshock.entities.items;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import org.bioshock.components.NetworkC;
import org.bioshock.entities.EntityManager;
import org.bioshock.entities.players.Hider;
import org.bioshock.utils.Size;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.main.TestingApp;

import static org.junit.jupiter.api.Assertions.*;

class BombTest {

    @BeforeEach
    void setUp() {
        TestingApp.launchJavaFXThread();
    }

    @Test
    void test() {
        //Create item
        Bomb bomb = new Bomb(0);

        //Initialise the hider and seeker
        Hider hider = new Hider(
                new Point3D(70, 70, 0),
                new NetworkC(false),
                new Size(10, 10),
                10,
                Color.RED
        );
        EntityManager.register(hider);
        hider.initAnimations();

        assertFalse(hider.isDead());

        bomb.apply(hider);

        assertTrue(hider.isDead());
    }
}