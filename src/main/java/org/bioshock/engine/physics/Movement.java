package org.bioshock.engine.physics;

import org.bioshock.engine.entity.SquareEntity;

import javafx.geometry.Point2D;
import javafx.scene.transform.Rotate;

public class Movement {
    private double speed = 10;

    private double xDirection = 0;
    private double yDirection = 0;

    private SquareEntity entity;

    public Movement(SquareEntity entity) {
        this.entity = entity;
    }

    public Point2D getDirection() {
		return new Point2D(xDirection, yDirection);
	}

    public void tick(double timeDelta) {
        if (xDirection != 0 || yDirection != 0) move(getDirection());
    }

    public void move(Point2D trans) {
        Point2D target = trans.add(entity.getPosition());
        double x = entity.getX();
        double y = entity.getY();

        if (x != target.getX()) {
            double disp = target.getX() - x;
            x += disp / Math.abs(disp) * speed;
        }
        if (y != target.getY()) {
            double disp = target.getY() - y;
            y += disp / Math.abs(disp) * speed;
        }
        entity.setPosition(x, y);
        updateFacing(trans);
    }

    public void direction(double newXDirection, double newYDirection) {
        xDirection = newXDirection;

        yDirection = newYDirection;
    }

    public void direction(Point2D targ) {
        direction(targ.getX(), targ.getY());
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

    public void setRotation(double newDegree, Point2D pos) {
        Rotate rotate = entity.getRotation();

        rotate.setPivotX(pos.getX());
        rotate.setPivotY(pos.getY());

        rotate.setAngle(newDegree);
    }

    public void setSpeed(int newSpeed) {
        speed = newSpeed;
    }

    public double getSpeed() {
        return speed;
    }
}
