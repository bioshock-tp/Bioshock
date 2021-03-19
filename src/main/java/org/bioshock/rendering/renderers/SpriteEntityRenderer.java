package org.bioshock.rendering.renderers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import org.bioshock.entities.SpriteEntity;
import org.bioshock.rendering.RenderManager;

public final class SpriteEntityRenderer implements Renderer {
    private SpriteEntityRenderer() {}

    public static <E extends SpriteEntity> void render(
        GraphicsContext gc,
        E player
    ) {
        gc.save();

        gc.beginPath();
        gc.rect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
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

        gc.save();

        RenderManager.clipToFOV(gc);

        gc.setFill(player.getRendererC().getColour());

        gc.setLineWidth(10);
        gc.setStroke(player.getRendererC().getColour());

        gc.restore();
    }
}
