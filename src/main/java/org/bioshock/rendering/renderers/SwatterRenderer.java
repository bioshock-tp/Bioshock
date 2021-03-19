package org.bioshock.rendering.renderers;

import static org.bioshock.rendering.RenderManager.getRenHeight;
import static org.bioshock.rendering.RenderManager.getRenWidth;
import static org.bioshock.rendering.RenderManager.getRenX;
import static org.bioshock.rendering.RenderManager.getRenY;

import org.bioshock.entities.SquareEntity;
import org.bioshock.rendering.RenderManager;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Rotate;

public final class SwatterRenderer implements Renderer {
    private SwatterRenderer() {}

    public static <E extends SquareEntity> void render(
        GraphicsContext gc,
        E swatter
    ) {
        double x = swatter.getX();
        double y = swatter.getY();
        double width = swatter.getWidth();
        double height = swatter.getHeight();

        gc.save();

        RenderManager.clipToFOV(gc);
        Rotate r = swatter.getRotate();

        gc.setTransform(
            r.getMxx(), r.getMyx(), r.getMxy(),
            r.getMyy(), r.getTx(), r.getTy()
        );
        gc.setFill(swatter.getRendererC().getColour());
        gc.fillRect(getRenX(x), getRenY(y), getRenWidth(width), getRenHeight(height));
        gc.setLineWidth(10);
        gc.setStroke(swatter.getRendererC().getColour());

        gc.restore();
    }
}
