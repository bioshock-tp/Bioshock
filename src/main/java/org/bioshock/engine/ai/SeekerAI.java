package org.bioshock.engine.ai;

import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.entity.EntityManager;
import org.bioshock.engine.entity.Hider;
import org.bioshock.engine.entity.Size;
import org.bioshock.engine.entity.SquareEntity;
import org.bioshock.engine.renderers.SeekerRenderer;
import org.bioshock.engine.renderers.components.SimpleRendererC;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class SeekerAI extends SquareEntity {
    private Hider target;
    private Arc swatterHitbox;

    private boolean isActive = false;

    public SeekerAI(Point3D p, NetworkC com, Size s, int r, Color c, Hider e) {
        super(p, com, new SimpleRendererC(), s, r, c);

        target = e;

        movement.setSpeed(3);

        renderer = SeekerRenderer.class;

        swatterHitbox = new Arc(
            getCentre().getX(),
            getCentre().getY(),
            50, 50, 30, 120
        );
        swatterHitbox.setType(ArcType.ROUND);
    }

    private boolean intersects(SquareEntity entity, String type) {
        Shape intersect;
        Rectangle entityHitbox = new Rectangle(
            entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight()
        );

        switch(type) {
            case "fov":
                Circle fovC = new Circle(
                    getCentre().getX(),
                    getCentre().getY(),
                    getRadius()
                );
                intersect = Shape.intersect(fovC, entityHitbox);
                break;

            case "swatter":
                intersect = Shape.intersect(swatterHitbox, entityHitbox);
                break;
            default:
                return false;
        }

        return intersect.getBoundsInLocal().getWidth() != -1;
    }

	protected void tick(double timeDelta) {
        doActions();
        setSwatterPos();
        setSwatterRot();
	}

    public void doActions() {
        EntityManager.getPlayers().forEach(entity -> {
            if (
                EntityManager.isManaged(this, entity)
                && intersects(entity, "swatter")
                && !entity.isDead()
            ) {
                setActive(true);
                entity.setDead(true);
                rendererC.setColor(Color.GREEN);
            }
            if (
                EntityManager.isManaged(this, entity)
                && intersects(entity, "fov")
                && !entity.isDead()
            ) {
                target = entity;
                movement.move(target.getPosition().subtract(getPosition()));

            }
        });
    }

    public void setActive(boolean b) { isActive = b; }

    public void setSwatterPos() {
        swatterHitbox.setCenterX(getCentre().getX());
        swatterHitbox.setCenterY(getCentre().getY());
    }

    public void setSwatterRot() {
        double r = movement.getFacingRotate(
            target.getPosition().subtract(getPosition())
        );
        swatterHitbox.setStartAngle(390-r);
    }

	public Arc getSwatterHitbox() { return swatterHitbox; }

    public SquareEntity getTarget() { return target; }

    public boolean getIsActive() { return isActive; }
}
