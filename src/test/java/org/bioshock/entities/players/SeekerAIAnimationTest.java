package org.bioshock.entities.players;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.bioshock.animations.SeekerAnimations;
import org.bioshock.animations.Sprite;
import org.bioshock.animations.SwingAnimations;
import org.bioshock.components.NetworkC;
import org.bioshock.main.TestingApp;
import org.bioshock.scenes.SceneManager;
import org.bioshock.utils.GlobalConstants;
import org.bioshock.utils.Size;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

@RunWith(MockitoJUnitRunner.class)
public class SeekerAIAnimationTest {

    private SeekerAI seeker;

    @Before
    public void setUp() throws Exception {
        TestingApp.launchJavaFXThread();
        Point2D roomCenter = SceneManager.getMap().getRooms().get(0).getRoomCenter();
        seeker = new SeekerAI(
            new Point3D(roomCenter.getX(), roomCenter.getY(), 1),
            new NetworkC(false),
            new Size(10, 10),
            50,
            Color.RED
        );
    }

    @Test
    public void testInitAnimations() {

        seeker.initAnimations();

        // Checks seeker animation is not null and is the idle sprite.
        assertNotNull(seeker.getSeekerAnimations());
        assertEquals(seeker.getCurrentSprite(), seeker.getSeekerAnimations().getPlayerIdleSprite());

        // Checks swing animation is not null and is the idle sprite.
        assertNotNull(seeker.getSwingAnimations());
        assertEquals(seeker.getCurrentSwingSprite(), SwingAnimations.getIdle());

    }

    @Test
    public void testSetAnimation() {
        seeker.initAnimations();
        seeker.setAnimation();

        SeekerAnimations seekerAnimations = new SeekerAnimations(seeker, GlobalConstants.PLAYER_SCALE);
        Sprite seekerSprite = seekerAnimations.getMoveDownSprite();
        seeker.setCurrentSprite(seekerSprite);

        // Checks if current sprite is equal to sprite newly set
        assertEquals(seeker.getCurrentSprite(), seekerSprite);

    }

    @Test
    public void testSetSwingAnimation() {
        seeker.initAnimations();
        seeker.setAnimation();

        seeker.setCurrentSwingSprite(SwingAnimations.getTopLeftSwing());

        // Checks if current swing sprite is equal to sprite newly set
        assertEquals(seeker.getCurrentSwingSprite(), SwingAnimations.getTopLeftSwing());
    }

}
