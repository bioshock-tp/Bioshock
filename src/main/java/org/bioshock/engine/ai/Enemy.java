package org.bioshock.engine.ai;

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

    public boolean touchSwatter(SquareEntity entity){
        Shape intersect = Shape.intersect(swatter.getHitbox(), entity.getHitbox());
        return intersect.getBoundsInLocal().getWidth() != -1;
    }

    public boolean touchClose(SquareEntity entity){
        Rectangle rec = getHitbox();
        rec.setTranslateX(getX() - swatter.getWidth());
        rec.setTranslateY(getY() - swatter.getWidth());
        rec.setWidth((swatter.getWidth()*2) + getWidth());
        rec.setHeight(swatter.getWidth());
        Shape intersect = Shape.intersect(rec, entity.getHitbox());
        return intersect.getBoundsInLocal().getWidth() != -1;
    }

    public void followPlayer() {
        if(EntityManager.areRendered(this, target, swatter) && touchSwatter(target)){
            if(target instanceof Player){
                ((Player) target).setDead(true);
            }
        }
        if(EntityManager.areRendered(this, target, swatter) && touchClose(target)){
            swatter.shouldSwat = true;
        }
        if (EntityManager.areRendered(this, target) && canSee(target)) {
            movement.move(target.getPosition().subtract(this.getPosition()));
        }
    }

    public void setSwatterPos(){
        swatter.setPosition((int) (getX() - swatter.getWidth()), (int) (getY() + getWidth()/2 - swatter.getHeight()/2));
    }
    public void setSwatterRot(){
        swatter.setRotation(getRotation());
    }

    public boolean intersects(SquareEntity entity, String type) {
        Shape intersect;


        switch(type){
            case "fov":
                intersect = Shape.intersect(fov, entity.getHitbox());
                break;

            case "close":
                Rectangle closeHitBox = new Rectangle(getX() - swatter.getWidth(),getY() - swatter.getWidth(), (swatter.getWidth()*2) + getWidth(), swatter.getWidth());
                Rotate r = new Rotate(getRotation().getAngle(), getX() + getWidth()/2, getY() + getHeight()/2);
                closeHitBox.getTransforms().add(r);
                intersect = Shape.intersect(closeHitBox, entity.getHitbox());
                break;

            case "swatter":
                intersect = Shape.intersect(swatter.getHitbox(), entity.getHitbox());
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
