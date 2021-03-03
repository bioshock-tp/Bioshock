package org.bioshock.engine.ai;

import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.entity.EntityManager;
import org.bioshock.engine.entity.Hider;
import org.bioshock.engine.entity.Size;
import org.bioshock.engine.entity.SquareEntity;
import org.bioshock.engine.renderers.SeekerRenderer;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class SeekerAI extends SquareEntity {
    private SquareEntity target;
    private Arc swatterHitbox;

    private boolean isActive = false;
	
    public SeekerAI(Point3D p, NetworkC com, Size s, int r, Color c, Hider e) {
        super(p, com, s, r, c);

        target = e;
        
        movement.setSpeed(5);

        renderer = SeekerRenderer.class;

        swatterHitbox = new Arc(getCentre().getX(), getCentre().getY(), 150,150,30, 120);
        swatterHitbox.setType(ArcType.ROUND);

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

    public void doActions() {
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

    public boolean intersects(SquareEntity entity, String type) {
        Shape intersect;
        Rectangle entityHitbox = new Rectangle(
            entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight()
        );
        entityHitbox.getTransforms().add(entity.getRotation());

        switch(type){
            case "fov":
                intersect = Shape.intersect(fov, entity.getHitbox());
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


	public Arc getSwatterHitbox(){return swatterHitbox;}

    public SquareEntity getTarget(){return target;}

    public boolean getIsActive(){return isActive;}

}
