package org.bioshock.engine.physics;

import static org.bioshock.main.App.logger;

import org.bioshock.engine.ai.Swatter;
import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.entity.EntityManager;
import org.bioshock.engine.entity.SquareEntity;
import org.bioshock.entities.TexRectEntity;

import javafx.geometry.Point2D;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

public class Movement {
    private int speed = 10;

    private int xDirection = 0;
    private int yDirection = 0;

    private SquareEntity entity;

    public Movement(SquareEntity entity) {
        this.entity = entity;
    }

    private Point2D getDirection() {
		return new Point2D(xDirection, yDirection);
	}

    public void tick() {
        if (xDirection != 0 || yDirection != 0) move(getDirection());
    }

    public void move(Point2D trans) {
        Point2D target = trans.add(entity.getPosition());

        int x = entity.getX();
        int y = entity.getY();

        int dispX = (int) target.getX() - x;
        if (x != target.getX()) {
            x += dispX / Math.abs(dispX) * speed;
        }

        int dispY = (int) target.getY() - y;
        if (y != target.getY()) {
            y += dispY / Math.abs(dispY) * speed;
        }

        updateFacing(trans);

        while (x < 0) x++;
        while (y < 0) y++;
        while (x + entity.getWidth() > WindowManager.getWindowWidth()) x--;
        while (y + entity.getHeight() > WindowManager.getWindowHeight()) y--;

        int oldX = entity.getX();
        int oldY = entity.getY();
        entity.setPosition(x, y);

        EntityManager.getEntityList().forEach(child ->{
            if (
                   child == entity
                || child instanceof TexRectEntity
                || child instanceof Swatter
            ) return;

            Shape intersects = Shape.intersect(
                entity.getHitbox(),
                child.getHitbox()
            );
            if (intersects.getBoundsInLocal().getWidth() != -1) {
                logger.debug("{} collided with {}", entity, child);
                entity.setPosition(oldX, oldY);
            }
        });
    }

    public void direction(int newXDirection, int newYDirection) {
        int newX = Math.abs(xDirection + newXDirection);
        if (newX <= speed) xDirection += newXDirection;

        int newY = Math.abs(yDirection + newYDirection);
        if (newY <= speed) yDirection += newYDirection;
    }

    public void updateFacing(Point2D trans){
        double rotation = Math.atan2(trans.getX(), -trans.getY())*180/Math.PI;
        setRotation(rotation);
    }

    public void rotate(double degree) {
        Rotate rotate = entity.getRotation();
        Point2D pos = entity.getCentre();

        rotate.setPivotX(pos.getX());
        rotate.setPivotY(pos.getY());

        setRotation(entity.getRotation().getAngle() + degree);
    }

    public void setRotation(double newDegree) {
        Rotate rotate = entity.getRotation();
        Point2D pos = entity.getCentre();

        rotate.setPivotX(pos.getX());
        rotate.setPivotY(pos.getY());

        rotate.setAngle(newDegree);
    }

    public void setRotation(double newDegree, Point2D pivot) {
        Rotate rotate = entity.getRotation();

        rotate.setPivotX(pivot.getX());
        rotate.setPivotY(pivot.getY());

        rotate.setAngle(newDegree);
    }

    public void setSpeed(int newSpeed) {
        speed = newSpeed;
    }

    public int getSpeed() {
        return speed;
    }
}
