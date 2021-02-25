package org.bioshock.engine.ai;

import org.bioshock.engine.entity.Components;
import org.bioshock.engine.entity.EntityManager;
import org.bioshock.engine.entity.Player;
import org.bioshock.engine.entity.Size;
import org.bioshock.engine.entity.SquareEntity;
import org.bioshock.engine.renderers.EnemyRenderer;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class Enemy extends SquareEntity {
    private SquareEntity target;
	
    public Enemy(Point3D p, Components com, Size s, int r, Color c, Player e) {
        super(p, com, s, r, c);

        target = e;
        
        movement.setSpeed(5);

        renderer = new EnemyRenderer();
    }

    public boolean canSee(SquareEntity enemy) {
    	Shape intersect = Shape.intersect(fov, enemy.getHitbox());
        return intersect.getBoundsInLocal().getWidth() != -1;
    }

    public void followPlayer() {
        if (EntityManager.isManaged(this, target) && canSee(target)) {
            movement.move(target.getPosition().subtract(this.getPosition()));
        }
    }
    
    @Override
	protected void tick(double timeDelta) {
    	followPlayer();
	}

	public int getRadius() {
		return (int) fov.getRadius();
	}
}
