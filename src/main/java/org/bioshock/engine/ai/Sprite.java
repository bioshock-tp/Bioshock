package org.bioshock.engine.ai;

import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Sprite extends Parent {

    Rectangle spr;
    Circle fov;
    double r;
    double speed = 1;

    public Sprite(int x, int y, int w, int h, double r, Color c) {
        spr = new Rectangle(w, h, c);
        fov = new Circle(r);
        fov.setStroke(c);
        fov.setFill(Color.TRANSPARENT);

        this.r = r;
        // spr.relocate(x, y);
        // fov.relocate(x, y);

        getChildren().add(new StackPane(fov, spr));

        setCentreXY(x, y);
    }

    public Point2D getCentre() {
        return new Point2D(getTranslateX() + r, getTranslateY() + r);
    }

    public void setCentreXY(double x, double y) {
        setTranslateX(x - r);
        setTranslateY(y - r);
    }

    public Shape getSpr() {
        return spr;
    }
}
