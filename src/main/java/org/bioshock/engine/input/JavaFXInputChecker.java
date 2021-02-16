package org.bioshock.engine.input;

import org.bioshock.engine.utils.Point;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

public class JavaFXInputChecker implements IInputChecker{

	public JavaFXInputChecker(Stage primaryStage) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean CheckKeyDown(KeyCode key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean CheckKeyUp(KeyCode key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean CheckMouseDown(MouseButton mouseButton) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean CheckMouseUp(MouseButton mouseButton) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Point CheckMousePoint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point CheckMouseDelta() {
		// TODO Auto-generated method stub
		return null;
	}

}
