package org.bioshock.engine.input;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import org.bioshock.engine.scene.SceneManager;
import org.bioshock.main.App;

import javafx.scene.input.KeyCode;

public class InputManager {
    private static Map<KeyCode, Runnable> keyPresses =
        new EnumMap<>(KeyCode.class);

    private static Map<KeyCode, Runnable> keyReleases =
        new EnumMap<>(KeyCode.class);

    private InputManager() {}

	public static void initialise() {
		changeScene();
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
