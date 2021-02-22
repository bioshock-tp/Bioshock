package org.bioshock.render.renderers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Rotate;
import org.bioshock.engine.ai.Enemy;
import org.bioshock.engine.ai.Swatter;
import org.bioshock.engine.entity.GameEntityBase;
import org.bioshock.engine.rendering.IBaseRenderer;
import org.bioshock.render.components.SwatterRendererC;
import org.bioshock.transform.components.SwatterTransformC;

public class SwatterRenderer implements IBaseRenderer {
    @Override
    public void render(GraphicsContext gc, GameEntityBase ge) {
        Swatter entity = (Swatter) ge;
        SwatterTransformC transform = (SwatterTransformC) entity.transformC;
        SwatterRendererC renderer = (SwatterRendererC) entity.rendererC;

        Enemy enemy = entity.getEnemy();

        double x = transform.getPosition().getX();
        double y = transform.getPosition().getY();
        double width = transform.width;
        double height = transform.height;

        gc.save();
        Rotate r = new Rotate(transform.getRotation(), enemy.getX() + (enemy.getWidth()/2), enemy.getY() + (enemy.getHeight()/2));
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
        gc.setFill(renderer.getColor());
        gc.fillRect(x, y, width, height);
        //gc.fillArc(x-100+width/2, y-100+width/2, 200, 200, 30, 120, ArcType.ROUND);
        gc.setLineWidth(10);
        gc.setStroke(renderer.getColor());

        gc.restore();
    }
}
