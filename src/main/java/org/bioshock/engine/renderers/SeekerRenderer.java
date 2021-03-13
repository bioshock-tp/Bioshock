package org.bioshock.engine.renderers;

import static org.bioshock.engine.rendering.RenderManager.getRenHeight;
import static org.bioshock.engine.rendering.RenderManager.getRenWidth;
import static org.bioshock.engine.rendering.RenderManager.getRenX;
import static org.bioshock.engine.rendering.RenderManager.getRenY;

import org.bioshock.engine.ai.SeekerAI;
import org.bioshock.engine.rendering.RenderManager;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.transform.Rotate;

public class SeekerRenderer implements Renderer {

    private SeekerRenderer() {}

    public static <E extends SeekerAI> void render(
            GraphicsContext gc,
            E entity
    ) {
        SeekerAI seeker = entity;

        double x = seeker.getX();
        double y = seeker.getY();
        double radius = seeker.getRadius();
        double width = seeker.getWidth();
        double height = seeker.getHeight();
        Arc swatter = seeker.getSwatterHitbox();
        boolean isActive = seeker.getIsActive();

        gc.save();

        RenderManager.clipToFOV(gc);
        Rotate r = seeker.getRotate();
        gc.setTransform(
                r.getMxx(), r.getMyx(), r.getMxy(),
                r.getMyy(), r.getTx(), r.getTy()
        );
        gc.setFill(seeker.getRendererC().getColor());
        gc.fillRect(getRenX(x), getRenY(y), getRenWidth(width), getRenHeight(height));
        gc.setLineWidth(10);
        gc.setStroke(seeker.getRendererC().getColor());
//        gc.strokeOval(
//    		getRenX(x - radius + width / 2),
//    		getRenY(y - radius + height / 2),
//    		getRenWidth(radius * 2),
//    		getRenHeight(radius * 2)
//        );

        if(isActive){
//            put animation here instead of gc.fillArc
            gc.fillArc(
        		getRenX(swatter.getCenterX() - swatter.getRadiusX()),
        		getRenY(swatter.getCenterY() - swatter.getRadiusY()),
        		getRenWidth(swatter.getRadiusX()*2),
        		getRenHeight(swatter.getRadiusY()*2),
        		swatter.getStartAngle(),
        		swatter.getLength(),
        		swatter.getType()
    		);

            seeker.setActive(false);
        }

        gc.restore();

        seeker.getRendererC().setColor(Color.INDIANRED);
    }
}
