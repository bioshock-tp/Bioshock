package org.bioshock.engine.entity;

import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.renderers.PlayerRenderer;
import org.bioshock.main.App;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

public class Hider extends SquareEntity {
    private boolean dead = false;

    public Hider(Point3D p, NetworkC com, Size s, int r, Color c) {
    	super(p, com, s, r, c);

        renderer = PlayerRenderer.class;
    }

	protected void tick(double timeDelta) {
        if (dead) {
            App.logger.info("{} is dead", getID());
            dead = false;
        }
        movement.tick(timeDelta);
	}

    public void setDead(boolean d) {
        dead = d;
    }

    public boolean getDead() {
        return dead;
    }
}
