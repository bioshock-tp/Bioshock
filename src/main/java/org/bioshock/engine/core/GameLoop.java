package org.bioshock.engine.core;

import org.bioshock.engine.entity.EntityManager;
import org.bioshock.engine.networking.NetworkManager;
import org.bioshock.engine.rendering.RenderManager;
import org.bioshock.engine.scene.SceneManager;

import javafx.animation.AnimationTimer;

public final class GameLoop extends AnimationTimer {
    private static final double LOGICRATE = 60;

    private long prev = 0;
    private long lastLogicTick = 0;

    @Override
    public void handle(long now) {
        if (!SceneManager.inGame()) {
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
}
