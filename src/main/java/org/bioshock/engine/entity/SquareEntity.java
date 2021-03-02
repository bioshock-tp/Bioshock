package org.bioshock.engine.entity;

import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.physics.Movement;
import org.bioshock.engine.renderers.components.SquareEntityRendererC;

import javafx.geometry.Point2D;
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

    private SquareEntity(Point3D p, Rectangle h, NetworkC com) {
        super(p, h, com, new SquareEntityRendererC());
    }

    protected SquareEntity(Point3D p, NetworkC com, Size s, int r, Color c) {
        this(
            p,
            new Rectangle(p.getX(), p.getY(), s.getWidth(), s.getHeight()),
            com
        );

        rendererC.setColor(c);

        size = s;

        fov = new Circle(p.getX(), p.getY(), r);

        setPosition(p);
    }

    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y);

        if (fov != null) {
            fov.setTranslateX(x);
            fov.setTranslateY(y);
        }
    }

    public void setSize(Size size) {
		this.size = size;
	}

    public Point2D getCentre() {
		return new Point2D(
            getX() + (double) getWidth() / 2,
            getY() + (double) getHeight() / 2
        );
	}

    public Size getSize() {
		return size;
	}

	public int getWidth() {
		return size.getWidth();
	}

	public int getHeight() {
		return size.getHeight();
	}

    public int getRadius() {
    	return (int) fov.getRadius();
    }

    public Rotate getRotation() {
        return rotate;
    }

    public Movement getMovement() {
		return movement;
	}
}
