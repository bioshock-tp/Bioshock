package org.bioshock.rendering.renderers;

import static org.bioshock.rendering.RenderManager.getRenHeight;
import static org.bioshock.rendering.RenderManager.getRenWidth;
import static org.bioshock.rendering.RenderManager.getRenX;
import static org.bioshock.rendering.RenderManager.getRenY;

import org.bioshock.animations.AnimationPlayer;
import org.bioshock.entities.EntityManager;
import org.bioshock.entities.players.Hider;
import org.bioshock.rendering.RenderManager;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;

public class PlayerSpriteRenderer implements Renderer {
    private PlayerSpriteRenderer() {}

    public static <E extends Hider> void render(
        GraphicsContext gc,
        E player
    ) {
        double x = player.getX();
        double y = player.getY();
        double radius = player.getRadius();
        double width = player.getWidth();
        double height = player.getHeight();

        if (
            player == EntityManager.getCurrentPlayer()
            && RenderManager.clips()
        ) {
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

        ColorAdjust colourAdjust = player.getColourAdjust();

        gc.setEffect(colourAdjust);
        gc.setFill(player.getRendererC().getColour());

        gc.setLineWidth(10);
        gc.setStroke(player.getRendererC().getColour());

        // Handles animations
        AnimationPlayer.playAnimation(
            player.getCurrentSprite(),
            new Point2D (
                getRenX(x),
                getRenY(y)
            ),
            player.getSize()
        );

        gc.setTextAlign(TextAlignment.CENTER);

        if (player == EntityManager.getCurrentPlayer()) {
            gc.setFill(Color.GREEN);
        }
        else {
            gc.setFill(Color.BLACK);
        }

        gc.setFont(new Font(getRenHeight(20)));
        gc.fillText(
            player.getName(),
            getRenX(x + width / 2),
            getRenY(y - 5),
            getRenWidth(width * 3)
        );

        gc.restore();
    }
}
