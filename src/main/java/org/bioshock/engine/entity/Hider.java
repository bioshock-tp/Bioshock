package org.bioshock.engine.entity;

import javafx.geometry.Point3D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import org.bioshock.engine.animations.PlayerAnimations;
import org.bioshock.engine.animations.Sprite;
import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.input.InputManager;
import org.bioshock.engine.renderers.PlayerSpriteRenderer;
import org.bioshock.main.App;
import org.bioshock.utils.GlobalConstants;

public class Hider extends SquareEntity {
    private boolean dead = false;
    Sprite currentSprite;
    PlayerAnimations playerAnimations;


    public Hider(Point3D p, NetworkC com, Size s, int r, Color c) {
    	super(p, com, s, r, c);

        renderer = PlayerSpriteRenderer.class;
    }

	protected void tick(double timeDelta) {
        dead = false;
        movement.tick(timeDelta);
	}

    public void initMovement() {
        final double speed = movement.getSpeed();

        InputManager.onPress(
            KeyCode.W, () -> {
                movement.direction(0, -speed);
                setCurrentSprite(playerAnimations.getMoveUpSprite());
            }
        );
        InputManager.onPress(
            KeyCode.A, () -> {
                movement.direction(-speed, 0);
                setCurrentSprite(playerAnimations.getMoveLeftSprite());
            }
        );
        InputManager.onPress(
            KeyCode.S, () -> {
                movement.direction(0,  speed);
                setCurrentSprite(playerAnimations.getMoveDownSprite());
            }
        );
        InputManager.onPress(
            KeyCode.D, () -> {
                movement.direction(speed,  0);
                setCurrentSprite(playerAnimations.getMoveRightSprite());
            }
        );

        InputManager.onRelease(
            KeyCode.W, () -> {
                movement.direction(0,  speed);
                setCurrentSprite(playerAnimations.getPlayerIdleSprite());
            }
        );
        InputManager.onRelease(
            KeyCode.A, () -> {
                movement.direction(speed,  0);
                setCurrentSprite(playerAnimations.getPlayerIdleSprite());
            }
        );
        InputManager.onRelease(
            KeyCode.S, () -> {
                movement.direction(0, -speed);
                setCurrentSprite(playerAnimations.getPlayerIdleSprite());
            }
        );
        InputManager.onRelease(
            KeyCode.D, () -> {
                movement.direction(-speed, 0);
                setCurrentSprite(playerAnimations.getPlayerIdleSprite());
            }
        );
    }

    public void initAnimations() {
        playerAnimations = new PlayerAnimations(this, GlobalConstants.PLAYER_SCALE);
        currentSprite = playerAnimations.getPlayerIdleSprite();
    }

    private void setCurrentSprite(Sprite s) {
        if (s != null) {
            currentSprite = s;
        } else {
            App.logger.debug("Sprite is missing!");
        }
    }

    public void setDead(boolean d) {
        dead = d;
    }

    public boolean isDead() {
        return dead;
    }

    public Sprite getCurrentSprite() {
        return currentSprite;
    }

}
