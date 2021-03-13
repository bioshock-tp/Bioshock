package org.bioshock.engine.entity;

import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.input.InputManager;
import org.bioshock.engine.renderers.components.SimpleRendererC;

import javafx.geometry.Point3D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class SeekerHuman extends SquareEntity {
    private Arc swatterHitbox;

    private boolean isActive = false;

    public SeekerHuman(Point3D p, NetworkC com, Size s, int r, Color c) {
        super(p, com, new SimpleRendererC(), s, r, c);

        movement.initMovement();

        InputManager.onPress(
            KeyCode.UP, () -> {
                getSwatterHitbox().setStartAngle(30);
                setActive(true);
            }
        );
        InputManager.onPress(
            KeyCode.LEFT, () -> {
                getSwatterHitbox().setStartAngle(120);
                setActive(true);
            }
        );
        InputManager.onPress(
            KeyCode.DOWN, () -> {
                getSwatterHitbox().setStartAngle(210);
                setActive(true);
            }
        );
        InputManager.onPress(
            KeyCode.RIGHT, () -> {
                getSwatterHitbox().setStartAngle(300);
                setActive(true);
            }
        );
    }

    private boolean intersects(SquareEntity entity, String type) {
        Shape intersect;
        Rectangle entityHitbox = new Rectangle(
            entity.getX(),
            entity.getY(),
            entity.getWidth(),
            entity.getHeight()
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
        movement.tick(timeDelta);
    }

    public void doActions() {
        EntityManager.getPlayers().forEach(player -> {
            if (
                intersects(player, "swatter")
                && getIsActive()
            ) {
                player.setDead(true);
                rendererC.setColor(Color.GREEN);
            }
        });
    }

    public void setSwatterPos() {
        swatterHitbox.setCenterX(getCentre().getX());
        swatterHitbox.setCenterY(getCentre().getY());
    }

    public void setActive(boolean b) { isActive = b; }

    public Arc getSwatterHitbox() { return swatterHitbox; }

    public boolean getIsActive() { return isActive; }
}
