package org.bioshock.engine.renderers;

import org.bioshock.engine.entity.SquareEntity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Rotate;

public class SquareRenderer implements Renderer {
    private SquareRenderer() {}

	public static <E extends SquareEntity> void render(
        GraphicsContext gc,
        E rect
    ) {
        double x = rect.getX();
        double y = rect.getY();
        double width = rect.getWidth();
        double height = rect.getHeight();

        gc.save();

        Rotate r = rect.getRotate();
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
        gc.setFill(rect.getRendererC().getColor());
        gc.fillRect(x, y, width, height);

        gc.restore();
    }
}
