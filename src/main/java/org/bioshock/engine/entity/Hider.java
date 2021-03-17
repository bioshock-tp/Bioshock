package org.bioshock.engine.entity;

import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.networking.NetworkManager;
import org.bioshock.engine.renderers.PlayerRenderer;
import org.bioshock.engine.renderers.components.PlayerRendererC;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class Hider extends SquareEntity {
    private boolean dead = false;

    public Hider(Point3D p, NetworkC com, Size s, int r, Color c) {
        super(p, com, new PlayerRendererC(), s, r, c);

        renderer = PlayerRenderer.class;
    }

    protected void tick(double timeDelta) {
        movement.tick(timeDelta);
    }

    public void setDead(boolean d) {
        dead = d;

        if (dead) {
            rendererC.setColour(Color.GREY);
            NetworkManager.kill(this);
        }
    }

    public boolean isDead() {
        return dead;
    }

    @Override
    public Pair<Point2D, Point2D> renderArea() {
        Point2D centre = getCentre();
        double radius = getRadius();
        return new Pair<>(
            centre.subtract(radius, radius),
            centre.add(radius, radius)
        );
    }
}
