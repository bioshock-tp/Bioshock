package org.bioshock.utils;

/**
 * An immutable wrapper class that holds 2 doubles, used to represent the width
 * and height of an object. Is primarily for making function parameters more
 * readable
 */
public class Size {
    /**
     * The width of the size object
     */
    private double width;

    /**
     * The height of the object
     */
    private double height;


    /**
     * @param width The width of the size object
     * @param height The height of the object
     */
    public Size(double width, double height) {
        this.width = width;
        this.height = height;
    }


    /**
     * @return The width of the object
     */
    public double getWidth() {
        return width;
    }


    /**
     * @return The height of the object
     */
    public double getHeight() {
        return height;
    }
}
