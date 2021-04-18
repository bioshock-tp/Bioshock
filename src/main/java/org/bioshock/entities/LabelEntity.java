package org.bioshock.entities;

import org.bioshock.components.NetworkC;
import org.bioshock.rendering.renderers.LabelRenderer;
import org.bioshock.rendering.renderers.components.SimpleRendererC;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class LabelEntity extends Entity{

    private StringBuilder sb = new StringBuilder();
    private Font font;
    private boolean display = true;
    private int charsPerLine = 50;
    private int lineSpacing = 5;

    public LabelEntity(
        Point3D point,
        String initText,
        Font font,
        int charsPerLine,
        Color colour
    ) {
        super(
            point,
            new Rectangle(),
            new NetworkC(false),
            new SimpleRendererC()
        );

        this.font = font;
        this.charsPerLine = charsPerLine;

        sb.append(initText);

        renderer = LabelRenderer.class;

        alwaysRender = true;

        rendererC.setColour(colour);
    }

    @Override
    protected void tick(double timeDelta) {
        /* Entity does not change */
    }

    public Color getColour() {
        return rendererC.getColour();
    }

    public int getLineSpacing() {
        return lineSpacing;
    }

    public int getCharsPerLine() {
        return charsPerLine;
    }

    public boolean isDisplayed() {
        return display;
    }

    public Font getFont() {
        return font;
    }

    public StringBuilder getStringBuilder() {
        return sb;
    }

    @Override
    public String toString() {
        return String.format("LabelEntity [%s]", sb.toString());
    }
}
