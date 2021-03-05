package org.bioshock.engine.renderers;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Arc;
import org.bioshock.engine.entity.SeekerHuman;
import org.bioshock.engine.entity.SpriteEntity;

import static org.bioshock.engine.scene.SceneManager.getPane;

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

        //gc.drawImage(seeker.getImage(), x, y);
        //ImageView imageView = entity.getSpriteImageView();
        ImageView imageView = new ImageView(seeker.getImage());
//        imageView.setSmooth(true);dw
//        imageView.setCache(true);

        imageView.setX(x);
        imageView.setY(y);
//        imageView.setLayoutY(x);
//        imageView.setLayoutY(y);
//        imageView.setTranslateX(x);
//        imageView.setTranslateY(y);
        imageView.setViewport(new Rectangle2D(0,0,16,32));
        StackPane pane = getPane();
        pane.getChildren().add(imageView);
        //gc.fillRect(x, y, width, height);
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
