package org.bioshock.engine.entity;

import javafx.geometry.Point3D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.input.InputManager;
import org.bioshock.engine.renderers.SpriteEntityRenderer;

public class SeekerHuman extends SpriteEntity {
    private Arc swatterHitbox;

    private boolean isActive = false;

    public SeekerHuman(Point3D p, Rectangle h, NetworkC com, int r) {
        super(p, h, com, r);

        final int speed = (int) movement.getSpeed();

        renderer = SpriteEntityRenderer.class;

        InputManager.onPress(
            KeyCode.W, () -> movement.direction(0, -speed)
        );
        InputManager.onPress(
            KeyCode.A, () -> movement.direction(-speed, 0)
        );
        InputManager.onPress(
            KeyCode.S, () -> movement.direction(0,  speed)
        );
        InputManager.onPress(
            KeyCode.D, () -> movement.direction(speed,  0)
        );

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

        InputManager.onRelease(
            KeyCode.W, () -> movement.direction(0,  speed)
        );
        InputManager.onRelease(
            KeyCode.A, () -> movement.direction(speed,  0)
        );
        InputManager.onRelease(
            KeyCode.S, () -> movement.direction(0, -speed)
        );
        InputManager.onRelease(
            KeyCode.D, () -> movement.direction(-speed, 0)
        );
    }

    public void initMovement() {
        final double speed = movement.getSpeed();

        InputManager.onPress(
                KeyCode.W, () -> movement.direction(0, -speed)
        );
        InputManager.onPress(
                KeyCode.A, () -> movement.direction(-speed, 0)
        );
        InputManager.onPress(
                KeyCode.S, () -> movement.direction(0,  speed)
        );
        InputManager.onPress(
                KeyCode.D, () -> movement.direction(speed,  0)
        );

        InputManager.onRelease(
                KeyCode.W, () -> movement.direction(0,  speed)
        );
        InputManager.onRelease(
                KeyCode.A, () -> movement.direction(speed,  0)
        );
        InputManager.onRelease(
                KeyCode.S, () -> movement.direction(0, -speed)
        );
        InputManager.onRelease(
                KeyCode.D, () -> movement.direction(-speed, 0)
        );
    }

    private boolean intersects(SquareEntity entity, String type) {
        Shape intersect;
        Rectangle entityHitbox = new Rectangle(
            entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight()
        );

        switch(type){
//            case "fov":
//                Circle fovC = new Circle(
//                    getCentre().getX(),
//                    getCentre().getY(),
//                    getRadius()
//                );
//                intersect = Shape.intersect(fovC, entityHitbox);
//                break;

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
//        swatterHitbox.setCenterX(getCentre().getX());
//        swatterHitbox.setCenterY(getCentre().getY());
    }

    public void setActive(boolean b) { isActive = b; }

    public Arc getSwatterHitbox() { return swatterHitbox; }

    public boolean getIsActive() { return isActive; }
}
