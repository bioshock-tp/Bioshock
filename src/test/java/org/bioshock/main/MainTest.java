package org.bioshock.main;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class MainTest {
    @Test
    public void mainTest() {
        CountDownLatch latch = new CountDownLatch(1);

        Thread thread = new Thread(() -> {
            Main.main(new String[0]);
            latch.countDown();
        });
        thread.start();

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
    }
}
