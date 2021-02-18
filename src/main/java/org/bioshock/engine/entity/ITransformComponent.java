package org.bioshock.engine.entity;

import javafx.geometry.Point2D;

public interface ITransformComponent {
	
	public double getRotation();
	public void setRotation(double newRot);
	
	public Point2D getPosition();
	public void setPosition(Point2D newPos);
}
