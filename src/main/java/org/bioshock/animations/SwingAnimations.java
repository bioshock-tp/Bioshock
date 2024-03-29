package org.bioshock.animations;

import org.bioshock.entities.Entity;
import org.bioshock.utils.GlobalConstants;
import org.bioshock.utils.Size;

import javafx.geometry.Point2D;

public class SwingAnimations {

    static Sprite topRightSwing;
    static Sprite topLeftSwing;
    static Sprite bottomRightSwing;
    static Sprite bottomLeftSwing;
    static Sprite idle;

    /**
     * Creates the swing animations for the seeker.
     * @param entity Entity to animate.
     * @param scale Scale of the animation to play.
     */
    public SwingAnimations(Entity entity, double scale) {
        topRightSwing = new Sprite(
            entity,
            new Point2D(422, 188),
            new Size(
                84,
                84
            ),
            96,
            5,
            scale,
            true,
            GlobalConstants.PLAYER_ANIMATION_SPEED / 1.5
        );


        topLeftSwing = new Sprite(
            entity,
            new Point2D(422, 284),
            new Size(
                84,
                84
            ),
            96,
            5,
            scale,
            true,
            GlobalConstants.PLAYER_ANIMATION_SPEED / 1.5
        );

        bottomRightSwing = new Sprite(
            entity,
            new Point2D(422, 380),
            new Size(
                84,
                84
            ),
            96,
            5,
            scale,
            true,
            GlobalConstants.PLAYER_ANIMATION_SPEED / 1.5
        );

        bottomLeftSwing = new Sprite(
            entity,
            new Point2D(422, 476),
            new Size(
                84,
                84
            ),
            96,
            5,
            scale,
            true,
            GlobalConstants.PLAYER_ANIMATION_SPEED / 1.5
        );

        idle = new Sprite(
            entity,
            new Point2D(0, 364),
            new Size(
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

    public static Sprite getTopRightSwing() {
        return topRightSwing;
    }

    public static Sprite getTopLeftSwing() {
        return topLeftSwing;
    }

    public static Sprite getBottomRightSwing() {
        return bottomRightSwing;
    }

    public static Sprite getBottomLeftSwing() {
        return bottomLeftSwing;
    }

    public static Sprite getIdle() {
        return idle;
    }

}
