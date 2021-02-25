package org.bioshock.engine.entity;

public class Size {
    private double width;
    private double height;

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
