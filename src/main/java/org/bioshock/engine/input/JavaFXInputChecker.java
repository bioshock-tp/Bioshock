package org.bioshock.engine.input;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class JavaFXInputChecker implements IInputChecker{

	private boolean keyAdded = false;
	private boolean mouseAdded = false;
	private ArrayList<KeyCode> keysDown = new ArrayList<KeyCode>();
	private ArrayList<KeyCode> keysUp = new ArrayList<KeyCode>();
	private ArrayList<MouseButton> mouseDown = new ArrayList<MouseButton>();
	private ArrayList<MouseButton> mouseUp = new ArrayList<MouseButton>();
	private Point2D mousePos = null;
	private Point2D lastMousePos = null;
	
	public JavaFXInputChecker(Stage stage) {
		stage.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
			keysDown.add(event.getCode());
		});
		stage.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
			keysDown.remove(event.getCode());
			keysUp.add(event.getCode());
			keyAdded = true;
		});
		stage.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
			mouseDown.add(event.getButton());
		});
		stage.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
			mouseDown.remove(event.getButton());
			mouseUp.add(event.getButton());
			mouseAdded = true;
		});
		stage.addEventHandler(MouseEvent.MOUSE_MOVED, (MouseEvent event) -> {
			lastMousePos = mousePos;
			mousePos = new Point2D((float) event.getSceneX(),(float) event.getSceneY());
		});
		inputCheckTick.start();
	}
	
	AnimationTimer inputCheckTick = new AnimationTimer() {
		
		@Override
		public void handle(long now) {
			if (keyAdded) {
				keyAdded = false;
			}
			else {
				//System.out.println("Clear Keys Up");
				keysUp.clear();
			}
			if (mouseAdded) {
				mouseAdded = false;
			}
			else {
				mouseUp.clear();
			}			
		}
	}; 
	
	@Override
	public boolean CheckKeyDown(KeyCode key) {
		if (keysDown.contains(key)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean CheckKeyUp(KeyCode key) {
		if (keysUp.contains(key)) {
			System.out.println("Array List Length: " + keysUp.size());
			System.out.println("Check Key Up");
			return true;
		}
		return false;
	}

	@Override
	public boolean CheckMouseDown(MouseButton mouseButton) {
		if (mouseDown.contains(mouseButton)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean CheckMouseUp(MouseButton mouseButton) {
		if (mouseUp.contains(mouseButton)) {
			return true;
		}
		return false;
	}

	@Override
	public Point2D CheckMousePoint() {
		return mousePos;
	}

	@Override
	public Point2D CheckMouseDelta() {
		return mousePos.subtract(lastMousePos);
	}

}
