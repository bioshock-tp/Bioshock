package org.bioshock.entities.map;

import org.bioshock.components.NetworkC;
import org.bioshock.entities.SquareEntity;
import org.bioshock.rendering.renderers.SquareRenderer;
import org.bioshock.rendering.renderers.components.SimpleRendererC;
import org.bioshock.utils.Size;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

public class TexRectEntity extends SquareEntity {
    public TexRectEntity(Point3D p, NetworkC com, Size s, Color c) {
        super(p, com, new SimpleRendererC(), s, 0, c);
        renderer = SquareRenderer.class;
    }

    @Override
    protected void tick(double timeDelta) {
        /* This entity does not change */
    }
}