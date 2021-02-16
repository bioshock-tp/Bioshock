package org.bioshock.engine.input;



import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public interface IInputChecker {
	public boolean CheckKeyDown(KeyCode key);
	
	public boolean CheckKeyUp(KeyCode key);
	
	public boolean CheckMouseDown(MouseButton mouseButton);
	
	public boolean CheckMouseUp(MouseButton mouseButton);
	
	public Point2D CheckMousePoint();
	
	public Point2D CheckMouseDelta();
}
