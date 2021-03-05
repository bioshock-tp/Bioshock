package org.bioshock.engine.entity;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.physics.Movement;
import org.bioshock.engine.renderers.components.SpriteEntityRendererC;

public abstract class SpriteEntity extends Entity {
//	protected Size size;
	protected Rectangle hitbox;
    protected Circle fov;
    private static final Image spriteImage = SpriteAnimator.getImage();
    ImageView spriteImageView = new ImageView(spriteImage);

    protected final Movement movement = new Movement(this);

    protected SpriteEntity(Point3D p, NetworkC com, int r) {
        super(p, com, new SpriteEntityRendererC());
        //rendererC.setColor(c);

        //spriteImageView = new ImageView(spriteImage);


        fov = new Circle(p.getX(), p.getY(), r);
        fov.setTranslateX(p.getX());
        fov.setTranslateY(p.getY());

        hitbox = new Rectangle(
            p.getX(), p.getY(),
            getWidth(), getHeight()
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

//    public void setSize(Size size) {
//		this.size = size;
//	}

    public Point2D getCentre() {
		return new Point2D(
            getX() + (double) getWidth() / 2,
            getY() + (double) getHeight() / 2
        );
	}

//    public Size getSize() {
//		return size;
//	}
//
	public int getWidth() {
		return (int) spriteImage.getWidth();
	}

    public int getHeight() {
        return (int) spriteImage.getHeight();
    }
//
//	public int getHeight() {
//		return size.getHeight();
//	}

    public int getRadius() {
    	return (int) fov.getRadius();
    }

//    public Rotate getRotation() {
//        return rotate;
//    }

	public Rectangle getHitbox() {
		return hitbox;
	}

    public Movement getMovement() {
		return movement;
	}

	public Image getImage() {
        return spriteImage;
    }

    public ImageView getSpriteImageView() {
        return spriteImageView;
    }
}
