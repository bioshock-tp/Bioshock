package org.bioshock.main;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.bioshock.gui.MainController;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import javafx.application.Application;

public class AppTest {
    // @Test
    public void mainTest() {
        CountDownLatch latch = new CountDownLatch(1);

        new Thread(() -> {
            Application.launch(App.class);
            latch.countDown();
        }).start();

        try {
			latch.await(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
            Thread.currentThread().interrupt();
		}

        Assertions.assertEquals(
            1,
            latch.getCount(),
            "Latch count is zero, JavaFX thread closed prematurely"
        );

        Object fxmlController = App.getFXMLController();
        Assertions.assertNotNull(
            fxmlController,
            "FXML GUI not initialised"
        );

        Assertions.assertTrue(
            fxmlController instanceof MainController,
            "FXML Controller was not initial GUI instance controller"
        );
    }
}
