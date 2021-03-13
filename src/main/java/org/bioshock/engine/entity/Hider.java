package org.bioshock.engine.entity;

import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.renderers.PlayerRenderer;
import org.bioshock.engine.renderers.components.PlayerRendererC;
import org.bioshock.main.App;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

public class Hider extends SquareEntity {
    private boolean dead = false;

    public Hider(Point3D p, NetworkC com, Size s, int r, Color c) {
    	super(p, com, new PlayerRendererC(), s, r, c);

        ((PlayerRendererC) rendererC).setOriginalColour(c);

        renderer = PlayerRenderer.class;
    }

	protected void tick(double timeDelta) {
        movement.tick(timeDelta);
	}

    public void setDead(boolean d) {
        dead = d;

        if (dead) {
            rendererC.setColor(Color.GREY);

            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    App.logger.debug("Revived PogU");
                    setDead(false);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();;
                }

            }).start();
        } else {
            ((PlayerRendererC) rendererC).revertColour();
        }
    }

    public boolean isDead() {
        return dead;
    }
}
