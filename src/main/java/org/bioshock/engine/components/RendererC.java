package org.bioshock.engine.components;

import javafx.scene.paint.Color;

public interface RendererC {
    public void setZ(double newY);
    public void setColor(Color newColor);
	public double getZ();
	public Color getColor();
}
