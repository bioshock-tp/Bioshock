package org.bioshock.engine.entity;

import org.bioshock.engine.input.InputManager;
import org.bioshock.engine.renderers.PlayerRenderer;
import org.bioshock.main.App;

import javafx.geometry.Point3D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class Hider extends SquareEntity {
    private boolean dead = false;

    public Hider(Point3D p, NetworkC com, Size s, int r, Color c) {
    	super(p, com, s, r, c);

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
            KeyCode.W, () -> movement.direction(0,  speed)
        );
        InputManager.onReleaseListener(
            KeyCode.A, () -> movement.direction(speed,  0)
        );
        InputManager.onReleaseListener(
            KeyCode.S, () -> movement.direction(0, -speed)
        );
        InputManager.onReleaseListener(
            KeyCode.D, () -> movement.direction(-speed, 0)
        );
    }
    
	@Override
	protected void tick(double timeDelta) {
        if (dead) {
            App.logger.info("{} is dead", getID());
            dead = false;
        }
        movement.tick(timeDelta);
	}

    public void setDead(boolean d){
        dead = d;
    }

    public boolean getDead(){
        return dead;
    }

	public int getRadius() {
		return (int) fov.getRadius();
	}
}
