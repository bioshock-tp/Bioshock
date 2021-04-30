package org.bioshock.rendering.renderers;

import static org.bioshock.rendering.RenderManager.*;

import org.bioshock.entities.SquareEntity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Rotate;

public class SquareRenderer implements Renderer {
    private SquareRenderer() {}

    /**
     * Method to render the given squareEntity
     * @param <E>
     * @param gc The graphics context to render the squareEntity on
     * @param rect The squareEntity to render
     */
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
        gc.setFill(rect.getRendererC().getColour());
        gc.fillRect(getRenX(x), getRenY(y), getRenWidth(width), getRenHeight(height));

        gc.restore();
    }
}
