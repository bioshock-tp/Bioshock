package org.bioshock.engine.renderers;

import java.security.InvalidParameterException;

import org.bioshock.engine.entity.Entity;
import org.bioshock.engine.entity.SquareEntity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Rotate;

public final class PlayerRenderer implements Renderer {
    @Override
    public void render(GraphicsContext gc, Entity entity) {
        if (!(entity instanceof SquareEntity)) {
            throw new InvalidParameterException();
        }

        SquareEntity player = (SquareEntity) entity;

        int x = player.getX();
        int y = player.getY();
        int radius = player.getRadius();
        double width = player.getWidth();
        double height = player.getHeight();

        gc.save();

        Rotate r = player.getRotation();
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
        gc.setFill(player.getSqrenc().getColor());
        gc.fillRect(x, y, width, height);
        gc.setLineWidth(10);
        gc.setStroke(player.getSqrenc().getColor());
        gc.strokeOval(
            x - radius + (double) width / 2, y - radius + (double) height / 2,
            (double) radius * 2, (double) radius * 2
        );

        gc.restore();
    }
}
