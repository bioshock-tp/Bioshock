package org.bioshock.engine.entity;

import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.components.RendererC;
import org.bioshock.engine.physics.Movement;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public abstract class SquareEntity extends Entity {
    protected final Rotate rotate = new Rotate();

    protected final Movement movement = new Movement(this);

    protected Size size;
    protected Circle fov;

    private SquareEntity(Point3D p, Rectangle h, NetworkC nC, RendererC rC) {
        super(p, h, nC, rC);
    }

    protected SquareEntity(
        Point3D p,
        NetworkC nCom,
        RendererC rCom,
        Size s,
        int r,
        Color c
    ) {
        this(
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

    public void setSize(Size size) {
        this.size = size;
    }

    public Point getCentre() {
        return new Point(getX() + getWidth() / 2, getY() + getHeight() / 2);
    }

    public Size getSize() {
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
