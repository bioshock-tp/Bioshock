package org.bioshock.engine.renderers.components;

import javafx.scene.paint.Color;

public class PlayerRendererC extends SimpleRendererC {
    private Color originalColour;

    public void setOriginalColour(Color originalColour) {
        this.originalColour = originalColour;
    }

    public void revertColour() {
        setColor(originalColour);
    }
}
