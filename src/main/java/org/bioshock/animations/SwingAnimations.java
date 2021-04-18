package org.bioshock.animations;

import javafx.geometry.Point2D;
import org.bioshock.entities.Entity;
import org.bioshock.utils.GlobalConstants;
import org.bioshock.utils.Size;

public class SwingAnimations {

    Sprite topRight;
    Sprite topLeft;
    Sprite bottomRight;
    Sprite bottomLeft;
//    Sprite idle;
//    Sprite die;

    public SwingAnimations(Entity entity, int scale) {
        topRight = new Sprite(
            entity,
            new Point2D(211, 94),
            new Size(
                42,
                42
            ),
            48,
            5,
            scale,
            true,
            GlobalConstants.PLAYER_ANIMATION_SPEED / 1.5
        );
//
//        moveLeft = new Sprite(
//            entity,
//            new Point2D(30, 0),
//            new Size(
//                GlobalConstants.PLAYER_WIDTH,
//                GlobalConstants.PLAYER_HEIGHT
//            ),
//            30,
//            3,
//            scale,
//            false
//        );
//
//        moveUp = new Sprite(
//            entity,
//            new Point2D(60, 0),
//            new Size(
//                GlobalConstants.PLAYER_WIDTH - 1.5,
//                GlobalConstants.PLAYER_HEIGHT
//            ),
//            30,
//            3,
//            scale,
//            false
//        );
//
//        moveRight = new Sprite(
//            entity,
//            new Point2D(90, 0),
//            new Size(
//                GlobalConstants.PLAYER_WIDTH,
//                GlobalConstants.PLAYER_HEIGHT
//            ),
//            30,
//            3,
//            scale,
//            false
//        );

//        idle = new Sprite(
//            entity,
//            new Point2D(118, 0),
//            new Size(
//                GlobalConstants.PLAYER_WIDTH + 2,
//                GlobalConstants.PLAYER_HEIGHT
//            ),
//            30,
//            1,
//            scale,
//            false
//        );

//        die = new Sprite(
//            entity,
//            new Point2D(148, 0),
//            new Size(
//                GlobalConstants.PLAYER_WIDTH + 4,
//                GlobalConstants.PLAYER_HEIGHT + 2
//            ),
//            30,
//            1,
//            scale,
//            false
//        );
    }

    public Sprite getTopRightSwing() {
        return topRight;
    }
//
//    public Sprite getMoveLeftSprite() {
//        return moveLeft;
//    }
//
//    public Sprite getMoveUpSprite() {
//        return moveUp;
//    }
//
//    public Sprite getMoveDownSprite() {
//        return moveDown;
//    }
//    public Sprite getPlayerIdleSprite() {
//        return idle;
//    }
//    public Sprite getPlayerDying() {
//        return die;
//    }
}
