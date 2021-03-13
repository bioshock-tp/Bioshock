package org.bioshock.engine.renderers;

import org.bioshock.engine.ai.SeekerAI;

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

        Rotate r = seeker.getRotate();
        gc.setTransform(
                r.getMxx(), r.getMyx(), r.getMxy(),
                r.getMyy(), r.getTx(), r.getTy()
        );
        gc.setFill(seeker.getRendererC().getColor());
        gc.fillRect(x, y, width, height);
        gc.setLineWidth(10);
        gc.setStroke(seeker.getRendererC().getColor());
        gc.strokeOval(
                x - radius + width / 2,
                y - radius + height / 2,
                radius * 2, radius * 2
        );

        if(isActive) {
            //put animation here instead of gc.fillArc
            gc.fillArc(
                swatter.getCenterX() - swatter.getRadiusX(),
                swatter.getCenterY() - swatter.getRadiusY(),
                swatter.getRadiusX()*2,swatter.getRadiusY()*2,
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
