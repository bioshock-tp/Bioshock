package org.bioshock.entities.players;

import org.bioshock.components.NetworkC;
import org.bioshock.entities.SquareEntity;
import org.bioshock.rendering.renderers.components.RendererC;
import org.bioshock.utils.Size;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

public class SeekerHuman extends SquareEntity {
    private boolean dead = false;

    public SeekerHuman(
        Point3D p,
        NetworkC nCom,
        RendererC rCom,
        Size s,
        int r,
        Color c
    ) {
    	super(p, nCom, rCom, s, r, c);
    }

	protected void tick(double timeDelta) {
        dead = false;
        movement.tick(timeDelta);
	}

    public void setDead(boolean d) {
        dead = d;
    }

    public boolean isDead() {
        return dead;
    }
}
