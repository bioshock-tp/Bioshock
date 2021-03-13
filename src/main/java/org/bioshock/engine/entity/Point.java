package org.bioshock.engine.entity;

import java.lang.reflect.Field;

import org.bioshock.main.App;

import javafx.geometry.Point2D;

public class Point extends Point2D {
    private Field xField;
    private Field yField;

    public Point(double x, double y) {
        super(x, y);
        try {
            xField = Point2D.class.getDeclaredField("x");
            yField = Point2D.class.getDeclaredField("y");

            xField.setAccessible(true);
            yField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            App.logger.error(e);
            App.exit(-1);
        }
    }

    /**
     * Changes this points x value
     * @param newX New value for x for this point
     */
    public void setX(double newX) {
        try {
            xField.set(this, newX);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            App.logger.error(e);
            App.exit(-1);
        }
    }

    /**
     * Changes this points y value
     * @param newY New value for y for this point
     */
    public void setY(double newY) {
        try {
            yField.set(this, newY);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            App.logger.error(e);
            App.exit(-1);
        }
    }

    @Override
    public boolean equals(Object b) { return super.equals(b); }

    @Override
    public int hashCode() { return super.hashCode(); }
}
