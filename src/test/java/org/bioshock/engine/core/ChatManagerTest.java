package org.bioshock.engine.core;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.PrimitiveIterator.OfInt;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.bioshock.main.TestingApp;
import org.bioshock.scenes.MainGame;
import org.bioshock.scenes.SceneManager;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;
import javafx.scene.robot.Robot;

public class ChatManagerTest {
    // @Test
    public void sendShortMessage() {
        TestingApp.launchJavaFXThread();

        CountDownLatch setSceneLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            SceneManager.setScene(new MainGame());
            setSceneLatch.countDown();
        });

        try {
            setSceneLatch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        /* Stage must be shown for key event to be triggered */
        Assertions.assertTrue(
            TestingApp.showGame(true),
            "Failed to show stage"
        );

        CountDownLatch enterLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            new Robot().keyType(KeyCode.ENTER);
            enterLatch.countDown();
        });

        try {
            enterLatch.await(5, TimeUnit.SECONDS);

            /* Wait for key press event to trigger */
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Assertions.assertTrue(
            ChatManager.inChat(),
            "Not in chat after pressing enter"
        );

        CountDownLatch messageLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            Robot robot = new Robot();

            robot.keyType(KeyCode.H);
            robot.keyType(KeyCode.I);

            robot.keyType(KeyCode.ENTER);

            messageLatch.countDown();
        });

        try {
            messageLatch.await(5, TimeUnit.SECONDS);

            /* Wait for key press event to trigger */
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Assertions.assertFalse(
            ChatManager.inChat(),
            "Was in chat after second press of enter"
        );

        Field messageListF = null;

        try {
            messageListF = ChatManager.class.getDeclaredField("messageList");
        } catch (NoSuchFieldException | SecurityException e) {
            Assertions.fail("Reflection failed");
        }

        Assertions.assertNotNull(messageListF, "Reflection failed");

        Objects.requireNonNull(messageListF).setAccessible(true);

        List<?> rawMessageList = Collections.EMPTY_LIST;
        try {
            rawMessageList = (ObservableList<?>) messageListF.get(null);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            Assertions.fail("Reflection failed");
        }

        Assertions.assertFalse(
            rawMessageList.isEmpty(),
            "Message list was empty"
        );

        int index = rawMessageList.size() - 1;
        String latestMessage = (String) rawMessageList.get(index);
        Assertions.assertEquals(
            "Hider: hi",
            latestMessage,
            "Message was incorrect"
        );
    }



    // @Test
    public void sendLongMessageTest() {
        TestingApp.launchJavaFXThread();

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            stringBuilder.append("HELLOWORLD");
        }

        Assertions.assertTrue(
            stringBuilder.length() > 90,
            "Message to send was too short"
        );

        CountDownLatch setSceneLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            SceneManager.setScene(new MainGame());
            setSceneLatch.countDown();
        });

        try {
            setSceneLatch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        /* Stage must be shown for key event to be triggered */
        Assertions.assertTrue(
            TestingApp.showGame(true),
            "Failed to show stage"
        );

        CountDownLatch enterLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            new Robot().keyType(KeyCode.ENTER);
            enterLatch.countDown();
        });

        try {
            enterLatch.await(5, TimeUnit.SECONDS);

            /* Wait for key press event to trigger */
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Assertions.assertTrue(
            ChatManager.inChat(),
            "Not in chat after pressing enter"
        );

        CountDownLatch messageLatch = new CountDownLatch(1);

        OfInt iterator = stringBuilder.chars().iterator();
        // while (iterator.hasNext()) {
            String character = Character.toString(iterator.next());
            KeyCode keyCode = KeyCode.getKeyCode(character);
            Platform.runLater(() -> new Robot().keyType(keyCode));
            System.out.println(character);
        // }

        Platform.runLater(() -> {
            new Robot().keyType(KeyCode.ENTER);
            System.out.println("enter");
            messageLatch.countDown();
        });

        try {
            messageLatch.await(5, TimeUnit.SECONDS);

            /* Wait for key press event to trigger */
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Assertions.assertFalse(
            ChatManager.inChat(),
            "Was in chat after second press of enter"
        );

        Field messageListF = null;

        try {
            messageListF = ChatManager.class.getDeclaredField("messageList");
        } catch (NoSuchFieldException | SecurityException e) {
            Assertions.fail("Reflection failed");
        }

        Assertions.assertNotNull(messageListF, "Reflection failed");

        Objects.requireNonNull(messageListF).setAccessible(true);

        List<?> rawMessageList = Collections.EMPTY_LIST;
        try {
            rawMessageList = (ObservableList<?>) messageListF.get(null);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            Assertions.fail("Reflection failed");
        }

        Assertions.assertFalse(
            rawMessageList.isEmpty(),
            "Message list was empty"
        );

        int index = rawMessageList.size() - 1;
        String latestMessage = (String) rawMessageList.get(index);
        Assertions.assertTrue(
            latestMessage.length() <= 90,
            "Message length was more than 90"
        );
    }
}
