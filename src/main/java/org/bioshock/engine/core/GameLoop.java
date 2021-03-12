package org.bioshock.engine.core;

import org.bioshock.engine.entity.EntityManager;
import org.bioshock.engine.networking.NetworkManager;
import org.bioshock.engine.rendering.RenderManager;
import org.bioshock.engine.scene.SceneManager;
import org.bioshock.main.App;

import javafx.animation.AnimationTimer;

public final class GameLoop extends AnimationTimer {
	private long prev = 0;

	@Override
	public void handle(long now) {
		long nanoSDelta = now - prev;
		double sDelta = nanoSDelta / 10e9;

		NetworkManager.tick();
		EntityManager.tick(sDelta);
		SceneManager.getScene().tick(sDelta);
		RenderManager.tick();
        FrameRate.tick(now);
		prev = now;
		
//		App.logger.debug("Canavs layout x,y: " + SceneManager.getCanvas().getLayoutX() + ", " + SceneManager.getCanvas().getLayoutY());
//		App.logger.debug("Canavs translate x,y: " + SceneManager.getCanvas().getTranslateX() + ", " + SceneManager.getCanvas().getTranslateY());
	}
}
