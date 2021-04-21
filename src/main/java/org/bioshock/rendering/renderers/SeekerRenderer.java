package org.bioshock.rendering.renderers;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
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
        Arc swatter = seeker.getSwatterHitbox();
        boolean isActive = seeker.getIsActive();

        gc.save();
        RenderManager.clipToFOV(gc);
        
       
        Rotate r = seeker.getRotate();
        gc.setTransform(
            r.getMxx(), r.getMyx(), r.getMxy(),
            r.getMyy(), r.getTx(), r.getTy()
        );
        gc.setFill(seeker.getRendererC().getColour());
        gc.fillRect(
            getRenX(x),
            getRenY(y),
            getRenWidth(width),
            getRenHeight(height)
        );
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

        if(isActive) {
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
        gc.fillText("Seeker(AI)", getRenX(x-width), getRenY(y-5), getRenWidth(width*3));

        gc.restore();
    }

    private static Point2D calcSwingPosition(Sprite swingAnimation, double x, double y) {
        int rightXOffset = 12;
        int topYOffset = 70;
        int leftXOffset = 80;
        if (SwingAnimations.getTopRightSwing().equals(swingAnimation)) {
            return (
                new Point2D(getRenX(x + rightXOffset), getRenY(y - topYOffset))
            );
        }
        else if (SwingAnimations.getTopLeftSwing().equals(swingAnimation)) {
            return (
                new Point2D(getRenX(x - leftXOffset), getRenY(y - topYOffset))
            );
        }
        else if (SwingAnimations.getBottomRightSwing().equals(swingAnimation)) {
            return (
                new Point2D(getRenX(x + rightXOffset), getRenY(y))
            );
        }
        else if (SwingAnimations.getBottomLeftSwing().equals(swingAnimation)) {
            return (
                new Point2D(getRenX(x - leftXOffset), getRenY(y))
            );
        }
        return (new Point2D (getRenX(x), getRenY(y)));
    }
}
