package org.bioshock.engine.sprites;

import org.bioshock.engine.physics.Movement;
import org.bioshock.engine.scene.SceneController;
import org.bioshock.render.components.PlayerRendererC;
import org.bioshock.scenes.GameOverScreen;
import org.bioshock.transform.components.PlayerTransformC;

import javafx.scene.paint.Color;

public class Player extends SquareEntity {
	public Movement movement;
	public PlayerTransformC transform;
	PlayerRendererC renderer;

	boolean dead = false;

	public Player(PlayerTransformC transform, PlayerRendererC renderer,
				  int x, int y, int w, int h, double r, Color c, double z) {
		super(transform, renderer, x, y, w, h, c, z);
		this.transform = transform;
		this.transform.setRadius(r);

		this.movement = new Movement(this);

		setXYRectangle(x, y);
	}

	public Player(int x, int y, int w, int h, double r, Color c, double z) {
		this(new PlayerTransformC(), new PlayerRendererC(), x, y, w, h, r, c, z);
	}

	public boolean getDead(){
		return dead;
	}

	public void setDead(boolean d){
		dead = d;
	}

	public Movement getMovement() {
		return movement;
	}

	@Override
	public void destroy() {
		return;
	}

	@Override
	protected void tick(double timeDelta) {
		if(dead == true){
			destroy();
		}
		movement.tick(timeDelta);
	}


}
