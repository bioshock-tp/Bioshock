package org.bioshock.engine.sprites;

import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public abstract class Sprite extends Parent {
    protected Rectangle spr;
    protected int x;
    protected int y;
    protected int w;
    protected int h;
    protected Color c;
    protected Rectangle hitbox;

    protected Sprite(int x, int y, int w, int h, Color c) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.c = c;

        spr = new Rectangle(w, h, c);

        hitbox = new Rectangle((double) w + 1, (double) h + 1);

        setCentreXY(x, y);
    }

    protected void setCentreXY(double x, double y) {
            setTranslateX(x);
            setTranslateY(y);
    }

    public Point2D getCentre() { return new Point2D(x, y); }

    public Shape getSpr() {
        return spr;
    }

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}
