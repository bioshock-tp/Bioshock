package org.bioshock.engine.ai;

import org.bioshock.engine.sprites.Player;
import org.bioshock.engine.sprites.SquareEntity;
import org.bioshock.render.components.PlayerRendererC;
import org.bioshock.transform.components.PlayerTransformC;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Enemy extends Player {
	private SquareEntity entityToFollow;
	
    public Enemy(PlayerTransformC transform, PlayerRendererC renderer, 
    		int x, int y, int w, int h, double r, Color c, double z, SquareEntity entityToFollow) {
        super(transform, renderer, x, y, w, h, r, c, z);
        this.entityToFollow = entityToFollow;
    }
    
    public Enemy(int x, int y, int w, int h, double r, Color c, double z, SquareEntity entityToFollow) {
    	this(new PlayerTransformC(), new PlayerRendererC(), x, y, w, h, r, c, z, entityToFollow);
    }

    public void followPlayer(){
        if(intersects(entityToFollow)){            
            movement.move(entityToFollow.transformC.getPosition().subtract(this.transformC.getPosition()));
        }
    }
    
    public boolean intersects(SquareEntity entity) {
    	double radius = super.transform.getRadius();
    	Circle fov = new Circle(getX() + super.transform.width/2, getY() + super.transform.height/2, radius);
    	Rectangle entityHitBox = new Rectangle(entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight());
    	
    	Shape intersect = Shape.intersect(fov, entityHitBox);
        if(intersect.getBoundsInLocal().getWidth() != -1){
        	return true;
        }
        return false;
    }
    
    @Override
	protected void tick(double timeDelta) {
    	followPlayer();
	}
}
