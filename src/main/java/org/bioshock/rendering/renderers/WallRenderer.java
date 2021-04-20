package org.bioshock.rendering.renderers;

import static org.bioshock.rendering.RenderManager.getRenHeight;
import static org.bioshock.rendering.RenderManager.getRenWidth;
import static org.bioshock.rendering.RenderManager.getRenX;
import static org.bioshock.rendering.RenderManager.getRenY;

import org.bioshock.entities.map.Wall;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Rotate;

public class WallRenderer implements Renderer{
    public static <E extends Wall> void render(
            GraphicsContext gc,
            E wall
        ) {
        double x = wall.getX();
        double y = wall.getY();
        double width = wall.getWidth();
        double height = wall.getHeight();

        gc.save();

        Rotate r = wall.getRotate();
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
        gc.setFill(wall.getRendererC().getColour());
        gc.fillRect(getRenX(x), getRenY(y), getRenWidth(width), getRenHeight(height));
        
        gc.drawImage(
            wall.getImage(), 
            getRenX(x), 
            getRenY(y), 
            getRenWidth(width), 
            getRenHeight(height));

        gc.restore();
    }
}
