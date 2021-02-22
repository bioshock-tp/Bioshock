package org.bioshock.render.renderers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.ArcType;
import javafx.scene.transform.Rotate;
import org.bioshock.engine.ai.Enemy;
import org.bioshock.engine.entity.GameEntityBase;
import org.bioshock.engine.rendering.IBaseRenderer;
import org.bioshock.engine.sprites.Player;
import org.bioshock.render.components.EnemyRendererC;
import org.bioshock.transform.components.EnemyTransformC;

public class EnemyRenderer implements IBaseRenderer {


    @Override
    public void render(GraphicsContext gc, GameEntityBase ge) {
        Enemy entity = (Enemy) ge;
        EnemyTransformC transform = (EnemyTransformC) entity.transformC;
        EnemyRendererC renderer = (EnemyRendererC) entity.rendererC;
        double x = transform.getPosition().getX();
        double y = transform.getPosition().getY();
        double radius = transform.getRadius();
        double width = transform.width;
        double height = transform.height;

        gc.save();
        Rotate r = new Rotate(transform.getRotation(), x + (width/2), y + (height/2));
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
        gc.setFill(renderer.getColor());
        gc.fillRect(x, y, width, height);
        //gc.fillRect(x-width/2, y- height, width*2, height);
        //gc.fillArc(x-100+width/2, y-100+width/2, 200, 200, 30, 120, ArcType.ROUND);
        gc.setLineWidth(10);
        gc.setStroke(renderer.getColor());
        gc.strokeOval(x-radius + width/2, y-radius + height/2, radius*2, radius*2);

        gc.restore();
    }
}
