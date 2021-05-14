package org.bioshock.engine.input;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.bioshock.main.TestingApp;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.robot.Robot;

public class InputManagerTest {
    @AfterAll
    public static void destroy() {
        TestingApp.showGame(false);
        TestingApp.playGameLoop();
    }

    @Test
    public void inputTest() {
        TestingApp.launchJavaFXThread();
        TestingApp.stopGameLoop();;

        InputManager.stop();

        boolean[] pressed = new boolean[1];
        CountDownLatch latch = new CountDownLatch(1);
        InputManager.onPress(KeyCode.W, () -> {
            pressed[0] = true;
            latch.countDown();
        });

        TestingApp.showGame(true);

        Platform.runLater(() -> new Robot().keyType(KeyCode.W));

        try {
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Assertions.assertTrue(pressed[0], "Key press not detected");
    }
}
