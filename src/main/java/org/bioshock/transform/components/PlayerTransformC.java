package org.bioshock.transform.components;

public class PlayerTransformC extends SquareEntityTransformC{
	private double radius;
	
	public void setRadius(double newRadius) {
		this.radius = newRadius;
	}
	
	public double getRadius() {
		return radius;
	}
}
