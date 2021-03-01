package org.bioshock.engine.ai;

import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.entity.Size;
import org.bioshock.engine.entity.SquareEntity;
import org.bioshock.engine.renderers.SwatterRenderer;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

public class Swatter extends SquareEntity {
    private Seeker enemy;

    private boolean shouldSwat = false;
    private boolean swatBack = false;

    private int angles = 0;

    public Swatter(Point3D p, NetworkC com, Size s, Color c, Seeker enemy) {
        super(p, com, s, 0, c);

        renderer = SwatterRenderer.class;

        this.enemy = enemy;
    }

    public void setEnemy(Seeker e){
        enemy = e;
    }

	public void setShouldSwat(boolean b) {
        shouldSwat = b;
	}

    public Seeker getEnemy() {
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
