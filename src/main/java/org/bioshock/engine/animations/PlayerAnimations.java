package org.bioshock.engine.animations;

import org.bioshock.engine.entity.Entity;
import org.bioshock.engine.entity.Size;
import org.bioshock.utils.GlobalConstants;

import javafx.geometry.Point2D;

public class PlayerAnimations {

    Sprite moveRight;
    Sprite moveLeft;
    Sprite moveUp;
    Sprite moveDown;
    Sprite idle;
    Sprite die;

    public PlayerAnimations(Entity entity, int scale) {
        moveDown  = new Sprite(
            entity,
            new Point2D(0, 0),
            new Size(
                GlobalConstants.PLAYER_WIDTH,
                GlobalConstants.PLAYER_HEIGHT
            ),
            30,
            3,
            scale,
            false
        );

        moveLeft  = new Sprite(
            entity,
            new Point2D(30, 0),
            new Size(
                GlobalConstants.PLAYER_WIDTH,
                GlobalConstants.PLAYER_HEIGHT
            ),
            30,
            3,
            scale,
            false
        );

        moveUp = new Sprite(
            entity,
            new Point2D(60, 0),
            new Size(
                GlobalConstants.PLAYER_WIDTH - 1.5,
                GlobalConstants.PLAYER_HEIGHT
            ),
            30,
            3,
            scale,
            false
        );

        moveRight = new Sprite(
            entity,
            new Point2D(90, 0),
            new Size(
                GlobalConstants.PLAYER_WIDTH,
                GlobalConstants.PLAYER_HEIGHT
            ),
            30,
            3,
            scale,
            false
        );

        idle = new Sprite(
            entity,
            new Point2D(118, 0),
            new Size(
                GlobalConstants.PLAYER_WIDTH,
                GlobalConstants.PLAYER_HEIGHT
            ),
            30,
            1,
            scale,
            false
        );

        die = new Sprite(
            entity,
            new Point2D(1480, 0),
            new Size(
                GlobalConstants.PLAYER_WIDTH + 4,
                GlobalConstants.PLAYER_HEIGHT + 2
            ),
            30,
            1,
            scale,
            false
        );
    }

    public Sprite getMoveRightSprite() {
        return moveRight;
    }

    public Sprite getMoveLeftSprite() {
        return moveLeft;
    }

    public Sprite getMoveUpSprite() {
        return moveUp;
    }

    public Sprite getMoveDownSprite() {
        return moveDown;
    }
    public Sprite getPlayerIdleSprite() {
        return idle;
    }
    public Sprite getPlayerDying() {
        return die;
    }
}