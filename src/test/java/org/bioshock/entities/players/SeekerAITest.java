package org.bioshock.entities.players;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.bioshock.components.NetworkC;
import org.bioshock.engine.pathfinding.GraphNode;
import org.bioshock.entities.Entity;
import org.bioshock.entities.EntityManager;
import org.bioshock.scenes.SceneManager;
import org.bioshock.utils.Size;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.main.TestingApp;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import junit.framework.TestCase;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.fail;

public class SeekerAITest extends TestCase {

    private SeekerAI seeker;
    private Hider hider;
    private Point2D roomCenter;
    private final CountDownLatch waiter = new CountDownLatch(1);

    private void initVariables(){
        int x = (int) SceneManager.getMap().getRooms().get(0).getRoomCenter().getX();
        int y = (int) SceneManager.getMap().getRooms().get(0).getRoomCenter().getY();
        roomCenter = new Point2D(x,y);

        hider = new Hider(
                new Point3D(
                        (int) SceneManager.getMap().getRooms().get(1).getRoomCenter().getX(),
                        (int) SceneManager.getMap().getRooms().get(1).getRoomCenter().getY(),
                        1),
                new NetworkC(false),
                new Size(10, 10),
                100,
                Color.RED
        );
        EntityManager.register(hider);

        seeker = new SeekerAI(
                new Point3D(roomCenter.getX(), roomCenter.getY(), 1),
                new NetworkC(false),
                new Size(10, 10),
                200,
                Color.RED
        );
        EntityManager.register(seeker);

        hider.initAnimations();
        seeker.initAnimations();
    }

    @BeforeAll
    public static void init() {
        TestingApp.launchJavaFXThread();
        TestingApp.stopGameLoop();
    }


    @Test
    public void initTest() {
        initVariables();

        //Check that the seeker is at the right position
        assertEquals(roomCenter, seeker.getPosition());

        //make sure that all animations and rendering components have been assigned
        assertNotNull(seeker.getRendererC());
        assertNotNull(seeker.getSwatterHitbox());
        assertNotNull(seeker.getSeekerAnimations());
        assertNotNull(seeker.getCurrentSprite());
        assertNotNull(seeker.getCurrentSwingSprite());
        assertNotNull(seeker.getSwingAnimations());
        assertNotNull(seeker.getRenderArea());

        //make sure the hider is alive
        assertFalse(hider.isDead());
    }

    @Test
    public void catchHiderTest() {
        initVariables();

        for(int i=0; i<500;i++){
            //do a tick
            EntityManager.tick(0.05);
            seeker.setAnimation();

            //make sure the seeker sets the hider as a target if in sight
            if(hider.getPosition().subtract(seeker.getPosition()).magnitude() < seeker.getRadius()){
                assertNotNull(seeker.getTarget());
                assertEquals(hider, seeker.getTarget());
            }
        }
        //test if hider has been caught
        assertTrue(hider.isDead());

        //test that the seeker has moved from its original position
        assertNotSame(roomCenter, seeker.getPosition());

    }
}
