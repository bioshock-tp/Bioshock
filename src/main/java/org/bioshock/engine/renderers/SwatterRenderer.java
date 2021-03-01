package org.bioshock.engine.renderers;

import org.bioshock.engine.entity.SquareEntity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Rotate;

public final class SwatterRenderer implements Renderer {
    private SwatterRenderer() {}

    public static <E extends SquareEntity> void render(
        GraphicsContext gc,
        E swatter
    ) {
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
