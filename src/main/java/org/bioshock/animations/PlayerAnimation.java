package org.bioshock.animations;

public interface PlayerAnimation {
    Sprite getMoveRightSprite();
    Sprite getMoveLeftSprite();
    Sprite getMoveUpSprite();
    Sprite getMoveDownSprite();
    Sprite getPlayerIdleSprite();
    Sprite getPlayerDying();
}
