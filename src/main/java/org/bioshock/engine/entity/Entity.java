package org.bioshock.engine.entity;

import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.components.RendererC;
import org.bioshock.engine.renderers.Renderer;
import org.bioshock.main.App;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public abstract class Entity {
    protected Point position;
    protected Rectangle hitbox;

    protected String UUID = java.util.UUID.randomUUID().toString();
    protected double z;
    protected NetworkC networkC;
    protected RendererC rendererC;

    protected boolean enabled = true;

    protected Class<? extends Renderer> renderer;

    protected Entity(Point3D p, Rectangle h, NetworkC netC, RendererC renC) {
        position = new Point(p.getX(), p.getY());

        hitbox = h;
        hitbox.setFill(Color.TRANSPARENT);

        setPosition(position);

        networkC = netC;
        rendererC = renC;

        rendererC.setZ(p.getZ());

        App.logger.info("New {} with ID {}", this, UUID);
	}

    protected abstract void tick(double timeDelta);

    public final void safeTick(double timeDelta) {
		if (enabled) {
			this.tick(timeDelta);
		}
	}

    public boolean intersects(Entity entity) {
        Shape intersects = Shape.intersect(
            this.getHitbox(),
            entity.getHitbox()
        );

        return (intersects.getBoundsInLocal().getWidth() != -1);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setX(double newX) {
		setPosition(newX, getY());
	}

	public void setY(double newY) {
		setPosition(getX(), newY);
	}

    public void setPosition(double x, double y) {
        position.setX(x);
        position.setY(y);

        hitbox.setTranslateX(x + hitbox.getWidth() / 2);
        hitbox.setTranslateY(y + hitbox.getHeight() / 2);
    }

    public void setPosition(Point point) {
        setPosition(point.getX(), point.getY());
	}

    public void setPosition(Point3D point) {
        setPosition(point.getX(), point.getY());
	}

	public void setRenderC(RendererC renderC) {
		this.rendererC = renderC;
	}

    public void setNetwokC(NetworkC component) {
        this.networkC = component;
    }

    public void setID(String newID) {
        UUID = newID;
    }

	public String getID() {
		return UUID;
	}

    public Point getPosition() {
        return position;
    }

	public double getX() {
		return position.getX();
	}

	public double getY() {
		return position.getY();
	}

    public double getZ() {
        return rendererC.getZ();
    }

    public Rectangle getHitbox() {
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
