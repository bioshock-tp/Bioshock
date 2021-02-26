package org.bioshock.engine.ai;

import org.bioshock.engine.entity.Components;
import org.bioshock.engine.entity.Size;
import org.bioshock.engine.entity.SquareEntity;
import org.bioshock.engine.renderers.SwatterRenderer;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

public class Swatter extends SquareEntity {
    private Enemy enemy;

    private boolean shouldSwat = false;
    private boolean swatBack = false;

    private int angles = 0;

    public Swatter(Point3D p, Components com, Size s, Color c, Enemy enemy) {
        super(p, com, s, 0, c);

        renderer = new SwatterRenderer();

        this.enemy = enemy;
    }

    public void setEnemy(Enemy e){
        enemy = e;
    }

	public void setShouldSwat(boolean b) {
        shouldSwat = b;
	}

    public Enemy getEnemy() {
        return enemy;
    }

	public boolean shouldSwat() {
		return shouldSwat;
	}

    private void swat() {
        int s = 4;
        if (angles < 180/s) {
            movement.setRotation(
                getRotation().getAngle() + s,
                new Point2D(enemy.getRotation().getPivotX(),
                enemy.getRotation().getPivotY())
            );
            angles++;
        }
        else {
            angles = 180;
            swatBack = true;
        }
    }

    private void swatB() {
        int s = 4;
        if(angles > 0) {
            movement.setRotation(
                getRotation().getAngle() - s,
                new Point2D(enemy.getRotation().getPivotX(),
                enemy.getRotation().getPivotY())
            );
            angles -= s;
        }
        else {
            angles = 0;
            shouldSwat = false;
            swatBack = false;
        }
    }

    @Override
    protected void tick(double timeDelta) {
        if (shouldSwat) {
            if (swatBack) {
                swatB();
            }
            else {
                swat();
            }
        }
    }
}
