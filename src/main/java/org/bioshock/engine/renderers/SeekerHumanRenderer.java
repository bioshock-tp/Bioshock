package org.bioshock.engine.renderers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Arc;
import org.bioshock.engine.entity.SeekerHuman;
import org.bioshock.engine.entity.SpriteEntity;

public class SeekerHumanRenderer implements Renderer {

    private SeekerHumanRenderer() {}

    public static <E extends SpriteEntity> void render(
            GraphicsContext gc,
            E entity
    ) {
        SeekerHuman seeker = (SeekerHuman) entity;

        int x = seeker.getX();
        int y = seeker.getY();
        double radius = seeker.getRadius();
        double width = seeker.getWidth();
        double height = seeker.getHeight();
        Arc swatter = seeker.getSwatterHitbox();
        boolean isActive = seeker.getIsActive();

        gc.save();

//        Rotate r = seeker.getRotation();
//        gc.setTransform(
//                r.getMxx(), r.getMyx(), r.getMxy(),
//                r.getMyy(), r.getTx(), r.getTy()
//        );
//        gc.setFill(seeker.getRendererC().getColor());

        gc.drawImage(seeker.getImage(), 48,0, 16, 32, x, y, 16, 32);

        gc.setLineWidth(10);
        gc.setStroke(seeker.getRendererC().getColor());
        gc.strokeOval(
                x - radius + width / 2,
                y - radius + height / 2,
                radius * 2, radius * 2
        );

        if(isActive){
            //put animation here instead of gc.fillArc
            gc.fillArc(swatter.getCenterX() - swatter.getRadiusX(), swatter.getCenterY() - swatter.getRadiusY(), swatter.getRadiusX()*2,swatter.getRadiusY()*2, swatter.getStartAngle(), swatter.getLength(), swatter.getType());

            seeker.setActive(false);
        }


        gc.restore();

        //pane.getChildren().remove(imageView);

        //seeker.getRendererC().setColor(Color.INDIANRED);
    }
}
