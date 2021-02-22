package org.bioshock.engine.ai;

import javafx.scene.paint.Color;
import org.bioshock.engine.physics.Movement;
import org.bioshock.engine.sprites.SquareEntity;
import org.bioshock.render.components.SquareEntityRendererC;
import org.bioshock.render.components.SwatterRendererC;
import org.bioshock.transform.components.SquareEntityTransformC;
import org.bioshock.transform.components.SwatterTransformC;

public class Swatter extends SquareEntity {

    public SwatterTransformC transform;
    public SwatterRendererC renderer;
    public Movement movement;
    public Enemy enemy;


    public Swatter(SwatterTransformC transform, SwatterRendererC renderer, int x, int y, int w, int h, Color c, double z, Enemy enemy) {
        super(transform, renderer, x, y, w, h, c, z);
        this.transform = transform;
        this.renderer = renderer;
        this.movement = new Movement(this);
        this.enemy = enemy;

        setXYRectangle(x, y);
    }

    public Swatter(int x, int y, int w, int h, Color c, double z, Enemy enemy){
        this(new SwatterTransformC(), new SwatterRendererC(), x, y, w, h, c, z, enemy);
    }

    public void setEnemy(Enemy e){
        enemy = e;
    }

    public Enemy getEnemy(){
        return enemy;
    }

    @Override
    public void destroy() {
        return;
    }

    @Override
    protected void tick(double timeDelta) {
    }
}
