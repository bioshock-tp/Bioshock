package org.bioshock.engine.input;

import org.bioshock.engine.utils.Point;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public interface IInputChecker {
	public boolean CheckKeyDown(KeyCode key);
	
	public boolean CheckKeyUp(KeyCode key);
	
	public boolean CheckMouseDown(MouseButton mouseButton);
	
	public boolean CheckMouseUp(MouseButton mouseButton);
	
	public Point CheckMousePoint();
	
	public Point CheckMouseDelta();
}
