package org.bioshock.engine.physics;

import org.bioshock.engine.input.InputChecker;
import org.bioshock.engine.sprites.SquareEntity;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

public class Movement {
    public static final double SPEED = 10;

    private SquareEntity entity;

    public Movement(SquareEntity sprite) {
        this.entity = sprite;
    }
    
    public void tick(double timeDelta) {
    	Point2D inputMovement = checkMove();
    	
    	move(inputMovement);
    }

    public void move(Point2D trans) {
        //App.logger.debug("Translation ({}, {})", trans.getX(), trans.getY());

        //if (trans.getX() == 0 && trans.getY() == 0) 
            //App.logger.error("Empty movement call");

        Point2D target = trans.add(entity.transformC.getPosition());
        
        update(target);
    }

    private void update(Point2D target) {
    	double x = entity.transformC.getPosition().getX();
        double y = entity.transformC.getPosition().getY();
        //App.logger.debug("Current {} {} Target {} {}", x, y, target.getX(), target.getY());

        // Sprite[] sprites = App.getSprites();

        // for (Sprite spr : )
              		
        if (x != target.getX()) {
            double disp = target.getX() - x;
            x += disp / Math.abs(disp) * SPEED;
        }
        if (y != target.getY()) {
            double disp = target.getY() - y;
            y += disp / Math.abs(disp) * SPEED;
        }
        entity.transformC.setPosition(new Point2D(x, y));
        
        //App.logger.debug("{} {}", entity.getX(), entity.getY());

        // if (
        //     sprite.getTranslateX() == target.getX() && 
        //     sprite.getTranslateY() == target.getY()
        // ) timer.stop();
    }
    
	private Point2D checkMove() {
		double x = 0;
		if (InputChecker.checkKeyDown(KeyCode.A)) {
			x += -SPEED;
		}
		if (InputChecker.checkKeyDown(KeyCode.D)) {
			x += SPEED;
		}
		
		double y = 0;
		if (InputChecker.checkKeyDown(KeyCode.S)) {
			y += SPEED;
		}
		if (InputChecker.checkKeyDown(KeyCode.W)) {
			y += -SPEED;
		}
		
		return new Point2D(x, y);
	}
}


