package org.bioshock.engine.input;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

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
    private static Map<KeyCode, Runnable> keyPresses =
        new EnumMap<>(KeyCode.class);

    private static Map<KeyCode, Runnable> keyReleases =
        new EnumMap<>(KeyCode.class);

    private static boolean debug = false;

    private InputManager() {}

	public static void initialise() {
        initDebugPress();
		changeScene();
	}


    /**
     * Shows which of WASD is being pressed.
     * BLACK denotes the key is not currenlty pressed.
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
     * Runs {@code target} before runnning {@code toApp}
     * @param target The {@link Runnable} to run first
     * @param toApp The {@link Runnable} to run second
     * @return A {@link Runnable} that runs both the provided {@link Runnable Runnables}
     */
    private static Runnable appendRunnable(Runnable target, Runnable toApp) {
        return () -> {
            target.run();
            toApp.run();
        };
    }

    public static void changeScene() {
        SceneManager.getScene().setOnKeyPressed(e -> {
            Runnable runnable;
            if ((runnable = keyPresses.get(e.getCode())) != null) {
                runnable.run();
            }
        });

        SceneManager.getScene().setOnKeyReleased(e -> {
            Runnable runnable;
            if ((runnable = keyReleases.get(e.getCode())) != null) {
                runnable.run();
            }
        });
    }

	public static void onPress(KeyCode keyCode, Runnable runnable) {
        if (keyPresses.putIfAbsent(keyCode, runnable) != null) {
            App.logger.error(
                "Tried to add listener to key: {}, but was already assigned",
                keyCode.getChar()
            );
        }
	}

    public static void onRelease(KeyCode keyCode, Runnable runnable) {
        if (keyReleases.putIfAbsent(keyCode, runnable) != null) {
            App.logger.error(
                "Tried to add listener to key: {}, but was already assigned",
                keyCode.getChar()
            );
        }
	}

    public static void removeKeyListener(KeyCode keyCode) {
        keyPresses.remove(keyCode);
        keyReleases.remove(keyCode);
	}

    public static void removeKeyListeners(KeyCode... keyCodes) {
        Arrays.asList(keyCodes).forEach(InputManager::removeKeyListener);
	}
}
