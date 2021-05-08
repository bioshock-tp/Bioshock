package org.bioshock.entities;

import org.bioshock.components.NetworkC;
import org.bioshock.physics.Movement;
import org.bioshock.rendering.renderers.components.RendererC;
import org.bioshock.utils.Point;
import org.bioshock.utils.Size;
import org.checkerframework.checker.units.qual.radians;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

/**
 *
 * A class to represent a generic square entity with a FOV (field of view) with a radius of r
 *
 */
public abstract class SquareEntity extends Entity {
    /**
     * The current rotation of the square entity
     */
    protected final Rotate rotate = new Rotate();
    /**
     * The movement class for this entity
     */
    protected final Movement movement = new Movement(this);
    /**
     * The size of the square which represents this entity
     */
    protected Size size;
    /**
     * The circle that represents the FOV of the entity
     */
    protected Circle fov;
    /**
     * If set to true this entity will not be chased by a seeker
     */
    protected boolean invisible = false;

    /**
     * The basic constructor for a square entity
     * @param p The position of the top left of the square of the entity
     * @param nCom The new network component
     * @param rCom The new render component
     * @param s The size of the square of that makes up the entity
     * @param r The radius of the FOV
     * @param c The Colour of the squareEntity for rendering purposes
     * (not necessarily used but set one to avoid null pointers)
     */
    protected SquareEntity(
        Point3D p,
        NetworkC nCom,
        RendererC rCom,
        Size s,
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

    /**
     * Set invisible to true or false
     * @param invisible
     */
    public void setInvisible(boolean invisible) { this.invisible = invisible; }


    /**
     * @param radius The new radius for the {@link #fov}
     */
    public void setRadius(double radius) {
        fov = new Circle(position.getX(), position.getY(), radius);
    }


    /**
     *
     * @return The centre of the square entity
     * i.e. new Point(getX() + getWidth() / 2, getY() + getHeight() / 2)
     */
    public Point getCentre() {
        return new Point(getX() + getWidth() / 2, getY() + getHeight() / 2);
    }

    /**
     * Getter
     * @return the size of the square entity
     */
    public Size getSize() {
        return size;
    }

    /**
     * Getter
     * @return The width of the entity
     */
    public double getWidth() {
        return size.getWidth();
    }

    /**
     * Getter
     * @return The height of the entity
     */
    public double getHeight() {
        return size.getHeight();
    }

    /**
     * @return The radius of the current entity
     */
    public double getRadius() {
        return fov.getRadius();
    }

    /**
     *
     * @return The rotation of the current entity
     */
    public Rotate getRotate() {
        return rotate;
    }

    /**
     *
     * @return The movement component the entity
     */
    public Movement getMovement() {
        return movement;
    }

    /**
     *
     * @return If the entity is invisible or not
     */
    public boolean isInvisible() { return invisible; }
}
