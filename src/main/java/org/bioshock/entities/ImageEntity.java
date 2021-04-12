package org.bioshock.entities;

import java.io.File;

import org.bioshock.components.NetworkC;
import org.bioshock.main.App;
import org.bioshock.rendering.renderers.ImageRenderer;
import org.bioshock.rendering.renderers.components.SimpleRendererC;
import org.bioshock.utils.Size;

import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public abstract class ImageEntity extends Entity {

    protected Image image;

    /**
     * @param p
     * @param s
     * @param nC
     * @param path
     */
    protected ImageEntity(Point3D p, Size s, NetworkC nC, String path) {
        super(
            p,
            new Rectangle(
                p.getX(),
                p.getY(),
                s.getWidth(),
                s.getHeight()
            ),
            nC,
            new SimpleRendererC()
        );

        try {
            image = new Image(
                new File(path).toURI().toString(),
                s.getWidth(),
                s.getHeight(),
                true,
                true
            );

            hitbox.setWidth(image.getWidth());
            hitbox.setHeight(image.getHeight());
        } catch (IllegalArgumentException e) {
            App.logger.error("Error loading image at: {}", path, e);
            destroy();
            return;
        }

        renderer = ImageRenderer.class;
    }


    public Image getImage() {
        return image;
    }

    public void destroy() {
        hitbox = new Rectangle();
        enabled = false;
        rendererC = null;
    }
}
