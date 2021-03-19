package org.bioshock.entities;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import org.bioshock.components.NetworkC;
import org.bioshock.physics.Movement;
import org.bioshock.rendering.renderers.components.SpriteEntityRendererC;

public abstract class SpriteEntity extends Entity {
    protected Circle fov;

    protected final Movement movement = new Movement(this);

    protected SpriteEntity(Point3D p, Rectangle h, NetworkC com, int r) {
        super(p, h, com, new SpriteEntityRendererC());

        fov = new Circle(p.getX(), p.getY(), r);
        fov.setTranslateX(p.getX());
        fov.setTranslateY(p.getY());

        hitbox = new Rectangle(
                p.getX(), p.getY(),
                32, 32
        );
        hitbox.setTranslateX(p.getX());
        hitbox.setTranslateY(p.getY());
        hitbox.setFill(Color.TRANSPARENT);
    }

    @Override
    public void setPosition(double x, double y) {
        super.setPosition(x, y);

        if (fov != null) {
            fov.setTranslateX(x);
            fov.setTranslateY(y);
        }
    }

    public int getRadius() {
        return (int) fov.getRadius();
    }

    public Movement getMovement() {
        return movement;
    }
}