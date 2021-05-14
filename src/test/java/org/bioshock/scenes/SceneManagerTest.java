package org.bioshock.scenes;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.bioshock.main.TestingApp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import javafx.application.Platform;

public class SceneManagerTest {
    @Test
    public void setSceneTest() {
        TestingApp.launchJavaFXThread();

        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            SceneManager.setScene(new LoadingScreen("Test", () -> {}));
            latch.countDown();
        });

        try {
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Assertions.assertTrue(
            SceneManager.getScene() instanceof LoadingScreen,
            "Scene was not set correctly"
        );
    }
}
