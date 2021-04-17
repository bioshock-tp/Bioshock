package org.bioshock.animations;

import javafx.geometry.Point2D;
import org.bioshock.entities.Entity;
import org.bioshock.utils.GlobalConstants;
import org.bioshock.utils.Size;

public class SeekerAnimations implements PlayerAnimation {

    Sprite moveRight;
    Sprite moveLeft;
    Sprite moveUp;
    Sprite moveDown;
    Sprite idle;
    Sprite die;

    public SeekerAnimations(Entity entity, int scale) {
        moveDown = new Sprite(
            entity,
            new Point2D(210, 0),
            new Size(
                GlobalConstants.PLAYER_WIDTH,
                GlobalConstants.PLAYER_HEIGHT
            ),
            30,
            3,
            scale,
            false
        );

        moveLeft = new Sprite(
            entity,
            new Point2D(240, 0),
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
            new Point2D(270, 0),
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
            new Point2D(300, 0),
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
            new Point2D(328, 0),
            new Size(
                GlobalConstants.PLAYER_WIDTH + 2,
                GlobalConstants.PLAYER_HEIGHT
            ),
            30,
            1,
            scale,
            false
        );

        die = new Sprite(
            entity,
            new Point2D(258, 0),
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

