package org.bioshock.engine.ai;

import javafx.geometry.Point2D;
import javafx.scene.transform.Rotate;
import org.bioshock.engine.entity.GameEntityManager;
import org.bioshock.engine.rendering.RenderManager;
import org.bioshock.engine.sprites.Player;
import org.bioshock.engine.sprites.SquareEntity;
import org.bioshock.render.components.EnemyRendererC;
import org.bioshock.transform.components.EnemyTransformC;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Enemy extends Player {
	private SquareEntity entityToFollow;
	private Swatter swatter;
	
    public Enemy(EnemyTransformC transform, EnemyRendererC renderer,
                 int x, int y, int w, int h, double r, Color c, double z, SquareEntity entityToFollow) {
        super(transform, renderer, x, y, w, h, r, c, z);
        this.entityToFollow = entityToFollow;
        this.movement.speed = 5;

        swatter = new Swatter(x, y, 80,10,Color.BLACK,0.8, this);
        GameEntityManager.register(swatter);
        RenderManager.register(swatter);
    }
    
    public Enemy(int x, int y, int w, int h, double r, Color c, double z, SquareEntity entityToFollow) {
    	this(new EnemyTransformC(), new EnemyRendererC(), x, y, w, h, r, c, z, entityToFollow);
    }

    public void followPlayer(){
        if(intersects(entityToFollow, "swatter")){
            if(entityToFollow instanceof Player){
                ((Player) entityToFollow).setDead(true);
            }
        }
        if(intersects(entityToFollow, "close")){
            swatter.shouldSwat = true;
        }
        if(intersects(entityToFollow, "fov")){
            movement.move(entityToFollow.transformC.getPosition().subtract(this.transformC.getPosition()));
        }
    }

    public void setSwatterPos(){
        swatter.transform.setPosition(new Point2D(getX() - swatter.getWidth(), getY() + getWidth()/2 - swatter.getHeight()/2));
    }
    public void setSwatterRot(){
        swatter.transform.setRotation(transform.getRotation());
    }
    
    public boolean intersects(SquareEntity entity, String type) {
        Rectangle entityHitBox = new Rectangle(entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight());
        Shape intersect;


        switch(type){
            case "fov":
                double radius = super.transform.getRadius();
                Circle fov = new Circle(getX() + super.transform.width/2, getY() + super.transform.height/2, radius);
                intersect = Shape.intersect(fov, entityHitBox);
                break;

            case "close":
                Rectangle closeHitBox = new Rectangle(getX() - swatter.getWidth(),getY() - swatter.getWidth(), (swatter.getWidth()*2) + getWidth(), swatter.getWidth());
                Rotate r = new Rotate(transform.getRotation(), getX() + getWidth()/2, getY() + getHeight()/2);
                closeHitBox.getTransforms().add(r);
                intersect = Shape.intersect(closeHitBox, entityHitBox);
                break;

            case "swatter":
                Rectangle swatterHitBox = new Rectangle(swatter.getX(), swatter.getY(), swatter.getWidth(), swatter.getHeight());
                intersect = Shape.intersect(swatterHitBox, entityHitBox);
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
}
