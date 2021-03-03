package org.bioshock.engine.entity;

import javafx.geometry.Point3D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import org.bioshock.engine.ai.SeekerAI;
import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.input.InputManager;

public class SeekerHuman extends SeekerAI {
    private SquareEntity target;

    public SeekerHuman(Point3D p, NetworkC com, Size s, int r, Color c, Hider e) {
        super(p, com, s, r, c, e);

        final int speed = movement.getSpeed();

        target = getTarget();

        InputManager.onPressListener(
                KeyCode.W, () -> movement.direction(0, -speed)
        );
        InputManager.onPressListener(
                KeyCode.A, () -> movement.direction(-speed, 0)
        );
        InputManager.onPressListener(
                KeyCode.S, () -> movement.direction(0,  speed)
        );
        InputManager.onPressListener(
                KeyCode.D, () -> movement.direction(speed,  0)
        );

        InputManager.onPressListener(
                KeyCode.UP, () -> {
                    getSwatterHitbox().setStartAngle(30);
                    setActive(true);
                }
        );
        InputManager.onPressListener(
                KeyCode.LEFT, () -> {
                    getSwatterHitbox().setStartAngle(120);
                    setActive(true);
                }
        );
        InputManager.onPressListener(
                KeyCode.DOWN, () -> {
                    getSwatterHitbox().setStartAngle(210);
                    setActive(true);
                }
        );
        InputManager.onPressListener(
                KeyCode.RIGHT, () -> {
                    getSwatterHitbox().setStartAngle(300);
                    setActive(true);
                }
        );


        InputManager.onReleaseListener(
                KeyCode.W, () -> movement.direction(0,  speed)
        );
        InputManager.onReleaseListener(
                KeyCode.A, () -> movement.direction(speed,  0)
        );
        InputManager.onReleaseListener(
                KeyCode.S, () -> movement.direction(0, -speed)
        );
        InputManager.onReleaseListener(
                KeyCode.D, () -> movement.direction(-speed, 0)
        );


    }

    @Override
    public void doActions(){
        if (
                EntityManager.isManaged(this, target)
                        && intersects(target, "swatter")
        ) {
            if(getIsActive()){
                if(target instanceof Hider){
                    ((Hider) target).setDead(true);
                }
                rendererC.setColor(Color.GREEN);
            }

        }
    }

    @Override
    protected void tick(double timeDelta) {
        doActions();
        setSwatterPos();
        movement.tick(timeDelta);
    }

}
