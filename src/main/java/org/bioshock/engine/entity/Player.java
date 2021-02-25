package org.bioshock.engine.entity;

import org.bioshock.engine.input.InputManager;
import org.bioshock.engine.renderers.PlayerRenderer;

import javafx.geometry.Point3D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class Player extends SquareEntity {

    protected boolean dead = false;

    public Player(Point3D pos, Size s, int r, Color c) {
    	super(pos, s, r, c);

        renderer = new PlayerRenderer();

        final int speed = movement.getSpeed();

        InputManager.onPressListener(
            KeyCode.W, () -> movement.direction(0, -speed)
        );
        InputManager.onPressListener(
            KeyCode.A, () -> movement.direction(-speed, 0)
        );
        InputManager.onPressListener(
            KeyCode.S, () -> movement.direction(0,  speed)
        );
        InputManager.onPressListener(
            KeyCode.D, () -> movement.direction(speed,  0)
        );
        
        InputManager.onReleaseListener(
            KeyCode.W, () -> movement.direction(0, speed)
        );
        InputManager.onReleaseListener(
            KeyCode.A, () -> movement.direction(speed, 0)
        );
        InputManager.onReleaseListener(
            KeyCode.S, () -> movement.direction(0,  -speed)
        );
        InputManager.onReleaseListener(
            KeyCode.D, () -> movement.direction(-speed,  0)
        );
    }

    public void setDead(boolean d){
        dead = d;
    }

    public boolean getDead(){
        return dead;
    }

	@Override
	protected void tick(double timeDelta) {
        if(dead){
            System.out.println("DEAD");
            dead =false;
        }
        movement.tick(timeDelta);
	}

	public int getRadius() {
		return (int) fov.getRadius();
	}
}
