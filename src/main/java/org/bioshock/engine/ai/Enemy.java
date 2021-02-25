package org.bioshock.engine.ai;

import javafx.geometry.Point2D;
import javafx.scene.transform.Rotate;
import org.bioshock.engine.entity.EntityManager;
import org.bioshock.engine.entity.Player;
import org.bioshock.engine.entity.Size;
import org.bioshock.engine.entity.SquareEntity;
import org.bioshock.engine.renderers.EnemyRenderer;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import org.bioshock.engine.scene.SceneManager;


public class Enemy extends SquareEntity {
    private SquareEntity target;
    public Swatter swatter;
    public Rectangle closeBox;

    public Enemy(Point3D pos, Size s, int r, Color c, Player initialFollow) {
        super(pos, s, r, c);

        target = initialFollow;


        movement.setSpeed(5);

        renderer = new EnemyRenderer();

        swatter = new Swatter(pos, new Size(80,10),Color.BLACK, this);
    }

    public boolean canSee(SquareEntity enemy) {
    	Shape intersect = Shape.intersect(fov, enemy.getHitbox());
        return intersect.getBoundsInLocal().getWidth() != -1;
    }


    public void followPlayer() {
        if(EntityManager.areRendered(this, target, swatter) && intersects(target, "swatter")){
            if(target instanceof Player){
                ((Player) target).setDead(true);
            }
        }
        if(EntityManager.areRendered(this, target, swatter) && intersects(target, "close")){
            swatter.shouldSwat = true;
        }
        if (EntityManager.areRendered(this, target) && intersects(target, "fov")) {
            movement.move(target.getPosition().subtract(this.getPosition()));
        }
    }

    public void setSwatterPos(){
        swatter.setPosition((int) (getX() - swatter.getWidth()), (int) (getY() + getWidth()/2 - swatter.getHeight()/2));
    }
    public void setSwatterRot(){
        swatter.getMovement().setRotation(getRotation().getAngle(), new Point2D(getRotation().getPivotX(), getRotation().getPivotY()));
    }

    public boolean intersects(SquareEntity entity, String type) {
        Shape intersect;
        Rectangle entityHitbox = new Rectangle(entity.getX(),entity.getY(),entity.getWidth(),entity.getHeight());
        entityHitbox.getTransforms().add(entity.getRotation());

        switch(type){
            case "fov":
                intersect = Shape.intersect(fov, entity.getHitbox());
                break;

            case "close":
                Rectangle closeHitBox = new Rectangle(getX() - swatter.getWidth(),getY() - swatter.getWidth(), (swatter.getWidth()*2) + getWidth(), swatter.getWidth());
                closeHitBox.getTransforms().add(getRotation());
                intersect = Shape.intersect(closeHitBox, entityHitbox);
                break;

            case "swatter":
                Rectangle swatterHitbox = new Rectangle(swatter.getX(), swatter.getY(), swatter.getWidth(), swatter.getHeight());
                swatterHitbox.getTransforms().add(swatter.getRotation());
                intersect = Shape.intersect(swatterHitbox, entityHitbox);
                break;
            default:
                return false;
        }

        if(intersect.getBoundsInLocal().getWidth() != -1){
        	return true;
        }
        return false;
    }
    
    @Override
	protected void tick(double timeDelta) {
    	followPlayer();
        setSwatterPos();
    	if(!swatter.shouldSwat){
            setSwatterRot();
        }
	}

	public int getRadius() {
		return (int) fov.getRadius();
	}
}
