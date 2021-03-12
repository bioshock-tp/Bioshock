package org.bioshock.engine.input;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import org.bioshock.engine.scene.SceneManager;
import org.bioshock.main.App;

import javafx.scene.input.KeyCode;

public class InputManager {
    private static Map<KeyCode, InputAction> keyPresses =
        new EnumMap<>(KeyCode.class);

    private static Map<KeyCode, InputAction> keyReleases =
        new EnumMap<>(KeyCode.class);

    private InputManager() {}

	public static void initialise() {
		changeScene();
	}

	public static void changeScene() {
        SceneManager.getScene().setOnKeyPressed(e -> {
            InputAction action;
            if ((action = keyPresses.get(e.getCode())) != null) {
                action.execute();
            }
        });

        SceneManager.getScene().setOnKeyReleased(e -> {
            InputAction action;
            if ((action = keyReleases.get(e.getCode())) != null) {
                action.execute();
            }
        });

        //TODO: Insert mouse listener
    }

	public static void onPress(KeyCode keyCode, InputAction action) {
        if (keyPresses.putIfAbsent(keyCode, action) != null) {
            try {
                throw new InvalidParameterException(String.format(
                    "Tried to add listener to key: %s, was already assigned",
                    keyCode.getChar()
                ));
            } catch (InvalidParameterException e) {
                App.logger.error("{} {}", e.getMessage(), keyCode.getChar());
            }
        }
	}

    public static void onRelease(KeyCode keyCode, InputAction action) {
        if (keyReleases.putIfAbsent(keyCode, action) != null) {
            try {
                throw new InvalidParameterException(String.format(
                    "Tried to add listener to key: %s, was already assigned",
                    keyCode.getChar()
                ));
            } catch (InvalidParameterException e) {
                App.logger.error(e.getMessage());
            }
        }
	}

    public static void onMouseClick(InputAction action) {
        //TODO
    }

    public static void removeKeyListener(KeyCode keyCode) {
        keyPresses.remove(keyCode);
        keyReleases.remove(keyCode);
	}

    public static void removeKeyListeners(KeyCode... keyCodes) {
        Arrays.asList(keyCodes).forEach(InputManager::removeKeyListener);
	}
}
