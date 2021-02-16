package org.bioshock.engine.input;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

public class DummyChecker implements IInputChecker{

	public DummyChecker(Stage primaryStage) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean CheckKeyDown(KeyCode key) {
		if (KeyCode.F11 == key) {
			System.out.println("c");
			return true;
		}
		return false;
	}

	@Override
	public boolean CheckKeyUp(KeyCode key) {
		if (KeyCode.F11 == key) {
			System.out.println("c");
			return true;
		}
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
	public Point2D CheckMousePoint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point2D CheckMouseDelta() {
		// TODO Auto-generated method stub
		return null;
	}

}
