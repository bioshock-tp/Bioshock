package org.bioshock.engine.physics;

import org.bioshock.engine.entity.SquareEntity;
import org.bioshock.main.App;

import javafx.geometry.Point2D;

public class Movement {
    private int speed = 10;

    private int dispX = 0;
    private int dispY = 0;

    private SquareEntity entity;

    public Movement(SquareEntity entity) {
        this.entity = entity;
    }
    
    public void tick(double timeDelta) {
        Point2D displacement = getDisplacement();
        if (displacement.getX() == 0 && displacement.getY() == 0) return;
    	move(displacement);
    }

    public void move(Point2D trans) {
        Point2D target = trans.add(entity.getPosition());
        int x = entity.getX();
        int y = entity.getY();
              		
        if (x != target.getX()) {
            int disp = (int) target.getX() - x;
            x += disp / Math.abs(disp) * speed;
        }
        if (y != target.getY()) {
            int disp = (int) target.getY() - y;
            y += disp / Math.abs(disp) * speed;
        }
        entity.setPosition(x, y);
    }

    public void displace(int displacementX, int displacementY) {
        dispX += displacementX;
        dispY += displacementY;
    }

    public void setSpeed(int newSpeed) {
        speed = newSpeed;
    }

	private Point2D getDisplacement() {
        Point2D displacement = new Point2D(dispX, dispY);
        dispX = 0;
        dispY = 0;
		return displacement;
	}

    public int getSpeed() {
        return speed;
    }
}
