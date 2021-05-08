package org.bioshock.entities;

import java.util.LinkedList;

import org.bioshock.components.NetworkC;
import org.bioshock.rendering.renderers.LabelRenderer;
import org.bioshock.rendering.renderers.components.SimpleRendererC;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 *
 * An entity used to display text on screen
 *
 */
public class LabelEntity extends Entity {

    StringBuilder sb = new StringBuilder();

	/**
	 * The string to display on screen
	 */
    private String label;
    /**
     * The font of the label
     */
    private Font font;
    /**
     * A boolean to represent whether to display the label or not
     */
    private boolean display = true;
    /**
     * The number of characters before wrapping the text to the next line
     */
    private int charsPerLine = 50;
    /**
     * The amount of space between each line of text
     */
    private int lineSpacing = 5;
    /**
     * The length of the most recently added messages in a list
     */
    private LinkedList<Integer> msLen = new LinkedList<>();
    /**
     * The number of messages to store in the label
     */
    private int numMessages = 20;

    /**
     * Create a new label entity
     * @param point The position of the top left of the label on the screen
     * @param initText The initial text of the label
     * @param font The font of the label
     * @param charsPerLine The number of characters per line
     * @param colour The colour of the text
     */
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

        sb.append(initText);

        renderer = LabelRenderer.class;

        alwaysRender = true;

        rendererC.setColour(colour);
    }

    /**
     *
     * @param p The position of the top left of the label on the screen
     * @param font The font of the label
     * @param charsPerLine The number of characters per line
     * @param colour The colour of the text
     */
    public LabelEntity(Point3D p, Font font, int charsPerLine, Color colour) {
        this(p, "", font, charsPerLine, colour);
    }


    @Override
    protected void tick(double timeDelta) {
        /* Entity does not change */
    }

    /**
     * Set the label text to the new label
     * @param newLabel
     */
    public void setLabel(String newLabel) {
        label = newLabel;
        sb = new StringBuilder(label);
    }

    public void setStringBuilder(StringBuilder sb) {

        this.sb = sb;
        label = sb.toString();
    }

    /**
     * Set whether to display the label or not
     * @param bl
     */
    public void setDisplay(boolean bl) {
        display = bl;
    }

    /**
     *
     * @return The color of the label text
     */
    public Color getColour() {
        return rendererC.getColour();
    }

    /**
     *
     * @return The line spacing
     */
    public int getLineSpacing() {
        return lineSpacing;
    }

    /**
     *
     * @return The number of characters per line
     */
    public int getCharsPerLine() {
        return charsPerLine;
    }

    /**
     *
     * @return Whether the label is displayed or not
     */
    public boolean isDisplayed() {
        return display;
    }

    /**
     *
     * @return the font of the label
     */
    public Font getFont() {
        return font;
    }

    /**
     *
     * @return The current string to be displayed
     */
    public String getString() {
        return label;
    }

    /**
     * Appends the given string to the label with a newline added to the end
     *
     * If there have been more than numMessages messages the oldest
     * message gets removed from the label
     * @param s
     */
    public void appendString(String s) {
        String string = s.replace("\n", "");
        string = string + '\n';

        msLen.add(string.length());

        sb = new StringBuilder(label);

        if (msLen.size() == numMessages + 1) {
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
