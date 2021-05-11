package org.bioshock.entities.players;

import java.lang.reflect.Field;

import org.bioshock.components.NetworkC;
import org.bioshock.entities.EntityManager;
import org.bioshock.utils.Size;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.main.TestingApp;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import junit.framework.TestCase;

public class SeekerAITest extends TestCase {
    @BeforeAll
    public static void init() {
        TestingApp.launchJavaFXThread();
    }


    @Test
    public void testTest() {
        SeekerAI seeker = new SeekerAI(
            new Point3D(50, 50, 50),
            new NetworkC(false),
            new Size(10, 10),
            50,
            Color.RED
        );
        EntityManager.register(seeker);

        Hider hider = new Hider(
            new Point3D(70, 70, 0),
            new NetworkC(false),
            new Size(10, 10),
            10,
            Color.RED
        );
        EntityManager.register(hider);

        Field isSearching;
        try {
            isSearching = SeekerAI.class.getDeclaredField("isSearching");
        } catch (NoSuchFieldException | SecurityException e) {
            fail("isSearching field undefined for " + seeker);
            return;
        }

        isSearching.setAccessible(true);

        try {
            assertTrue(
                "Is not searching for seeker",
                isSearching.getBoolean(seeker) || hider.isDead()
            );
        } catch (IllegalArgumentException | IllegalAccessException e) {
            fail("Failed to get value of isSearching for " + seeker);
        }
    }
}