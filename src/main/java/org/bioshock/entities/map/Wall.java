package org.bioshock.entities.map;

import org.bioshock.components.NetworkC;
import org.bioshock.entities.SquareEntity;
import org.bioshock.rendering.renderers.WallRenderer;
import org.bioshock.rendering.renderers.components.SimpleRendererC;
import org.bioshock.utils.Size;

import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Wall extends SquareEntity {
    private Image image;
    
    public Wall(Point3D p, NetworkC com, Size s, Color c, Image image) {
        super(p, com, new SimpleRendererC(), s, 0, c);
        renderer = WallRenderer.class;
        this.image = image;
    }

    @Override
    protected void tick(double timeDelta) {
        /* This entity does not change */
    }

    public Image getImage() {
        return image;
    }
}
