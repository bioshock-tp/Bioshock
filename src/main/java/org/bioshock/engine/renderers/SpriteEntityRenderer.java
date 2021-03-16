package org.bioshock.engine.renderers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.bioshock.engine.entity.SpriteEntity;
import org.bioshock.engine.rendering.RenderManager;

public final class SpriteEntityRenderer implements Renderer {
    private SpriteEntityRenderer() {}

    public static <E extends SpriteEntity> void render(
        GraphicsContext gc,
        E player
    ) {
    	
    	
        double x = player.getX();
        double y = player.getY();
        double radius = player.getRadius();
//        double width = player.getWidth();
//        double height = player.getHeight();
        
//        if (player == EntityManager.getCurrentPlayer()) {
        	gc.save();
            gc.beginPath();
//        	gc.arc(getRenX(x + width / 2),
//            		getRenY(y + height / 2),
//            		getRenWidth(radius),
//            		getRenHeight(radius),
//            		0, 360);
        	gc.rect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
            gc.closePath();
            gc.clip();
            gc.setFill(new Color(0, 0, 0, 0.75));
            gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
            gc.restore();
//        }
        
        
        gc.save();
        RenderManager.clipToFOV(gc);
        
//        Rotate r = player.getRotate();
//        gc.setTransform(
//            r.getMxx(), r.getMyx(), r.getMxy(),
//            r.getMyy(), r.getTx(), r.getTy()
//        );
        gc.setFill(player.getRendererC().getColor());
//        gc.fillRect(getRenX(x), getRenY(y), getRenWidth(width), getRenHeight(height));
        gc.setLineWidth(10);
        gc.setStroke(player.getRendererC().getColor());
//        gc.strokeOval(
//    		getRenX(x - radius + width / 2),
//    		getRenY(y - radius + height / 2),
//    		getRenWidth(radius * 2), 
//    		getRenHeight(radius * 2)
//        );
        
        
        
        gc.restore();
    } 
}
