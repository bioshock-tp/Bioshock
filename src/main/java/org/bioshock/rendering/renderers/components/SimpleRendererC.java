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

    @Override
    public double getZ() {
        return this.z;
    }

    @Override
    public void setZ(double newZ) {
        this.z = newZ;
    }

    @Override
    public Color getColour() {
        return this.colour;
    }

    @Override
    public void setColour(Color newColor) {
        this.colour = newColor;
    }
}
