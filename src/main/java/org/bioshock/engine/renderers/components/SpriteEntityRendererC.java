package org.bioshock.engine.renderers.components;

import javafx.scene.paint.Color;
import org.bioshock.engine.components.RendererC;

public class SpriteEntityRendererC implements RendererC {
    private double z;

    public double getZ() {
        return this.z;
    }

    @Override
    public Color getColor() {
        return null;
    }

    public void setZ(double newZ) {
        this.z = newZ;
    }

    @Override
    public void setColor(Color newColor) {

    }

}