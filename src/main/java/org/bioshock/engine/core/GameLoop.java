package org.bioshock.engine.core;

import org.bioshock.engine.entity.EntityManager;
import org.bioshock.engine.networking.NetworkManager;
import org.bioshock.engine.rendering.RenderManager;
import org.bioshock.engine.scene.SceneManager;

import javafx.animation.AnimationTimer;

public final class GameLoop extends AnimationTimer {
    private static final int LOGICRATE = 60;

	private long prev = 0;
    private long lastUpdate = 0;
    final static double startNanoTime = System.nanoTime();
    static double currentGameTime;

	@Override
	public void handle(long now) {
		long nanoSDelta = now - prev;
        double sDelta = nanoSDelta / 1e9;
        currentGameTime = (now - startNanoTime) / 1e9;
        RenderManager.tick();

        if (now - lastUpdate >= LOGICRATE) {
            NetworkManager.tick();
            EntityManager.tick(sDelta);
            SceneManager.getScene().renderTick(sDelta);

            lastUpdate = now;
        }
        
        FrameRate.tick(now);
        
		prev = now;
	}

	public static double getCurrentGameTime() {
        return currentGameTime;
    }

}
