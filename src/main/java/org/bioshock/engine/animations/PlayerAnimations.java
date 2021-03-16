package org.bioshock.engine.animations;

import org.bioshock.engine.entity.Entity;
import org.bioshock.utils.GlobalConstants;

public class PlayerAnimations {

    Sprite moveRight;
    Sprite moveLeft;
    Sprite moveUp;
    Sprite moveDown;
    Sprite idle;
    Sprite die;

    public PlayerAnimations(Entity e, int scale) {
        moveDown  = new Sprite(
                e,
                30,
                GlobalConstants.PLAYER_ANIMATION_SPEED,
                0,
                0,
                3,
                GlobalConstants.PLAYER_WIDTH,
                GlobalConstants.PLAYER_HEIGHT,
                scale,
                false
        );
        moveLeft  = new Sprite(
                e,
                30,
                GlobalConstants.PLAYER_ANIMATION_SPEED,
                30,
                0,
                3,
                GlobalConstants.PLAYER_WIDTH,
                GlobalConstants.PLAYER_HEIGHT,
                scale,
                false
        );
        moveUp    = new Sprite(
                e,
                30,
                GlobalConstants.PLAYER_ANIMATION_SPEED,
                60,
                0,
                3,
                GlobalConstants.PLAYER_WIDTH - 1.5,
                GlobalConstants.PLAYER_HEIGHT,
                scale,
                false
        );
        moveRight = new Sprite(
                e,
                30,
                GlobalConstants.PLAYER_ANIMATION_SPEED,
                90,
                0,
                3,
                GlobalConstants.PLAYER_WIDTH,
                GlobalConstants.PLAYER_HEIGHT,
                scale,
                false
        );
        idle      = new Sprite(
                e,
                30,
                GlobalConstants.PLAYER_ANIMATION_SPEED,
                118,
                0,
                1,
                GlobalConstants.PLAYER_WIDTH + 2,
                GlobalConstants.PLAYER_HEIGHT,
                scale,
                false
        );
        die = new Sprite(
                e,
                30,
                GlobalConstants.PLAYER_ANIMATION_SPEED,
                148,
                0,
                1,
                GlobalConstants.PLAYER_WIDTH+4,
                GlobalConstants.PLAYER_HEIGHT+2,
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
    public Sprite getPlayerIdleSprite(){
        return idle;
    }
    public Sprite getPlayerDying(){
        return die;
    }
}
