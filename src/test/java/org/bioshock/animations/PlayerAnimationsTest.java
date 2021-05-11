package org.bioshock.animations;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import org.bioshock.components.NetworkC;
import org.bioshock.entities.Entity;
import org.bioshock.entities.EntityManager;
import org.bioshock.entities.players.Hider;
import org.bioshock.entities.players.SeekerAI;
import org.bioshock.scenes.SceneManager;
import org.bioshock.utils.GlobalConstants;
import org.bioshock.utils.Size;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.main.TestingApp;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class PlayerAnimationsTest {

    @Mock
    private Hider hider;
    private SeekerAI seeker;
    private HiderAnimations hiderAnimationsUnderTest;
    private SeekerAnimations seekerAnimationsUnderTest;

    @Before
    public void setUp() {
        TestingApp.launchJavaFXThread();
        // Initialise the hider
        hider = new Hider(
            new Point3D(70, 70, 0),
            new NetworkC(false),
            new Size(10, 10),
            10,
            Color.RED
        );
        hiderAnimationsUnderTest = new HiderAnimations(hider, (int) GlobalConstants.PLAYER_SCALE);
        EntityManager.register(hider);
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
        EntityManager.register(seeker);
        seekerAnimationsUnderTest = new SeekerAnimations(seeker, (int) GlobalConstants.PLAYER_SCALE);
    }

    @Test
    public void testGetMoveRightSprite() {

        final Sprite hiderAnimation = hiderAnimationsUnderTest.getMoveRightSprite();
        final Sprite seekerAnimation = seekerAnimationsUnderTest.getMoveRightSprite();

        assertNotNull(hiderAnimation);
        assertNotNull(seekerAnimation);
    }

    @Test
    public void testGetMoveLeftSprite() {

        final Sprite hiderAnimation = hiderAnimationsUnderTest.getMoveLeftSprite();
        final Sprite seekerAnimation = seekerAnimationsUnderTest.getMoveLeftSprite();

        assertNotNull(hiderAnimation);
        assertNotNull(seekerAnimation);
    }

    @Test
    public void testGetMoveUpSprite() {

        final Sprite hiderAnimation = hiderAnimationsUnderTest.getMoveUpSprite();
        final Sprite seekerAnimation = seekerAnimationsUnderTest.getMoveUpSprite();

        assertNotNull(hiderAnimation);
        assertNotNull(seekerAnimation);
    }

    @Test
    public void testGetMoveDownSprite() {

        final Sprite hiderAnimation = hiderAnimationsUnderTest.getMoveDownSprite();
        final Sprite seekerAnimation = seekerAnimationsUnderTest.getMoveDownSprite();

        assertNotNull(hiderAnimation);
        assertNotNull(seekerAnimation);
    }

    @Test
    public void testGetPlayerIdleSprite() {

        final Sprite hiderAnimation = hiderAnimationsUnderTest.getPlayerIdleSprite();
        final Sprite seekerAnimation = seekerAnimationsUnderTest.getPlayerIdleSprite();

        assertNotNull(hiderAnimation);
        assertNotNull(seekerAnimation);
    }

    @Test
    public void testGetPlayerDying() {

        final Sprite hiderAnimation = hiderAnimationsUnderTest.getPlayerDying();
        final Sprite seekerAnimation = seekerAnimationsUnderTest.getPlayerDying();

        assertNotNull(hiderAnimation);
        assertNotNull(seekerAnimation);
    }
}
