package org.bioshock.engine.entity;

import javafx.geometry.Point3D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import org.bioshock.engine.ai.Seeker;
import org.bioshock.engine.ai.Swatter;
import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.input.InputManager;

public class SeekerHuman extends Seeker {
    private SquareEntity target;
    private Swatter swatter;

    public SeekerHuman(Point3D p, NetworkC com, Size s, int r, Color c, Hider e) {
        super(p, com, s, r, c, e);

        final int speed = movement.getSpeed();

        target = getTarget();
        swatter = getSwatter();

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
                KeyCode.SPACE, () -> getSwatter().setShouldSwat(true)
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
                EntityManager.isManaged(this, target, swatter)
                        && intersects(target, "swatter")
                        && target instanceof Hider
        ) {
            ((Hider) target).setDead(true);
        }
    }

    @Override
    protected void tick(double timeDelta) {
        doActions();
        setSwatterPos();
        if(!swatter.shouldSwat()){
            setSwatterRot();
        }
        movement.tick(timeDelta);
    }

}
