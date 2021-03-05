package org.bioshock.engine.entity;

import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.input.InputManager;
import org.bioshock.engine.renderers.SeekerHumanRenderer;

public class SeekerHuman extends SpriteEntity {
    private SquareEntity target;
    private Arc swatterHitbox;

    private boolean isActive = false;

    public SeekerHuman(Point3D p, NetworkC com, int r) {
        super(p, com, r);


        movement.setSpeed(5);

        renderer = SeekerHumanRenderer.class;

        swatterHitbox = new Arc(getCentre().getX(), getCentre().getY(), 150,150,30, 120);
        swatterHitbox.setType(ArcType.ROUND);

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

    public void doActions(){
        if (
                EntityManager.isManaged(this, target)
                        && intersects(target, "swatter")
        ) {
            setActive(true);
            if(target instanceof Hider){
                ((Hider) target).setDead(true);
            }
            rendererC.setColor(Color.GREEN);
        }
        if (
                EntityManager.isManaged(this, target)
                        && intersects(target, "fov")
        ) {
            movement.move(target.getPosition().subtract(this.getPosition()));
        }
    }

    @Override
    protected void tick(double timeDelta) {
        doActions();
        setSwatterPos();
        movement.tick(timeDelta);
    }

    public void setActive(boolean b){isActive = b;}

    public void setSwatterPos(){
        swatterHitbox.setCenterX(getCentre().getX());
        swatterHitbox.setCenterY(getCentre().getY());
    }

    public void setSwatterRot(){
        double r = movement.getFacingRotate(target.getPosition().subtract(this.getPosition()));
        swatterHitbox.setStartAngle(390-r);
    }

//    public void doActions() {
//        if (
//                EntityManager.isManaged(this, target)
//                        && intersects(target, "swatter")
//        ) {
//            setActive(true);
//            if(target instanceof Hider){
//                ((Hider) target).setDead(true);
//            }
//            rendererC.setColor(Color.GREEN);
//        }
//        if (
//                EntityManager.isManaged(this, target)
//                        && intersects(target, "fov")
//        ) {
//            movement.move(target.getPosition().subtract(this.getPosition()));
//        }
//    }

    public boolean intersects(SquareEntity entity, String type) {
        Shape intersect;
        Rectangle entityHitbox = new Rectangle(
                entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight()
        );

        switch(type){
            case "fov":
                Circle fovC = new Circle(getCentre().getX(), getCentre().getY(), getRadius());
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

    private void searchForPlayer(){
        //TODO: Add independent enemy movement from room to room
    }


    public Arc getSwatterHitbox(){return swatterHitbox;}

    public SquareEntity getTarget(){return target;}

    public boolean getIsActive(){return isActive;}

    public Image getImage() {
        return super.getImage();
    }



}
