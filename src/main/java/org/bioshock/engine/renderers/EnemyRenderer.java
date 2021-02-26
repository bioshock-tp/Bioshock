package org.bioshock.engine.renderers;

import java.security.InvalidParameterException;

import org.bioshock.engine.ai.Enemy;
import org.bioshock.engine.entity.Entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Rotate;

public class EnemyRenderer implements Renderer {
    @Override
    public void render(GraphicsContext gc, Entity entity) {
        if (!(entity instanceof Enemy)) {
            throw new InvalidParameterException();
        }

        Enemy enemy = (Enemy) entity;

        int x = enemy.getX();
        int y = enemy.getY();
        int radius = enemy.getRadius();
        double width = enemy.getWidth();
        double height = enemy.getHeight();

        gc.save();

        Rotate r = enemy.getRotation();
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
        gc.setFill(enemy.getColor());
        gc.fillRect(x, y, width, height);
        gc.setLineWidth(10);
        gc.setStroke(enemy.getColor());
        gc.strokeOval(
            x - radius + (double) width / 2, y - radius + (double) height / 2,
            (double) radius * 2, (double) radius * 2
        );

        gc.restore();
    }
}
