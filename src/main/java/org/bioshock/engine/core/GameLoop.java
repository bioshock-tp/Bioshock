package org.bioshock.engine.core;

import org.bioshock.entities.EntityManager;
import org.bioshock.networking.NetworkManager;
import org.bioshock.physics.Collisions;
import org.bioshock.rendering.RenderManager;
import org.bioshock.scenes.SceneManager;

import javafx.animation.AnimationTimer;

/**
 * 
 * @author William Holbrook
 * A class which handles the running of the game calling logic tick and 
 * render ticks at a relevant rate while also maintaining the frame rate
 */
public final class GameLoop extends AnimationTimer {
	/**
	 * How many times a second logicTick should be attempted to be called
	 */
    private static final double LOGICRATE = 60;
    /**
     * The time the game loop starts running
     */
    private static final double START = System.nanoTime();
    /**
     * The number of nano seconds per logicTick update (logically)
     */
    private static final double NS_PER_UPDATE = (1 / LOGICRATE) * 1e9;
    /***
     * the bigger this is the smaller the minimum fps
     * if this is set to 1 it will attempt to have a fps of at least 60
     * 
     * the maximum number of logic ticks before a render tick occurs
     */
    private static final int FPS_FACTOR = 20;
    /**
     * The current game time in seconds
     */
    private static double currentGameTime;
    /**
     * The time of the last call of handle in nanoSeconds
     */
    double previous = System.nanoTime();
    /**
     * The amount of lag in nano seconds
     */
    double lag = 0.0;


    @Override
    public void handle(long now) {
    	//Set the current time to the amount of elapsed time in nano seconds
        currentGameTime = (now - START) / 1e9;
        
        //If you're not in the game set the value of previous to now
        //remove your lag
        //return so no logic ticks or render ticks are called
        if (!SceneManager.inGame() || previous == 0) {
            previous = now;
            lag = 0.0;
            return;
        }
        
        double timeDelta = 1 / LOGICRATE;
        double current = now;
        double elapsed = current - previous;
        previous = current;
        
        //Add the time elapsed between the start of the last handle call
        //and the start of the current handle call to lag
        lag += elapsed;

        //only call a logic tick if there has been enough lag 
        //this makes it so you only call a logic tick at maximum 
        //LOGICRATE times per second
        //and if your computer is too slow to run 60 logic ticks and 
        //render ticks per second it will do multiple logic ticks per 
        //call of handle making it so the game still runs at 60 logic 
        //ticks per second but may have less render ticks
        for (int i = 0; lag >= NS_PER_UPDATE && i<FPS_FACTOR;i++) {
            logicTick(timeDelta);
            lag -= NS_PER_UPDATE;
        }
        previous = System.nanoTime();

        renderTick(now);
    }

    /**
     * getter
     * @return currentGameTime
     */
    public static double getCurrentGameTime() {
        return currentGameTime;
    }

    /**
     * Calls tick on all logical components  
     * @param timeDelta the time for a logic tick in seconds
     */
    private static void logicTick(double timeDelta) {
        NetworkManager.tick();
        EntityManager.tick(timeDelta);
        Collisions.tick(timeDelta);
        SceneManager.getScene().logicTick(timeDelta);
    }

    /**
     * Calls tick on all render components
     * @param now the current time in nano seconds
     */
    private static void renderTick(long now) {
        SceneManager.getScene().renderTick(0);
        RenderManager.tick();
        FrameRate.tick(now);
    }
}
