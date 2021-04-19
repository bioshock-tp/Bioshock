package org.bioshock.animations;

import org.bioshock.entities.Entity;
import org.bioshock.utils.GlobalConstants;
import org.bioshock.utils.Size;

import javafx.geometry.Point2D;

public class SeekerAnimations implements PlayerAnimation {

    Sprite moveRight;
    Sprite moveLeft;
    Sprite moveUp;
    Sprite moveDown;
    Sprite idle;
    Sprite die;

    public SeekerAnimations(Entity entity, double scale) {
        moveDown = new Sprite(
            entity,
            new Point2D(420, 0),
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
            new Point2D(480, 0),
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
            new Point2D(540, 0),
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
            new Point2D(600, 0),
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
            new Point2D(656, 0),
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
            new Point2D(516, 0),
            new Size(
                GlobalConstants.PLAYER_WIDTH + 4,
                GlobalConstants.PLAYER_HEIGHT + 2
            ),
            60,
            1,
            scale,
            false,
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

