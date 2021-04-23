package org.bioshock.engine.core;

import org.bioshock.entities.EntityManager;
import org.bioshock.networking.NetworkManager;
import org.bioshock.physics.Collisions;
import org.bioshock.rendering.RenderManager;
import org.bioshock.scenes.SceneManager;

import javafx.animation.AnimationTimer;

public final class GameLoop extends AnimationTimer {
    private static final double LOGICRATE = 60;
    private static final double START = System.nanoTime();
    private static final double NS_PER_UPDATE = (1 / LOGICRATE) * 1e9;
    /***
     * the bigger this is the smaller the minimum fps
     * if this is set to 1 it will attempt to have a fps of at least 60
     */
    private static final int FPS_FACTOR = 20;

    private static double currentGameTime;

    double previous = System.nanoTime();
    double lag = 0.0;


    @Override
    public void handle(long now) {
        currentGameTime = (now - START) / 1e9;

        if (!SceneManager.inGame() || previous == 0) {
            previous = now;
            lag = 0.0;
            return;
        }

        double timeDelta = 1 / LOGICRATE;
        double current = now;
        double elapsed = current - previous;
        previous = current;

        lag += elapsed;

        for (int i = 0; lag >= NS_PER_UPDATE && i<FPS_FACTOR;i++) {
            logicTick(timeDelta);
            lag -= NS_PER_UPDATE;
        }
        previous = System.nanoTime();

        renderTick(now);
    }

    public static double getCurrentGameTime() {
        return currentGameTime;
    }

    private static void logicTick(double timeDelta) {
        NetworkManager.tick();
        EntityManager.tick(timeDelta);
        Collisions.tick(timeDelta);
        SceneManager.getScene().logicTick(timeDelta);
    }

    private static void renderTick(long now) {
        SceneManager.getScene().renderTick(0);
        RenderManager.tick();
        FrameRate.tick(now);
    }
}
