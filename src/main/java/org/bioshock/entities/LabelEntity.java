package org.bioshock.entities;

import java.util.LinkedList;

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
    private LinkedList<Integer> msLen = new LinkedList<Integer>();
    
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

    public void setDisplay(boolean bl) {
        display = bl;
    }

    public Font getFont() {
        return font;
    }

    public StringBuilder getStringBuilder() {
        return sb;
    }
    
    public void setStringBuilder(StringBuilder sb) {
        this.sb = sb;
    }

    public void appendString(String s) {

        s = s.replace("\n", "");
        s = s + '\n';

        msLen.add(s.length());
        if(msLen.size() == 21){

            int len = msLen.getFirst();
            sb.delete(0, len);
            msLen.poll();
        }

        sb.append(s);
    }

    public LabelEntity(Point3D p, String initText, Font font, int charsPerLine, Color color) {
        super(p, new Rectangle(0,0), new NetworkC(false), new SimpleRendererC());
        this.font = font;
        this.charsPerLine = charsPerLine;
        sb.append(initText);
        renderer = LabelRenderer.class;
        alwaysRender = true;
        this.rendererC.setColour(color);
    }

    public LabelEntity(Point3D p, Font font, int charsPerLine, Color color) {
        this(p, "", font, charsPerLine, color);
    }

    @Override
    public String toString() {
        return String.format("LabelEntity [%s]", sb.toString());
    }
}
