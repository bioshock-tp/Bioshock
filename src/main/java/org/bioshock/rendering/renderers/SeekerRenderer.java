package org.bioshock.rendering.renderers;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import org.bioshock.animations.AnimationPlayer;
import org.bioshock.animations.Sprite;
import org.bioshock.animations.SwingAnimations;
import org.bioshock.entities.players.SeekerAI;
import org.bioshock.rendering.RenderManager;

import static org.bioshock.rendering.RenderManager.*;

public class SeekerRenderer implements Renderer {
    private SeekerRenderer() {}

    /**
     * Method to render the given entity
     * @param gc the graphics context to render to
     * @param ent the seeker type to render
     * @param <E> a generic type extending SeekerAI
     */
    public static <E extends SeekerAI> void render(GraphicsContext gc, E ent) {
        SeekerAI seeker = ent;

        /**
         * get the needed values for rendering
         */
        double x = seeker.getX();
        double y = seeker.getY();
        double width = seeker.getWidth();
        boolean isActive = seeker.isActive();

        gc.save();

        RenderManager.clipToFOV(gc);

        /**
         * Rotates the rendered image of the seeker
         */
        Rotate r = seeker.getRotate();
        gc.setTransform(
            r.getMxx(), r.getMyx(), r.getMxy(),
            r.getMyy(), r.getTx(), r.getTy()
        );

        /**
         * Will change the colour of the seeker if it has speed 0 (if frozen)
         */
        ColorAdjust ca = new ColorAdjust(0,0,0,0);
        if(seeker.getMovement().getSpeed() == 0) {
            ca.setHue(Color.BLUE.getHue());
            ca.setContrast(-0.9);
        }
        gc.setEffect(ca);

        gc.setLineWidth(getRenWidth(10));
        gc.setStroke(seeker.getRendererC().getColour());

        /**
         * Does the swing animations if it is active
         */
        if (isActive) {
            // Handles swing animation
            Sprite currentSwingAnimation = ent.getCurrentSwingSprite();
            AnimationPlayer.playAnimation(
                currentSwingAnimation,
                calcSwingPosition(currentSwingAnimation, x, y),
                currentSwingAnimation.getSize()
            );
        }

        // Handles animations
        AnimationPlayer.playAnimation(
            ent.getCurrentSprite(),
            new Point2D(
                getRenX(x),
                getRenY(y)
            ),
            seeker.getSize()
        );

        gc.setFill(Color.RED);
        gc.setFont(new Font(getRenHeight(20)));
        gc.fillText(
            "Seeker(AI)",
            getRenX(x - width),
            getRenY(y - 5),
            getRenWidth(width * 3)
        );

        gc.restore();
    }

    /**
     * Calculates where to place swing animation based on the current animation and position of the seeker.
     * @param swingAnimation The current swing animation.
     * @param x The x position of the seeker.
     * @param y The y position of the seeker.
     * @return The position of where to render the swing animation.
     */
    private static Point2D calcSwingPosition(
        Sprite swingAnimation,
        double x,
        double y
    ) {
        int rightXOffset = 38;
        int topYOffset = 56;
        int leftXOffset = 70;


        if (swingAnimation == SwingAnimations.getTopRightSwing()) {
            return (
                new Point2D(getRenX(x + rightXOffset), getRenY(y - topYOffset))
            );
        }
        else if (swingAnimation == SwingAnimations.getTopLeftSwing()) {
            return (
                new Point2D(getRenX(x - leftXOffset), getRenY(y - topYOffset))
            );
        }
        else if (swingAnimation == SwingAnimations.getBottomRightSwing()) {
            return (
                new Point2D(getRenX(x + rightXOffset), getRenY(y))
            );
        }
        else if (swingAnimation == SwingAnimations.getBottomLeftSwing()) {
            return (
                new Point2D(getRenX(x - leftXOffset), getRenY(y))
            );
        }
        else {
            return new Point2D (getRenX(x), getRenY(y));
        }
    }
}
