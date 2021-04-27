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

    public static <E extends SeekerAI> void render(GraphicsContext gc, E ent) {
        SeekerAI seeker = ent;

        double x = seeker.getX();
        double y = seeker.getY();
        double width = seeker.getWidth();
        double height = seeker.getHeight();
        double radius = seeker.getRadius();
        boolean isActive = seeker.isActive();

        gc.save();
        RenderManager.clipToFOV(gc);

        Rotate r = seeker.getRotate();
        gc.setTransform(
            r.getMxx(), r.getMyx(), r.getMxy(),
            r.getMyy(), r.getTx(), r.getTy()
        );
        ColorAdjust ca = new ColorAdjust(0,0,0,0);
        if(seeker.getMovement().getSpeed() == 0){
            ca.setHue(Color.BLUE.getHue());
            ca.setContrast(-0.9);
        }
        gc.setEffect(ca);
        /*gc.setFill(seeker.getRendererC().getColour());
        gc.fillRect(
            getRenX(x),
            getRenY(y),
            getRenWidth(width),
            getRenHeight(height)
        );*/
        gc.setLineWidth(getRenWidth(10));
        gc.setStroke(seeker.getRendererC().getColour());

        gc.strokeOval(
            getRenX(x - radius + width / 2),
            getRenY(y - radius + height / 2),
            getRenWidth(radius * 2),
            getRenHeight(radius * 2)
        );

        gc.setLineWidth(10);
        gc.setStroke(seeker.getRendererC().getColour());

        if (isActive) {
            // Handles swing animation
            Sprite currentSwingAnimation = ent.getCurrentSwingAnimation();
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
        int rightXOffset = 12;
        int topYOffset = 70;
        int leftXOffset = 80;


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
