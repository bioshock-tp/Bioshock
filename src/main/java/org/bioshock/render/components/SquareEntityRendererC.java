package org.bioshock.render.components;

import org.bioshock.engine.entity.IRendererComponent;

import javafx.scene.paint.Color;

public class SquareEntityRendererC implements IRendererComponent{
	private double z;
	private Color color;
	
	@Override
	public double getZ() {
		return this.z;
	}
	
	@Override
	public void setZ(double newZ) {
		this.z = newZ;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public void setColor(Color newColor) {
		this.color = newColor;
	}
}
