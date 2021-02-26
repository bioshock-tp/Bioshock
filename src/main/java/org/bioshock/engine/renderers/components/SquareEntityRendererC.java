package org.bioshock.engine.renderers.components;

import org.bioshock.engine.entity.IRendererC;

import javafx.scene.paint.Color;

public class SquareEntityRendererC implements IRendererC{
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
