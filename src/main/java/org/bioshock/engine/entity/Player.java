package org.bioshock.engine.entity;

import org.bioshock.engine.input.InputManager;
import org.bioshock.engine.renderers.PlayerRenderer;

import javafx.geometry.Point3D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Player extends SquareEntity {
    protected final Circle fov;

    public Player(Point3D pos, Size size, int r, Color c) {
    	super(pos, size, c);

        fov = new Circle(pos.getX(), pos.getY(), r);

        renderer = new PlayerRenderer();

        InputManager.addKeyListener(
            KeyCode.W,
            () -> movement.displace(0, -movement.getSpeed())
        );

        InputManager.addKeyListener(
            KeyCode.A,
            () -> movement.displace(-movement.getSpeed(), 0)
        );

        InputManager.addKeyListener(
            KeyCode.S,
            () -> movement.displace(0,  movement.getSpeed())
        );

        InputManager.addKeyListener(
            KeyCode.D,
            () -> movement.displace(movement.getSpeed(),  0)
        );
    }

	@Override
	protected void tick(double timeDelta) {
		movement.tick(timeDelta);
	}

	public int getRadius() {
		return (int) fov.getRadius();
	}
}
