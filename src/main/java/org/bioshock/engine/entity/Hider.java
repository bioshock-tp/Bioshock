package org.bioshock.engine.entity;

import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.input.InputManager;
import org.bioshock.engine.renderers.PlayerRenderer;
import org.bioshock.engine.renderers.components.PlayerRendererC;
import org.bioshock.main.App;

import javafx.geometry.Point3D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class Hider extends SquareEntity {
    private boolean dead = false;

    public Hider(Point3D p, NetworkC com, Size s, int r, Color c) {
    	super(p, com, new PlayerRendererC(), s, r, c);

        ((PlayerRendererC) rendererC).setOriginalColour(c);

        renderer = PlayerRenderer.class;
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
    }

	protected void tick(double timeDelta) {
        movement.tick(timeDelta);
	}

    public void setDead(boolean d) {
        dead = d;

        if (dead) {
            rendererC.setColor(Color.GREY);

            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    App.logger.debug("Revived PogU");
                    setDead(false);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();;
                }

            }).start();
        } else {
            ((PlayerRendererC) rendererC).revertColour();
        }
    }

    public boolean isDead() {
        return dead;
    }
}
