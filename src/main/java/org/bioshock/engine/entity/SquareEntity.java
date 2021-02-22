package org.bioshock.engine.entity;

import org.bioshock.engine.physics.Movement;
import org.bioshock.main.App;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public abstract class SquareEntity extends Entity {
    protected Size size;
    protected Rectangle hitbox;
    protected Paint colour;
    protected Rotate rotate = new Rotate();
    
    protected final Movement movement = new Movement(this);
    
    protected SquareEntity(Point3D pos, Size size, Color c) {
        super(pos);
        
        colour = c;

        this.size = size;

        hitbox = new Rectangle(
            pos.getX(), pos.getY(),
            size.getWidth(), size.getHeight()
        );
    }
	
    @Override
    public void setPosition(int x, int y) {
        setTranslateX(x);
        setTranslateY(y);
        if (hitbox != null) {
            hitbox.setTranslateX(x);
            hitbox.setTranslateY(y);
        }
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

    public Paint getColor() {
		return colour;
	}

	public Rectangle getHitbox() {
		return hitbox;
	}
 
    public Movement getMovement() {
		return movement;
	}
}
