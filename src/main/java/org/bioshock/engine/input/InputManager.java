package org.bioshock.engine.input;

import java.security.InvalidParameterException;
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

	public static void initialize() {
		changeScene();
	}

	public static void changeScene() {
        SceneManager.getScene().setOnKeyPressed(e -> {
            InputAction action;
            if ((action = keyPresses.get(e.getCode())) != null) {
                App.logger.info("{} pressed", e.getCode().getChar());
                action.execute();
            }
        });

        SceneManager.getScene().setOnKeyReleased(e -> {
            InputAction action;
            if ((action = keyReleases.get(e.getCode())) != null) {
                App.logger.info("{} released", e.getCode().getChar());
                action.execute();
            }
        });

        //TODO: Insert mouse listener
    }

	public static void onPressListener(KeyCode keyCode, InputAction action) {
        if (keyPresses.putIfAbsent(keyCode, action) != null) {
            throw new InvalidParameterException(String.format(
                "Tried to add listener to key: %s, but was already assinged",
                keyCode.getChar()
            ));
        }
	}

    public static void onReleaseListener(KeyCode keyCode, InputAction action) {
        if (keyReleases.putIfAbsent(keyCode, action) != null) {
            throw new InvalidParameterException(String.format(
                "Tried to add listener to key: %s, but was already assinged",
                keyCode.getChar()
            ));
        }
	}

    public static void addMouseClickListener(InputAction action) {

        //TODO: Insert mouse listener
    }

    public static void removeKeyListener(KeyCode keyCode) {
        keyPresses.remove(keyCode);
	}
}
