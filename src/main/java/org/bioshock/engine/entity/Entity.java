package org.bioshock.engine.entity;

import java.util.UUID;

import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.components.RendererC;
import org.bioshock.engine.renderers.Renderer;
import org.bioshock.main.App;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Parent;

public abstract class Entity extends Parent {
    protected int z;
    protected NetworkC networkC = null;
    protected RendererC rendererC = null;
	protected final UUID uuid = UUID.randomUUID();
    protected boolean enabled = true;
    protected Class<? extends Renderer> renderer;

    protected Entity(Point3D pos, NetworkC netC, RendererC renC) {
        setPosition((int) pos.getX(), (int) pos.getY());
        z = (int) pos.getZ();
        networkC = netC;
        rendererC = renC;

        App.logger.info("New Entity {} with ID {}", (Object) this, this.uuid);
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

	public void setRenderC(RendererC renderC) {
		this.rendererC = renderC;
	}

    public void setNetwokC(NetworkC component) {
        this.networkC = component;
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

	public Class<? extends Renderer> getRenderer() {
		return renderer;
	}

	public RendererC getRendererC() {
		return rendererC;
	}

    public NetworkC getNetworkC() {
        return networkC;
    }

}