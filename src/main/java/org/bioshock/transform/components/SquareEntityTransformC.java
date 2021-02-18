package org.bioshock.transform.components;

import org.bioshock.engine.entity.ITransformComponent;

import javafx.geometry.Point2D;

public class SquareEntityTransformC implements ITransformComponent{
	public double width;
    public double height;
    public double rotation = 0.0;
    private Point2D position = null;
	
	@Override
	public double getRotation() {
		return this.rotation;
	}

	@Override
	public void setRotation(double newRot) {
		this.rotation = newRot;
	}

	@Override
	public Point2D getPosition() {
		return this.position;
	}

	@Override
	public void setPosition(Point2D newPos) {
		this.position = newPos;
	}

}
