package org.bioshock.engine.renderers;

import java.security.InvalidParameterException;

import org.bioshock.engine.ai.Swatter;
import org.bioshock.engine.entity.Entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Rotate;

public final class SwatterRenderer implements Renderer {
    public void render(GraphicsContext gc, Entity entity) {
        if (!(entity instanceof Swatter)) {
            throw new InvalidParameterException();
        }

        Swatter swatter = (Swatter) entity;

        int x = swatter.getX();
        int y = swatter.getY();
        double width = swatter.getWidth();
        double height = swatter.getHeight();

        gc.save();

        Rotate r = swatter.getRotation();

        gc.setTransform(
            r.getMxx(), r.getMyx(), r.getMxy(),
            r.getMyy(), r.getTx(), r.getTy()
        );
        gc.setFill(swatter.getRendererC().getColor());
        gc.fillRect(x, y, width, height);
        gc.setLineWidth(10);
        gc.setStroke(swatter.getRendererC().getColor());

        gc.restore();
    }
}
