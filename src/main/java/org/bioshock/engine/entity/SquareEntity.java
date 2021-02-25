package org.bioshock.engine.entity;

import org.bioshock.engine.physics.Movement;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public abstract class SquareEntity extends Entity {
    protected Size size;
    protected Color colour;
    protected Rectangle hitbox;
    protected Circle fov;
    protected final Rotate rotate = new Rotate();
    
    protected final Movement movement = new Movement(this);
    
    protected SquareEntity(Point3D p, Components com, Size s, int r, Color c) {
        super(p, com);
        size = s;
        colour = c;

        fov = new Circle(p.getX(), p.getY(), r);
        fov.setTranslateX(p.getX());
        fov.setTranslateY(p.getY());

        hitbox = new Rectangle(
            p.getX(), p.getY(),
            s.getWidth(), s.getHeight()
        );
        hitbox.setTranslateX(p.getX());
        hitbox.setTranslateY(p.getY());
        hitbox.setFill(Color.TRANSPARENT);
    }
	
    @Override
    public void setPosition(int x, int y) {
        setTranslateX(x);
        setTranslateY(y);
        
        if (hitbox != null) {
            hitbox.setTranslateX(x);
            hitbox.setTranslateY(y);
        }

        if (fov != null) {
            fov.setTranslateX(x);
            fov.setTranslateY(y);
        }
    }

    public Point2D getCentre() {
		return new Point2D(
            getX() + (double) getWidth() / 2,
            getY() + (double) getHeight() / 2
        );
	}

	public int getWidth() {
		return size.getWidth();
	}
	
	public int getHeight() {
		return size.getHeight();
	}

    public Rotate getRotation() {
        return rotate;
    }

    public Color getColor() {
		return colour;
	}

	public Rectangle getHitbox() {
		return hitbox;
	}
 
    public Movement getMovement() {
		return movement;
	}
}
