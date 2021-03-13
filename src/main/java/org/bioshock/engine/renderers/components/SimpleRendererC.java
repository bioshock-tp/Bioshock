package org.bioshock.engine.renderers.components;

import org.bioshock.engine.components.RendererC;

import javafx.scene.paint.Color;

public class SimpleRendererC implements RendererC {
	private double z;
	private Color color;

	public double getZ() {
		return this.z;
	}

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
