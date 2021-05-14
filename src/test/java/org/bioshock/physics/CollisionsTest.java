package org.bioshock.physics;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.bioshock.entities.Entity;
import org.bioshock.entities.players.Hider;
import org.bioshock.main.TestingApp;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class CollisionsTest {
    @Test
    public void spawnInTest() {
        TestingApp.launchJavaFXThread();

        /* Spawns a hider at (0, 0) */
        new Hider();

        Queue<Entity> collisionsCopy = new ConcurrentLinkedDeque<>();
        CountDownLatch latch = new CountDownLatch(1);

        /* Spawns a second hider at (0, 0), on top of previous hider */
        new Hider() {
            @Override
            public void collisionTick(Set<Entity> collisions) {
                collisionsCopy.addAll(collisions);
                latch.countDown();
            }
        };

        try {
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Assertions.assertNotEquals(
            0,
            collisionsCopy.size(),
            "No collisions detected"
        );
    }


    @Test
    public void movementTest() {
        TestingApp.launchJavaFXThread();

        /* Pause game loop so hiders do not collide on construction */
        TestingApp.stopGameLoop();

        /* Spawn hider at (0, 0) */
        new Hider();

        Queue<Entity> collisionsCopy = new ConcurrentLinkedDeque<>();
        CountDownLatch latch = new CountDownLatch(1);

        /* Spawn another hider at (0, 0) */
        Hider hider = new Hider() {
            @Override
            public void collisionTick(Set<Entity> collisions) {
                collisionsCopy.addAll(collisions);
                latch.countDown();
            }
        };

        /*
         * Move hider away from first hider as to not prematurely cause
         * collision
         */
        hider.setPosition(-100, -100);

        /* Play game loop to update collisions */
        TestingApp.playGameLoop();

        /* Should not collide as was moved away from (0, 0) */
        Assertions.assertTrue(collisionsCopy.isEmpty());

        /* Cause collision due to movement */
        hider.setPosition(0, 0);

        try {
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        /* Collision should be detected */
        Assertions.assertFalse(
            collisionsCopy.isEmpty(),
            "No collisions detected"
        );

        /* Pause game loop to clear collisionsCopy */
        TestingApp.stopGameLoop();

        collisionsCopy.clear();

        /* Play game loop to ensure hider's illegal movement was reverted */
        TestingApp.playGameLoop();

        /* No collision should be detected as hider is moved back */
        Assertions.assertTrue(
            collisionsCopy.isEmpty(),
            "Collision detected"
        );
    }
}
