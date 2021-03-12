package org.bioshock.engine.renderers;

import static org.bioshock.engine.rendering.RenderManager.* ;

import org.bioshock.engine.entity.EntityManager;
import org.bioshock.engine.entity.SquareEntity;
import org.bioshock.engine.rendering.RenderManager;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;

public final class PlayerRenderer implements Renderer {
    private PlayerRenderer() {}

    public static <E extends SquareEntity> void render(
        GraphicsContext gc,
        E player
    ) {
    	
    	
        double x = player.getX();
        double y = player.getY();
        double radius = player.getRadius();
        double width = player.getWidth();
        double height = player.getHeight();
        
        if (player == EntityManager.getCurrentPlayer()) {
        	gc.save();
            gc.beginPath();
        	gc.arc(getRenX(x + width / 2),
            		getRenY(y + height / 2),
            		getRenWidth(radius), 
            		getRenHeight(radius), 
            		0, 360);
        	gc.rect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
            gc.closePath();
            gc.clip();
            gc.setFill(new Color(0, 0, 0, 0.75));
            gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
            gc.restore();
        }
        
        
        gc.save();
        RenderManager.clipToFOV(gc);
        
//        gc.setFill(new Color(1, 1, 1, 0.7));
//        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        
//        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        
        Rotate r = player.getRotate();
        gc.setTransform(
            r.getMxx(), r.getMyx(), r.getMxy(),
            r.getMyy(), r.getTx(), r.getTy()
        );
        gc.setFill(player.getRendererC().getColor());
        gc.fillRect(getRenX(x), getRenY(y), getRenWidth(width), getRenHeight(height));
        gc.setLineWidth(10);
        gc.setStroke(player.getRendererC().getColor());
        gc.strokeOval(
    		getRenX(x - radius + width / 2),
    		getRenY(y - radius + height / 2),
    		getRenWidth(radius * 2), 
    		getRenHeight(radius * 2)
        );
        
        
        
        gc.restore();
    } 
}
