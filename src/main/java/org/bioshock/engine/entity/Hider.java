package org.bioshock.engine.entity;

import org.bioshock.engine.components.NetworkC;
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

        renderer = PlayerRenderer.class;
    }

	protected void tick(double timeDelta) {
        dead = false;
        movement.tick(timeDelta);
	}

    public void initMovement() {
        final double speed = movement.getSpeed();

        InputManager.onPress(
            KeyCode.W, () -> movement.direction(0, -speed)
        );
        InputManager.onPress(
            KeyCode.A, () -> movement.direction(-speed, 0)
        );
        InputManager.onPress(
            KeyCode.S, () -> movement.direction(0,  speed)
        );
        InputManager.onPress(
            KeyCode.D, () -> movement.direction(speed,  0)
        );

        InputManager.onRelease(
            KeyCode.W, () -> movement.direction(0,  speed)
        );
        InputManager.onRelease(
            KeyCode.A, () -> movement.direction(speed,  0)
        );
        InputManager.onRelease(
            KeyCode.S, () -> movement.direction(0, -speed)
        );
        InputManager.onRelease(
            KeyCode.D, () -> movement.direction(-speed, 0)
        );

        App.logger.info("Initialised movement");
    }

    public void setDead(boolean d) {
        dead = d;
    }

    public boolean isDead() {
        return dead;
    }
}
