package org.bioshock.engine.renderers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Rotate;
import org.bioshock.engine.ai.Enemy;
import org.bioshock.engine.ai.Swatter;
import org.bioshock.engine.entity.Entity;

import java.security.InvalidParameterException;

public class SwatterRenderer implements Renderer {
    @Override
    public void render(GraphicsContext gc, Entity entity) {
        if (!(entity instanceof Swatter)) {
            throw new InvalidParameterException();
        }

        Swatter swatter = (Swatter) entity;
        Enemy enemy = swatter.getEnemy();

        int x = swatter.getX();
        int y = swatter.getY();
        double width = swatter.getWidth();
        double height = swatter.getHeight();

        gc.save();
        Rotate r = swatter.getRotation();
        //Rotate r = new Rotate(swatter.getRotation().getAngle(), enemy.getX() + (enemy.getWidth()/2), enemy.getY() + (enemy.getHeight()/2));
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
        gc.setFill(swatter.getColor());
        gc.fillRect(x, y, width, height);
        //gc.fillArc(x-100+width/2, y-100+width/2, 200, 200, 30, 120, ArcType.ROUND);
        gc.setLineWidth(10);
        gc.setStroke(swatter.getColor());

        gc.restore();
    }
}
