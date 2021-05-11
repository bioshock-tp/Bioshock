package org.bioshock;

import java.util.concurrent.CountDownLatch;

import org.junit.BeforeClass;

import javafx.application.Platform;

/**
 * 
 * A class to extend if the test needs to test JavaFX components
 *
 */
public abstract class JavaFxTest {
    @BeforeClass
    /**
     * Call this on any test you need the JavaFX platform to be running on to test
     */
    public static void init() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // TODO: handle exception
        }
    }
    
    /**
     * Run the given function on the JavaFX thread 
     * @param function the function to run
     * @throws InterruptedException if the current thread is interruptedwhile waiting
     */
    public static void runOnAppThread(Runnable function) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            function.run();
            latch.countDown();
        });
        latch.await();
    }
}
