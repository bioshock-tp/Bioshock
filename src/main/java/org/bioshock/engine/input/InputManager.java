package org.bioshock.engine.input;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.bioshock.engine.core.ChatManager;
import org.bioshock.entities.EntityManager;
import org.bioshock.entities.LabelEntity;
import org.bioshock.main.App;
import org.bioshock.rendering.RenderManager;
import org.bioshock.scenes.SceneManager;

import javafx.geometry.Point3D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class InputManager {

    /** Maps a key press to it's desired action */
    private static Map<KeyCode, Runnable> keyPresses = new EnumMap<>(
        KeyCode.class
    );

    /** Maps a key release to it's desired action */
    private static Map<KeyCode, Runnable> keyReleases = new EnumMap<>(
        KeyCode.class
    );

    /** Maps WASD to if they've been pressed, help ignore repeated key presses */
    private static Map<KeyCode, Integer> wasd = new EnumMap<>(
        KeyCode.class
    );

    /** When true, will use debugging features */
    private static boolean debug = false;

    /** True if should listen to key presses/releases */
    private static boolean active = true;


    /** InputManager is a static class */
    private InputManager() {}


    /**
     * Calls {@link #changeScene()}
     * @see #changeScene()
     */
	public static void initialise() {
        initDebugPress();
		changeScene();
	}


    /**
     * Shows which of WASD is being pressed.
     * BLACK denotes the key is not currently pressed.
     * RED denotes the key is currently pressed.
     */
    private static void initDebugPress() {
        onPress(KeyCode.M, () -> {
            if (!debug) {
                debug = true;

                final Font font = new Font("arial", 20);

                LabelEntity w = new LabelEntity(
                    new Point3D(30, 30, 50),
                    "W",
                    font,
                    1,
                    Color.BLACK
                );

                LabelEntity a = new LabelEntity(
                    new Point3D(40, 30, 50),
                    "A",
                    font,
                    1,
                    Color.BLACK
                );

                LabelEntity s = new LabelEntity(
                    new Point3D(50, 30, 50),
                    "S",
                    font,
                    1,
                    Color.BLACK
                );

                LabelEntity d = new LabelEntity(
                    new Point3D(60, 30, 50),
                    "D",
                    font,
                    1,
                    Color.BLACK
                );

                EntityManager.registerAll(w, a, s, d);
                RenderManager.registerAll(List.of(w, a, s, d));

                keyPresses.replace(KeyCode.W, appendRunnable(
                    keyPresses.get(KeyCode.W),
                    () -> w.getRendererC().setColour(Color.RED)
                ));
                keyPresses.replace(KeyCode.A, appendRunnable(
                    keyPresses.get(KeyCode.A),
                    () -> a.getRendererC().setColour(Color.RED)
                ));
                keyPresses.replace(KeyCode.S, appendRunnable(
                    keyPresses.get(KeyCode.S),
                    () -> s.getRendererC().setColour(Color.RED)
                ));
                keyPresses.replace(KeyCode.D, appendRunnable(
                    keyPresses.get(KeyCode.D),
                    () -> d.getRendererC().setColour(Color.RED)
                ));

                keyReleases.replace(KeyCode.W, appendRunnable(
                    keyReleases.get(KeyCode.W),
                    () -> w.getRendererC().setColour(Color.BLACK)
                ));
                keyReleases.replace(KeyCode.A, appendRunnable(
                    keyReleases.get(KeyCode.A),
                    () -> a.getRendererC().setColour(Color.BLACK)
                ));
                keyReleases.replace(KeyCode.S, appendRunnable(
                    keyReleases.get(KeyCode.S),
                    () -> s.getRendererC().setColour(Color.BLACK)
                ));
                keyReleases.replace(KeyCode.D, appendRunnable(
                    keyReleases.get(KeyCode.D),
                    () -> d.getRendererC().setColour(Color.BLACK)
                ));
            }
        });
    }


    /**
     * Runs {@code target} before running {@code toApp}
     * @param target The {@link Runnable} to run first
     * @param toApp The {@link Runnable} to run second
     * @return A {@link Runnable} that runs both the provided
     * {@link Runnable Runnables}
     */
    private static Runnable appendRunnable(Runnable target, Runnable toApp) {
        return () -> {
            target.run();
            toApp.run();
        };
    }


    /** Adds adds the event listeners to the current scene */
    public static void changeScene() {
        SceneManager.getScene().setOnKeyPressed(e -> {
            Runnable runnable;
            if (
                (active || e.getCode() == KeyCode.ENTER)
                && (runnable = keyPresses.get(e.getCode())) != null
            ) {
                runnable.run();
            }
        });

        SceneManager.getScene().setOnKeyReleased(e -> {
            Runnable runnable;
            if (
                (active || e.getCode() == KeyCode.ENTER)
                && (runnable = keyReleases.get(e.getCode())) != null
            ) {
                runnable.run();
            }
        });
    }


    /**
     * Adds a key listener to the specified {@code KeyCode}. Runs the
     * {@code Runnable} when the associated key is pressed
     * @param keyCode The key pressed that should call {@code #runnable}
     * @param runnable A functional interface to run when the specified key is
     * pressed
     */
	public static void onPress(KeyCode keyCode, Runnable runnable) {
        if (keyPresses.putIfAbsent(keyCode, runnable) != null) {
            App.logger.error(
                "Tried to add listener to key: {}, but was already assigned",
                keyCode.getChar()
            );
        }
	}


    /**
     * Adds a key listener to the specified {@code KeyCode}. Runs
     * {@code runnable} when the associated key is released
     * @param keyCode The key pressed that should call {@code runnable}
     * @param runnable A functional interface to run when the specified key is
     * released
     */
    public static void onRelease(KeyCode keyCode, Runnable runnable) {
        if (keyReleases.putIfAbsent(keyCode, runnable) != null) {
            App.logger.error(
                "Tried to add listener to key: {}, but was already assigned",
                keyCode.getChar()
            );
        }
	}


    /**
     * Stops running the mapped {@link Runnable} (if any) when
     * {@link KeyCode KeyCode(s)} key is pressed
     * @param keyCode The {@code KeyCode} of the key to ignore
     * {@link KeyCode KeyCode(s)} of
     */
    public static void removeKeyListener(KeyCode keyCode) {
        keyPresses.remove(keyCode);
        keyReleases.remove(keyCode);
	}


    /**
     * Stops running the mapped {@link Runnable Runnable(s)} when any of the
     * {@link KeyCode KeyCode(s)} key is pressed
     * @param keyCodes The {@link KeyCode KeyCode(s)} of the key(s) to ignore
     * {@link javafx.scene.input.KeyEvent KeyEvent(s)} of
     */
    public static void removeKeyListeners(KeyCode... keyCodes) {
        Arrays.asList(keyCodes).forEach(InputManager::removeKeyListener);
	}


    /**
     * @param active True if key presses/releases should be listened to
     */
    public static void setActive(boolean active) {
        InputManager.active = active;
    }

    /**
     * Stops listening to key presses/releases
     */
    public static void stop() {
        keyPresses.clear();
        keyReleases.clear();
    }


    /**
     * Maps WASD keys to whether they are pressed, helps to ignore repeated key
     * presses
     */
    public static void initMovement() {
        InputManager.onPress(  KeyCode.W, () -> wasd.put(KeyCode.W, 1));
        InputManager.onPress(  KeyCode.A, () -> wasd.put(KeyCode.A, 1));
        InputManager.onPress(  KeyCode.S, () -> wasd.put(KeyCode.S, 1));
        InputManager.onPress(  KeyCode.D, () -> wasd.put(KeyCode.D, 1));

        InputManager.onRelease(KeyCode.W, () -> wasd.put(KeyCode.W, 0));
        InputManager.onRelease(KeyCode.A, () -> wasd.put(KeyCode.A, 0));
        InputManager.onRelease(KeyCode.S, () -> wasd.put(KeyCode.S, 0));
        InputManager.onRelease(KeyCode.D, () -> wasd.put(KeyCode.D, 0));
    }

    public static Map<KeyCode, Integer> getWasd() {
        return wasd;
    }
}
