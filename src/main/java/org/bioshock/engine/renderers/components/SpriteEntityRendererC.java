package org.bioshock.engine.renderers.components;

import javafx.scene.paint.Color;
import org.bioshock.engine.components.RendererC;

public class SpriteEntityRendererC implements RendererC {
    private double z;

    public void setZ(double newZ) {
        this.z = newZ;
    }

    @Override
    public void setColour(Color newColour) {
        /* Sprite has image not colour */
    }
    
    public double getZ() {
        return this.z;
    }

    @Override
    public Color getColour() {
        /* Sprite has image not colour */
        return null;
    }
}