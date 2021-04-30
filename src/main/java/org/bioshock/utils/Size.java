package org.bioshock.utils;

/**
 * 
 * A wrapper class that holds 2 doubles and is used to 
 * represent the height and width of a square and is used
 * to make functions more readable
 *
 */
public class Size {
    /**
     * The width the size represents
     */
    private double width;
    /**
     * The height the size represents
     */
    private double height;

    /**
     * Construct a new size
     * @param width
     * @param height
     */
    public Size(double width, double height) {
        this.width = width;
        this.height = height;
    }

    /**
     * 
     * @return The width of the size
     */
    public double getWidth() {
        return width;
    }

    /**
     * 
     * @return The height of the size
     */
    public double getHeight() {
        return height;
    }
}
