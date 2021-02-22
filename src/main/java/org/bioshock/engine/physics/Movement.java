package org.bioshock.engine.physics;

import org.bioshock.engine.input.InputChecker;
import org.bioshock.engine.sprites.SquareEntity;
import org.bioshock.main.App2;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

public class Movement {
    public double speed = 10;

    private SquareEntity entity;

    public Movement(SquareEntity sprite) {
        this.entity = sprite;
    }
    
    public void tick(double timeDelta) {
    	Point2D inputMovement = checkMove();
    	
    	move(inputMovement);
    }

    public void move(Point2D trans) {
        Point2D target = trans.add(entity.transformC.getPosition());
        rotate(trans);
        update(target);
    }

    public void rotate(Point2D trans){
        double rotation = Math.atan2(trans.getX(), -trans.getY())*180/Math.PI;
        entity.transformC.setRotation(rotation);
    }

    private void update(Point2D target) {
    	double x = entity.transformC.getPosition().getX();
        double y = entity.transformC.getPosition().getY();
        
        // Sprite[] sprites = App.getSprites();

        // for (Sprite spr : )
              		
        if (x != target.getX()) {
            double disp = target.getX() - x;
            x += disp / Math.abs(disp) * speed;
        }
        if (y != target.getY()) {
            double disp = target.getY() - y;
            y += disp / Math.abs(disp) * speed;
        }
        entity.transformC.setPosition(new Point2D(x, y));
        
        // if (
        //     sprite.getTranslateX() == target.getX() && 
        //     sprite.getTranslateY() == target.getY()
        // ) timer.stop();
    }
    
	private Point2D checkMove() {
		double x = 0;
		if (InputChecker.checkKeyDown(KeyCode.A)) {
			x += -speed;
		}
		if (InputChecker.checkKeyDown(KeyCode.D)) {
			x += speed;
		}
		
		double y = 0;
		if (InputChecker.checkKeyDown(KeyCode.S)) {
			y += speed;
		}
		if (InputChecker.checkKeyDown(KeyCode.W)) {
			y += -speed;
		}
		
		return new Point2D(x, y);
	}
}


