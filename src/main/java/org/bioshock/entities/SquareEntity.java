package org.bioshock.entities;

import org.bioshock.components.NetworkC;
import org.bioshock.components.RendererC;
import org.bioshock.physics.Movement;
import org.bioshock.utils.Point;
import org.bioshock.utils.SizeD;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public abstract class SquareEntity extends Entity {
    protected final Rotate rotate = new Rotate();

    protected final Movement movement = new Movement(this);

    protected SizeD size;
    protected Circle fov;

    protected SquareEntity(
        Point3D p,
        NetworkC nCom,
        RendererC rCom,
        SizeD s,
        int r,
        Color c
    ) {
        super(
            p,
            new Rectangle(p.getX(), p.getY(), s.getWidth(), s.getHeight()),
            nCom,
            rCom
        );

        rendererC.setColour(c);

        size = s;

        fov = new Circle(p.getX(), p.getY(), r);

        setPosition(position);
    }

    @Override
    public void setPosition(double x, double y) {
        super.setPosition(x, y);
        x = (int) x;
        y = (int) y;

        if (fov != null) {
            fov.setTranslateX(x);
            fov.setTranslateY(y);
        }
    }

    public void setSize(SizeD size) {
        this.size = size;
    }

    public Point getCentre() {
        return new Point(getX() + getWidth() / 2, getY() + getHeight() / 2);
    }

    public SizeD getSize() {
        return size;
    }

    public double getWidth() {
        return size.getWidth();
    }

    public double getHeight() {
        return size.getHeight();
    }

    public double getRadius() {
        return fov.getRadius();
    }

    public Rotate getRotate() {
        return rotate;
    }

    public Movement getMovement() {
        return movement;
    }
}
