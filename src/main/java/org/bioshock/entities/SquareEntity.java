package org.bioshock.entities;

import org.bioshock.components.NetworkC;
import org.bioshock.physics.Movement;
import org.bioshock.rendering.renderers.components.RendererC;
import org.bioshock.utils.Size;
import org.checkerframework.checker.units.qual.radians;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

/**
 * A class to represent a generic square entity with a field of view
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
     * @param position The position of the top left of the square of the entity
     * @param nComponent The new network component
     * @param rComponent The new render component
     * @param size The size of the square of that makes up the entity
     * @param radius The radius of the FOV
     * @param colour The Colour of the squareEntity for rendering purposes
     * (not necessarily used but set one to avoid null pointers)
     */
    protected SquareEntity(
        Point3D position,
        NetworkC nComponent,
        RendererC rComponent,
        Size size,
        int radius,
        Color colour
    ) {
        super(
            position,
            new Rectangle(position.getX(), position.getY(), size.getWidth(), size.getHeight()),
            nComponent,
            rComponent
        );

        rendererC.setColour(colour);

        this.size = size;

        fov = new Circle(position.getX(), position.getY(), radius);

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
     * @param invisible True if this entity should be invisible
     */
    public void setInvisible(boolean invisible) { this.invisible = invisible; }


    /**
     * @param radius The new radius for the {@link #fov}
     */
    public void setRadius(double radius) {
        fov = new Circle(position.getX(), position.getY(), radius);
    }


    /**
     * @return the size of the square entity
     */
    public Size getSize() {
        return size;
    }


    /**
     * @return The radius of the current entity
     */
    public double getRadius() {
        return fov.getRadius();
    }


    /**
     * @return The rotation of the current entity
     */
    public Rotate getRotate() {
        return rotate;
    }


    /**
     * @return The movement component the entity
     */
    public Movement getMovement() {
        return movement;
    }


    /**
     * @return True if the entity is invisible
     */
    public boolean isInvisible() { return invisible; }
}
