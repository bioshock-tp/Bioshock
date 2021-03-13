package org.bioshock.engine.core;

import org.bioshock.engine.entity.EntityManager;
import org.bioshock.engine.networking.NetworkManager;
import org.bioshock.engine.rendering.RenderManager;
import org.bioshock.engine.scene.SceneManager;
import org.bioshock.main.App;

import javafx.animation.AnimationTimer;

public final class GameLoop extends AnimationTimer {
    private static final int LOGICRATE = 60;

	private long prev = 0;
    private long lastUpdate = 0;

	@Override
	public void handle(long now) {
		long nanoSDelta = now - prev;
		double sDelta = nanoSDelta / 10e9;
        RenderManager.tick();
        SceneManager.getScene().renderTick(sDelta);

        if (now - lastUpdate >= LOGICRATE) {
            NetworkManager.tick();
            EntityManager.tick(sDelta);
			

            lastUpdate = now;
        }
        
        FrameRate.tick(now);
        
		prev = now;
	}
}
