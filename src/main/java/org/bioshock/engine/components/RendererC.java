package org.bioshock.engine.components;

import javafx.scene.paint.Color;

public interface RendererC {
    public void setZ(double newZ);
    public void setColour(Color newColour);
	public double getZ();
	public Color getColour();
}
