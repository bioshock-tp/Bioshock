package org.bioshock.engine.core;

import org.bioshock.engine.entity.EntityManager;
import org.bioshock.engine.networking.NetworkManager;
import org.bioshock.engine.rendering.RenderManager;

import javafx.animation.AnimationTimer;

public final class GameLoop extends AnimationTimer {
	private long prev = 0;

	@Override
	public void handle(long now) {
		long nanoSDelta = now - prev;
		double sDelta = nanoSDelta / 10e9;

        NetworkManager.tick();
        EntityManager.tick(sDelta);
        RenderManager.tick();
        FrameRate.tick(now);

		prev = now;
	}
}
