package org.bioshock.rendering.renderers;

import static org.bioshock.rendering.RenderManager.clipToFOV;
import static org.bioshock.rendering.RenderManager.getRenHeight;
import static org.bioshock.rendering.RenderManager.getRenWidth;
import static org.bioshock.rendering.RenderManager.getRenX;
import static org.bioshock.rendering.RenderManager.getRenY;

import org.bioshock.entities.ImageEntity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


/**
 * Draws an image on the {@code canvas}
 */
public class ImageRenderer implements Renderer {
    private ImageRenderer() {}

    /**
     * Draws an image on the {@code gc}
     * @param <E> The type of the entity to render, must extend
     * {@link org.bioshock.entities.ImageEntity ImageEntity}
     * @param gc The {@link GraphicsContext} to draw on
     * @param entity to render
     */
    public static <E extends ImageEntity> void render(
        GraphicsContext gc,
        E entity
    ) {


        Image image = entity.getImage();
        double x = entity.getX();
        double y = entity.getY();
        double w = entity.getHitbox().getWidth();
        double h = entity.getHitbox().getHeight();

        gc.save();
        
        clipToFOV(gc);

        gc.drawImage(
            image,
            getRenX(x),
            getRenY(y),
            getRenWidth(w),
            getRenHeight(h)
        );

        gc.restore();
    }
}
