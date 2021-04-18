package org.bioshock.animations;

import org.bioshock.entities.Entity;
import org.bioshock.utils.GlobalConstants;
import org.bioshock.utils.SizeD;

import javafx.geometry.Point2D;

public class SwingAnimations {

    Sprite topRightSwing;
    Sprite topLeftSwing;
    Sprite bottomRightSwing;
    Sprite bottomLeftSwing;
    Sprite topRightIdle;
    Sprite topLeftIdle;
    Sprite bottomRightIdle;
    Sprite bottomLeftIdle;

    public SwingAnimations(Entity entity, double scale) {
        topRightSwing = new Sprite(
            entity,
            new Point2D(422, 188),
            new SizeD(
                84,
                84
            ),
            96,
            5,
            scale,
            true,
            GlobalConstants.PLAYER_ANIMATION_SPEED / 1.5
        );

        topRightIdle = new Sprite(
            entity,
            new Point2D(422, 188),
            new SizeD(
                84,
                84
            ),
            96,
            1,
            scale,
            true,
            GlobalConstants.PLAYER_ANIMATION_SPEED / 1.5
        );

        topLeftSwing = new Sprite(
            entity,
            new Point2D(422, 284),
            new SizeD(
                84,
                84
            ),
            96,
            5,
            scale,
            true,
            GlobalConstants.PLAYER_ANIMATION_SPEED / 1.5
        );

        topLeftIdle = new Sprite(
            entity,
            new Point2D(422, 284),
            new SizeD(
                84,
                84
            ),
            96,
            1,
            scale,
            true,
            GlobalConstants.PLAYER_ANIMATION_SPEED / 1.5
        );

        bottomRightSwing = new Sprite(
            entity,
            new Point2D(422, 380),
            new SizeD(
                84,
                84
            ),
            96,
            5,
            scale,
            true,
            GlobalConstants.PLAYER_ANIMATION_SPEED / 1.5
        );

        bottomRightIdle = new Sprite(
            entity,
            new Point2D(422, 380),
            new SizeD(
                84,
                84
            ),
            96,
            1,
            scale,
            true,
            GlobalConstants.PLAYER_ANIMATION_SPEED / 1.5
        );

        bottomLeftSwing = new Sprite(
            entity,
            new Point2D(422, 476),
            new SizeD(
                84,
                84
            ),
            96,
            5,
            scale,
            true,
            GlobalConstants.PLAYER_ANIMATION_SPEED / 1.5
        );

        bottomLeftIdle = new Sprite(
            entity,
            new Point2D(422, 476),
            new SizeD(
                84,
                84
            ),
            96,
            1,
            scale,
            true,
            GlobalConstants.PLAYER_ANIMATION_SPEED / 1.5
        );

    }

    public Sprite getTopRightSwing() {
        return topRightSwing;
    }

    public Sprite getTopLeftSwing() {
        return topLeftSwing;
    }

    public Sprite getBottomRightSwing() {
        return bottomRightSwing;
    }

    public Sprite getBottomLeftSwing() {
        return bottomLeftSwing;
    }

    public Sprite getTopRightIdle() {
        return topRightIdle;
    }

    public Sprite getTopLeftIdle() {
        return topLeftIdle;
    }

    public Sprite getBottomRightIdle() {
        return bottomRightIdle;
    }

    public Sprite getBottomLeftIdle() {
        return bottomLeftIdle;
    }
}
