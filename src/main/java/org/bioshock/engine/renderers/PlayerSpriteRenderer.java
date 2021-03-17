package org.bioshock.engine.renderers;

import static org.bioshock.engine.rendering.RenderManager.getRenHeight;
import static org.bioshock.engine.rendering.RenderManager.getRenWidth;
import static org.bioshock.engine.rendering.RenderManager.getRenX;
import static org.bioshock.engine.rendering.RenderManager.getRenY;

import org.bioshock.engine.animations.AnimationPlayer;
import org.bioshock.engine.entity.EntityManager;
import org.bioshock.engine.entity.Hider;
import org.bioshock.engine.entity.SquareEntity;
import org.bioshock.engine.rendering.RenderManager;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public class PlayerSpriteRenderer implements Renderer {
    private PlayerSpriteRenderer() {}

    public static <E extends SquareEntity> void render(
            GraphicsContext gc,
            E player
    ) {
        double x = player.getX();
        double y = player.getY();
        double radius = player.getRadius();
        double width = player.getWidth();
        double height = player.getHeight();

        if (player == EntityManager.getCurrentPlayer()) {
            gc.save();
            gc.beginPath();

            gc.arc(getRenX(x + width / 2),
                getRenY(y + height / 2),
                getRenWidth(radius),
                getRenHeight(radius),
                0, 360);

            gc.rect(
                0,
                0,
                gc.getCanvas().getWidth(),
                gc.getCanvas().getHeight()
            );

            gc.closePath();
            gc.clip();

            gc.setFill(new Color(0, 0, 0, 0.75));
            gc.fillRect(
                0,
                0,
                gc.getCanvas().getWidth(),
                gc.getCanvas().getHeight()
            );

            gc.restore();
        }

        gc.save();
        RenderManager.clipToFOV(gc);

        Rotate r = player.getRotate();
        gc.setTransform(
            r.getMxx(), r.getMyx(), r.getMxy(),
            r.getMyy(), r.getTx(), r.getTy()
        );
        gc.setFill(player.getRendererC().getColour());

        gc.setLineWidth(10);
        gc.setStroke(player.getRendererC().getColour());
        gc.strokeOval(
            getRenX(x - radius + width / 2),
            getRenY(y - radius + height / 2),
            getRenWidth(radius * 2),
            getRenHeight(radius * 2)
        );

        // Handles animations
        AnimationPlayer.playAnimation(
            ((Hider) player).getCurrentSprite(),
            new Point2D (
                getRenX(x),
                getRenY(y)
            )
        );

        gc.restore();
    }
}
