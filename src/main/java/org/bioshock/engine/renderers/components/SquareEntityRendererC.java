package org.bioshock.engine.renderers.components;

import org.bioshock.engine.components.RendererC;

import javafx.scene.paint.Color;

public class SquareEntityRendererC implements RendererC {
    /**
     * The order in which this {@code Entity} should be rendered
     */
	private double z;

    /**
     * The colour of this {@code Entity}
     */
	private Color colour;

	public double getZ() {
		return this.z;
	}

	public void setZ(double newZ) {
		this.z = newZ;
	}

	public Color getColour() {
		return this.colour;
	}

	public void setColour(Color newColor) {
		this.colour = newColor;
	}
}
