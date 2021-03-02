package org.bioshock.engine.entity;

import static org.bioshock.main.App.logger;

import java.util.UUID;

import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.components.RendererC;
import org.bioshock.engine.renderers.Renderer;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class Entity extends Parent {
    protected Rectangle hitbox = null;

    protected double z;
    protected boolean enabled = true;
    protected final UUID uuid = UUID.randomUUID();

    protected NetworkC networkC = null;
    protected RendererC rendererC = null;
    protected Class<? extends Renderer> renderer;

    protected Entity(Point3D p, Rectangle h, NetworkC netC, RendererC renC) {
        hitbox = h;
        hitbox.setFill(Color.TRANSPARENT);
        
        setPosition(p);

        z = p.getZ();

        networkC = netC;
        rendererC = renC;

        logger.info("New {} with ID {}", this, uuid);
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

        hitbox.setTranslateX(x);
        hitbox.setTranslateY(y);
    }

    public void setPosition(Point2D point) {
        setPosition((int) point.getX(), (int) point.getY());
	}

    public void setPosition(Point3D point) {
        setPosition((int) point.getX(), (int) point.getY());
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

    public Rectangle getHitbox() {
		if (hitbox == null) {
            logger.error(
                "Hitbox not implemented for {}",
                getClass().getSimpleName()
            );
        }
        return hitbox;
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

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
