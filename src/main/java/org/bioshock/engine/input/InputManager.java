package org.bioshock.engine.input;

import java.security.InvalidParameterException;
import java.util.EnumMap;
import java.util.Map;

import org.bioshock.engine.scene.SceneManager;
import org.bioshock.main.App;

import javafx.scene.input.KeyCode;

public class InputManager {
    private static Map<KeyCode, InputAction> keysAssigned = new EnumMap<>(KeyCode.class);

    private InputManager() {}

	public static void initialize() {
		changeScene();
	}

	public static void changeScene() {
        SceneManager.getScene().setOnKeyPressed(e -> {
            InputAction action;
            if ((action = keysAssigned.get(e.getCode())) != null) {
                App.logger.info("{} pressed", e.getCode().getChar());
                action.execute();
            }
        });

        //TODO: Insert mouse listener
    }

	public static void addKeyListener(KeyCode keyCode, InputAction action) {
        if (keysAssigned.putIfAbsent(keyCode, action) != null) {
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
        keysAssigned.remove(keyCode);
	}
}
