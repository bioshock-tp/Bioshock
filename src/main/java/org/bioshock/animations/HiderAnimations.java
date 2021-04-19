package org.bioshock.animations;

import org.bioshock.entities.Entity;
import org.bioshock.utils.GlobalConstants;
import org.bioshock.utils.Size;

import javafx.geometry.Point2D;

public class HiderAnimations implements PlayerAnimation {

    Sprite moveRight;
    Sprite moveLeft;
    Sprite moveUp;
    Sprite moveDown;
    Sprite idle;
    Sprite die;

    public HiderAnimations(Entity entity, int scale) {
        moveDown = new Sprite(
            entity,
            new Point2D(0, 0),
            new Size(
                GlobalConstants.PLAYER_WIDTH,
                GlobalConstants.PLAYER_HEIGHT
            ),
            60,
            3,
            scale,
            false,
            GlobalConstants.PLAYER_ANIMATION_SPEED
        );

        moveLeft = new Sprite(
            entity,
            new Point2D(30, 0),
            new Size(
                GlobalConstants.PLAYER_WIDTH,
                GlobalConstants.PLAYER_HEIGHT
            ),
            60,
            3,
            scale,
            false,
            GlobalConstants.PLAYER_ANIMATION_SPEED
        );

        moveUp = new Sprite(
            entity,
            new Point2D(60, 0),
            new Size(
                GlobalConstants.PLAYER_WIDTH - 1.5,
                GlobalConstants.PLAYER_HEIGHT
            ),
            60,
            3,
            scale,
            false,
            GlobalConstants.PLAYER_ANIMATION_SPEED
        );

        moveRight = new Sprite(
            entity,
            new Point2D(90, 0),
            new Size(
                GlobalConstants.PLAYER_WIDTH,
                GlobalConstants.PLAYER_HEIGHT
            ),
            60,
            3,
            scale,
            false,
            GlobalConstants.PLAYER_ANIMATION_SPEED
        );

        idle = new Sprite(
            entity,
            new Point2D(118, 0),
            new Size(
                GlobalConstants.PLAYER_WIDTH + 2,
                GlobalConstants.PLAYER_HEIGHT
            ),
            60,
            1,
            scale,
            false,
            GlobalConstants.PLAYER_ANIMATION_SPEED
        );

        die = new Sprite(
            entity,
            new Point2D(296, 0),
            new Size(
                GlobalConstants.PLAYER_WIDTH + 6,
                GlobalConstants.PLAYER_HEIGHT + 2
            ),
            60,
            2,
            scale,
            true,
            GlobalConstants.PLAYER_ANIMATION_SPEED
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
