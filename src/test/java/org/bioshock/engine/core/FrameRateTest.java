package org.bioshock.engine.core;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.bioshock.main.TestingApp;
import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;

public class FrameRateTest {
    // @After
    public void destroy() {
        TestingApp.playGameLoop();
    }


    // @Test
    public void frameRateTest() {
        TestingApp.launchJavaFXThread();
        TestingApp.stopGameLoop();

        final double LOGIC_RATE = 30;

        int N = 20;
        try {
            Field nField = FrameRate.class.getDeclaredField("N");
            nField.setAccessible(true);
            nField.set(null, N);
        } catch (
            NoSuchFieldException
            | SecurityException
            | IllegalArgumentException
            | IllegalAccessException e
        ) {
            N = 100;
        }

        AnimationTimer timer = new AnimationTimer() {
            long prev = 0;

            @Override
            public void handle(long now) {
                double tickDelta = (now - prev) / 1e9;
                /* Aims to update frame count 10 times per second */
                if (tickDelta >= 1 / LOGIC_RATE) {
                    FrameRate.tick(now);
                    prev = now;
                }
            }
        };

        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            timer.start();
            latch.countDown();
        });

        try {
            latch.await(5, TimeUnit.SECONDS);

            /* Sleep to let frames update */
            TimeUnit.SECONDS.sleep((long) (N / LOGIC_RATE + 6));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        int frameRate = Integer.parseInt(FrameRate.getLabel().getString());
        Assertions.assertAll(
            () -> Assertions.assertTrue(
                frameRate <= LOGIC_RATE + 5,
                "Frame rate was too large, was " + frameRate
            ),
            () -> Assertions.assertTrue(
                frameRate > LOGIC_RATE - 5,
                "Frame rate to small, was " + frameRate
            )
        );
        System.out.println(frameRate);
    }
}
