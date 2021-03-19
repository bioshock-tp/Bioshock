package org.bioshock.engine.core;

import org.bioshock.entities.EntityManager;
import org.bioshock.networking.NetworkManager;
import org.bioshock.rendering.RenderManager;
import org.bioshock.scenes.SceneManager;

import javafx.animation.AnimationTimer;

public final class GameLoop extends AnimationTimer {
    private static final double LOGICRATE = 60;
    private static final double START = System.nanoTime();

    private long prev = 0;
    private long lastLogicTick = 0;

    public static double currentGameTime;

    @Override
    public void handle(long now) {
        currentGameTime = (now - START) / 1e9;

        if (!SceneManager.inGame() || prev == 0) {
            prev = now;
            lastLogicTick = now;

            return;
        }

        double sDelta = (now - prev) / 1e9;

        double tickDelta = (now - lastLogicTick) / 1e9;
        if (tickDelta * 1e9 >= 1 / LOGICRATE) {
            NetworkManager.tick();
            EntityManager.tick(tickDelta);
            SceneManager.getScene().logicTick(tickDelta);

            lastLogicTick = now;
        }

        SceneManager.getScene().renderTick(sDelta);
        RenderManager.tick();
        FrameRate.tick(now);

        prev = now;
    }

	public static double getCurrentGameTime() {
        return currentGameTime;
    }
}
