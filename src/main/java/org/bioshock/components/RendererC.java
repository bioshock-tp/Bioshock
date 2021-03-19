package org.bioshock.components;

import javafx.scene.paint.Color;

/**
 * Component for storing information regarding the rendering of an
 * {@code Entity}
 */
public interface RendererC {
    /**
     * @param newZ the new {@code Z} value of this {@code Entity}
     */
    public void setZ(double newZ);


    /**
     * @param newColour the new colour of this {@code Entity}
     */
    public void setColour(Color newColour);


    /**
     * The order in which this {@code Entity} should be rendered
     * @return {@code Z}
     */
	public double getZ();


    /**
     * @return the Colour of this {@code Entity}
     */
	public Color getColour();
}
