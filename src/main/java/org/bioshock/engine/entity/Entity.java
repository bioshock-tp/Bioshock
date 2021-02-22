package org.bioshock.engine.entity;

import java.util.UUID;

import org.bioshock.engine.renderers.Renderer;
import org.bioshock.main.App;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Parent;

public abstract class Entity extends Parent {
    protected int z;

	protected final UUID uuid = UUID.randomUUID();
    protected boolean enabled = true;
    protected Renderer renderer;
	
    protected Entity(Point3D pos) {
        setPosition((int) pos.getX(), (int) pos.getY());
        z = (int) pos.getZ();
	}
    
    protected abstract void tick(double timeDelta);

    public final void safeTick(double timeDelta) {
		if (enabled) {
			this.tick(timeDelta);
		}
	}

    public boolean isEnabled() {
        return enabled;
    }

    public void setPosition(int x, int y) {
        setTranslateX(x);
        setTranslateY(y);
    }

    public void setPosition(Point2D point) {
        setTranslateX(point.getX());
        setTranslateY(point.getY());
	}

	public UUID getID() {
		return uuid;
	}

    public Point2D getPosition() {
        return new Point2D(getTranslateX(), getTranslateY());
    }

	public int getX() {
		return (int) getTranslateX();
	}

	public int getY() {
		return (int) getTranslateY();
	}

    public double getZ() {
        return z;
    }

	public Renderer getRenderer() {
		return renderer;
	}
}
