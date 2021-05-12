package org.bioshock.utils;

import java.lang.reflect.Field;

import org.bioshock.main.App;

import javafx.geometry.Point2D;

/**
 * A mutable version of {@link Point2D}
 */
public class Point extends Point2D {
    /**
     * The {@code x} field of {@code this} instance of {@link Point2D}
     */
    private Field xField;

    /**
     * The {@code y} field of {@code this} instance of {@link Point2D}
     */
    private Field yField;


    /**
     * Constructs an instance of {@link Point2D}, and attempts to make the x
     * and y values accessible through {@code Reflection}
     * @param x The initial x value
     * @param y The initial y value
     */
    public Point(double x, double y) {
        super(x, y);
        try {
            xField = Point2D.class.getDeclaredField("x");
            yField = Point2D.class.getDeclaredField("y");

            xField.setAccessible(true);
            yField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            App.logger.fatal(
                "Point2D has been refactored, Point not initialised"
            );
        }
    }


    /**
     * Sets the value of the {@code x} field of this instance's {@link Point2D}
     * @param x New value for x for this point
     */
    public void setX(double x) {
        try {
            xField.set(this, x);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            App.logger.error("Could not change x value of Point");
        }
    }


    /**
     * Sets the value of the {@code y} field of this instance's {@link Point2D}
     * @param y New value for y for this point
     */
    public void setY(double y) {
        try {
            yField.set(this, y);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            App.logger.error("Could not change y value of Point");
        }
    }


    @Override
    public boolean equals(Object b) { return super.equals(b); }


    @Override
    public int hashCode() { return super.hashCode(); }
}
