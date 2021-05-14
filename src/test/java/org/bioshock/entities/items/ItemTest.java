package org.bioshock.entities.items;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.bioshock.components.NetworkC;
import org.bioshock.entities.Entity;
import org.bioshock.entities.EntityManager;
import org.bioshock.entities.players.Hider;
import org.bioshock.main.TestingApp;
import org.bioshock.utils.Size;
import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class ItemTest {
    @After
    public void destroyed() {
        TestingApp.showGame(false);
    }

    @Test
    public void collectionTest() {
        TestingApp.launchJavaFXThread();
        TestingApp.stopGameLoop();

        CountDownLatch latch = new CountDownLatch(1);
        Set<Entity> collisionsCopy = new HashSet<>();
        Item item = new Item(
            Item.spawn(0),
            new Size(10, 10),
            new NetworkC(false),
            ""
        ) {
            @Override
            public void collisionTick(Set<Entity> collisions) {
                collisionsCopy.addAll(collisions);
                latch.countDown();

                super.collisionTick(collisions);
            }


            @Override
            protected void apply(Hider hider) {}


            @Override
            protected void playCollectSound() {}
        };

        item.setPosition(0, 0);

        /* Hitboxes are destroyed for items with invalid image paths */
        item.getHitbox().setWidth(10);
        item.getHitbox().setHeight(10);


        /* Spawn hider on top of item */
        Hider hider = new Hider();

        EntityManager.registerAll(item, hider);

        /* Stage must be visible for intersections to work */
        Assertions.assertTrue(
            TestingApp.showGame(true),
            "Stage could not be shown"
        );

        TestingApp.playGameLoop();

        try {
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Assertions.assertFalse(
            collisionsCopy.isEmpty(),
            "No collision detected"
        );

        Assertions.assertEquals(
            1,
            collisionsCopy.size(),
            "More than one collision detected"
        );

        Assertions.assertTrue(
            collisionsCopy.contains(hider),
            "Hider collision was not detected"
        );

        Assertions.assertFalse(
            item.isEnabled(),
            "Item was not collected"
        );
    }
}
