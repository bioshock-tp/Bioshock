package org.bioshock.engine.core;

import org.bioshock.entities.EntityManager;
import org.bioshock.networking.NetworkManager;
import org.bioshock.rendering.RenderManager;
import org.bioshock.scenes.SceneManager;

import javafx.animation.AnimationTimer;

public final class GameLoop extends AnimationTimer {
    private static final double LOGICRATE = 60;
    private static final double START = System.nanoTime();
    private static final double NS_PER_UPDATE = (1/LOGICRATE)*1e9;
    
    double previous = System.nanoTime();
    double lag = 0.0;

    public static double currentGameTime;

    @Override
    public void handle(long now) {
        currentGameTime = (now - START) / 1e9;

        if (!SceneManager.inGame() || previous == 0) {
        	previous = now;
            return;
        }
        
        double current = now;
        double elapsed = current - previous;
        previous = current;
        lag += elapsed;
        
        while (lag >= NS_PER_UPDATE)
        {
        	NetworkManager.tick();
            EntityManager.tick(1/LOGICRATE);
            SceneManager.getScene().logicTick(1/LOGICRATE);
            lag -= NS_PER_UPDATE;
        }

        SceneManager.getScene().renderTick(0);
        RenderManager.tick();
        FrameRate.tick(now);
    }

	public static double getCurrentGameTime() {
        return currentGameTime;
    }
}
