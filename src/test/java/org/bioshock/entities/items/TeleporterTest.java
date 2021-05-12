package org.bioshock.entities.items;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import org.bioshock.components.NetworkC;
import org.bioshock.entities.EntityManager;
import org.bioshock.entities.players.Hider;
import org.bioshock.main.TestingApp;
import org.bioshock.utils.Size;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeleporterTest {

    @BeforeEach
    void setUp() {
        TestingApp.launchJavaFXThread();
    }

    @Test
    void test() {
        //Create item
        Teleporter teleporter = new Teleporter(0);

        //Initialise the hider and seeker
        Hider hider = new Hider(
                new Point3D(70, 70, 0),
                new NetworkC(false),
                new Size(10, 10),
                10,
                Color.RED
        );
        EntityManager.register(hider);
        hider.setInitPositionY(70);
        hider.setInitPositionX(70);

        Point2D initPos = new Point2D(hider.getInitPositionX(), hider.getInitPositionY());
        Point2D newPos = new Point2D(220, 220);

        hider.setPosition(newPos.getX(), newPos.getY());

        //check hider has moved position to new position
        assertEquals(newPos, hider.getPosition());

        //check apply
        teleporter.apply(hider);

        assertEquals(initPos, hider.getPosition());

    }
}