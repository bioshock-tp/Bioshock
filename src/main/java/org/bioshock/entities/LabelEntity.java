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
    private String label;

    private Font font;
    private boolean display = true;
    private int charsPerLine = 50;
    private int lineSpacing = 5;

    private LinkedList<Integer> msLen = new LinkedList<>();

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

        label = initText;

        renderer = LabelRenderer.class;

        alwaysRender = true;

        rendererC.setColour(colour);
    }

    public LabelEntity(Point3D p, Font font, int charsPerLine, Color colour) {
        this(p, "", font, charsPerLine, colour);
    }


    @Override
    protected void tick(double timeDelta) {
        /* Entity does not change */
    }

    public void setLabel(String newLabel) {
        label = newLabel;
    }

    public void setDisplay(boolean bl) {
        display = bl;
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

    public String getString() {
        return label;
    }

    public void appendString(String s) {
        String string = s.replace("\n", "");
        string = string + '\n';

        msLen.add(string.length());

        StringBuilder sb = new StringBuilder(label);
        if (msLen.size() == 21) {
            int len = msLen.getFirst();
            sb.delete(0, len);
            msLen.poll();
        }

        sb.append(string);

        label = sb.toString();
    }


    @Override
    public String toString() {
        return String.format("LabelEntity [%s]", label);
    }
}
