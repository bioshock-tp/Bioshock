package org.bioshock.engine.ai;

import org.bioshock.engine.entity.Components;
import org.bioshock.engine.entity.EntityManager;
import org.bioshock.engine.entity.Player;
import org.bioshock.engine.entity.Size;
import org.bioshock.engine.entity.SquareEntity;
import org.bioshock.engine.renderers.EnemyRenderer;
import org.bioshock.main.App;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Enemy extends SquareEntity {
    private SquareEntity target;
    private Swatter swatter;
    private Rectangle closeBox;
	
    public Enemy(Point3D p, Components com, Size s, int r, Color c, Player e) {
        super(p, com, s, r, c);

        target = e;
        
        movement.setSpeed(5);

        renderer = new EnemyRenderer();

        swatter = new Swatter(p, com, new Size(80,10), Color.BLACK, this);
    }

    public boolean canSee(SquareEntity enemy) {
    	Shape intersect = Shape.intersect(fov, enemy.getHitbox());
        return intersect.getBoundsInLocal().getWidth() != -1;
    }

    public void followPlayer() {
        if (
            EntityManager.isManaged(this, target, swatter) 
            && intersects(target, "swatter")
            && target instanceof Player
        ) {
            ((Player) target).setDead(true);
        }
        if (
            EntityManager.isManaged(this, target, swatter)
            && intersects(target, "close")
        ) {
            swatter.setShouldSwat(true);
        }
        if (
            EntityManager.isManaged(this, target)
            && intersects(target, "fov")
        ) {
            movement.move(target.getPosition().subtract(this.getPosition()));
        }
    }

    public boolean intersects(SquareEntity entity, String type) {
        Shape intersect;
        Rectangle entityHitbox = new Rectangle(
            entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight()
        );
        entityHitbox.getTransforms().add(entity.getRotation());

        switch(type){
            case "fov":
                intersect = Shape.intersect(fov, entity.getHitbox());
                break;

            case "close":
                Rectangle closeHitBox = new Rectangle(
                    (double) getX() - swatter.getWidth(),         // x
                    (double) getY() - swatter.getWidth(),         // y
                    (double) swatter.getWidth() * 2 + getWidth(), // width
                    swatter.getWidth()                            // height
                );
                closeHitBox.getTransforms().add(getRotation());
                intersect = Shape.intersect(closeHitBox, entityHitbox);
                break;

            case "swatter":
                Rectangle swatterHitbox = new Rectangle(
                    swatter.getX(), swatter.getY(),
                    swatter.getWidth(), swatter.getHeight()
                );
                swatterHitbox.getTransforms().add(swatter.getRotation());
                intersect = Shape.intersect(swatterHitbox, entityHitbox);
                break;
            default:
                App.logger.error("Invalid intersects type: {}", type);
                return false;
        }

        return intersect.getBoundsInLocal().getWidth() != -1;
    }
    
    @Override
	protected void tick(double timeDelta) {
    	followPlayer();
        setSwatterPos();
    	if(!swatter.shouldSwat()){
            setSwatterRot();
        }
	}

    public void setSwatterPos() {
        swatter.setPosition(
            getX() - swatter.getWidth(),
            getY() + getWidth() / 2 - swatter.getHeight() / 2
        );
    }
    
    public void setSwatterRot() {
        swatter.getMovement().setRotation(
            getRotation().getAngle(),
            new Point2D(getRotation().getPivotX(), getRotation().getPivotY())
        );
    }

	public int getRadius() {
		return (int) fov.getRadius();
	}

    public Swatter getSwatter() {
        return swatter;
    }

    public Rectangle getCloseBox() {
        return closeBox;
    }
}
