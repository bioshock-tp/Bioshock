package org.bioshock.animations;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import org.bioshock.components.NetworkC;
import org.bioshock.entities.EntityManager;
import org.bioshock.entities.players.Hider;
import org.bioshock.entities.players.SeekerAI;
import org.bioshock.scenes.SceneManager;
import org.bioshock.utils.GlobalConstants;
import org.bioshock.utils.Size;
import org.junit.Before;
import org.junit.Test;
import org.main.TestingApp;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SwingAnimationsTest {

    private SwingAnimations swingAnimationsUnderTest;
    private SeekerAI seeker;

    @Before
    public void setUp() {
        TestingApp.launchJavaFXThread();
        double x = SceneManager.getMap().getRooms().get(0).getRoomCenter().getX();
        double y = SceneManager.getMap().getRooms().get(0).getRoomCenter().getY();
        seeker = new SeekerAI(
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
        swingAnimationsUnderTest = new SwingAnimations(seeker, 1.5);
    }

    @Test
    public void testGetTopRightSwing() {

        final Sprite swingAnimation = SwingAnimations.getTopRightSwing();

        assertNotNull(swingAnimation);
    }

    @Test
    public void testGetTopLeftSwing() {

        final Sprite swingAnimation = SwingAnimations.getTopLeftSwing();

        assertNotNull(swingAnimation);
    }

    @Test
    public void testGetBottomRightSwing() {

        final Sprite swingAnimation = SwingAnimations.getBottomRightSwing();

        assertNotNull(swingAnimation);
    }

    @Test
    public void testGetBottomLeftSwing() {

        final Sprite swingAnimation = SwingAnimations.getBottomLeftSwing();

        assertNotNull(swingAnimation);
    }

    @Test
    public void testGetIdle() {

        final Sprite swingAnimation = SwingAnimations.getIdle();

        assertNotNull(swingAnimation);
    }
}
