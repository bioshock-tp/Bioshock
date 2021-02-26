package org.bioshock.engine.entity;

import org.bioshock.engine.physics.Movement;
import org.bioshock.engine.renderers.components.SquareEntityRendererC;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public abstract class SquareEntity extends Entity {
    private SquareEntityRendererC sqRenC = new SquareEntityRendererC();
	public SquareEntityRendererC getSqrenc() {
		return sqRenC;
	}

	protected Size size;
    protected Rectangle hitbox;
    protected Circle fov;
    protected final Rotate rotate = new Rotate();
    
    protected final Movement movement = new Movement(this);
    
    protected SquareEntity(Point3D p, NetworkC com, Size s, int r, Color c) {
        super(p, com, new SquareEntityRendererC());
        sqRenC = (SquareEntityRendererC) super.renderC;
        sqRenC.setColor(c);
        
        size = s;

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
    
    public int getRadius() {
    	return (int) fov.getRadius();
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

	public Rectangle getHitbox() {
		return hitbox;
	}
 
    public Movement getMovement() {
		return movement;
	}
}
