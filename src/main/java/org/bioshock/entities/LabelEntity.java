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
    private Color color;

    public Color getColor() {
        return color;
    }

    public int getLineSpacing() {
        return lineSpacing;
    }

    public int getCharsPerLine() {
        return charsPerLine;
    }

    public boolean isDisplay() {
        return display;
    }

    public Font getFont() {
        return font;
    }

    public StringBuilder getStringBuilder() {
        return sb;
    }

    public LabelEntity(Point3D p, String initText, Font font, int charsPerLine, Color color) {
        super(p, new Rectangle(0,0), new NetworkC(false), new SimpleRendererC());
        this.font = font;
        this.charsPerLine = charsPerLine;
        sb.append(initText);
        renderer = LabelRenderer.class;
        alwaysRender = true;
        this.color = color;
    }

    @Override
    protected void tick(double timeDelta) {
        // TODO Auto-generated method stub
        
    }

}
