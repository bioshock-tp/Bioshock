package org.bioshock.rendering.renderers.components;

import org.bioshock.components.RendererC;

import javafx.scene.paint.Color;



public class SimpleRendererC implements RendererC {
    /**
     * The order in which this {@code Entity} should be rendered
     */
    private double z;

    /**
     * The colour of this {@code Entity}
     */
    private Color colour;

    public void setZ(double newZ) {
        this.z = newZ;
    }

    public double getZ() {
        return z;
    }

    public void setColour(Color newColour) {
        colour = newColour;
    }

    public Color getColour() {
        return colour;
    }
}
