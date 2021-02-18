package org.bioshock.engine.entity;

import javafx.geometry.Point2D;

public interface ITransformComponent {
	
	public double getRotation();
	public void setRotation();
	
	public Point2D getPosition();
	public Point2D setPosition();
}
