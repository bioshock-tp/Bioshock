package org.bioshock.entities;

import org.bioshock.entities.players.Hider;
import org.bioshock.main.TestingApp;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class EntityManagerTest {
    @Test
    public void registerTest() {
        TestingApp.stopGameLoop();

        EntityManager.unregisterAll();
        Hider hider = new Hider();

        EntityManager.register(hider);

        Assertions.assertEquals(
            1,
            EntityManager.getEntityList().size(),
            "Incorrect amount of registered entities"
        );

        Assertions.assertEquals(
            1,
            EntityManager.getPlayers().size(),
            "Incorrect amount of registered players"
        );

        Assertions.assertEquals(
            hider,
            EntityManager.getPlayers().get(0),
            "Hider was not registered correctly"
        );

        Assertions.assertEquals(
            hider,
            EntityManager.getCurrentPlayer(),
            "Hider was not registered correctly"
        );

        TestingApp.playGameLoop();
    }
}
