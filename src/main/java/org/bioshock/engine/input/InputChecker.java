package org.bioshock.engine.input;



import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public final class InputChecker {
	private static IInputChecker inputChecker;

	public static void initialize(IInputChecker checker) {
		inputChecker = checker;
	}
	
	public static boolean checkKeyDown(KeyCode key) {
		return inputChecker.CheckKeyDown(key);
	}
	
	public static boolean checkKeyUp(KeyCode key) {
		return inputChecker.CheckKeyUp(key);
	}
	
	public static boolean checkMouseDown(MouseButton mouseButton) {
		return inputChecker.CheckMouseDown(mouseButton);
	}
	
	public static boolean checkMouseUp(MouseButton mouseButton) {
		return inputChecker.CheckMouseUp(mouseButton);
	}
	
	public static Point2D checkMousePoint() {
		return inputChecker.CheckMousePoint();
	}
	
	public static Point2D checkMouseDelta() {
		return inputChecker.CheckMouseDelta();
	}
}
