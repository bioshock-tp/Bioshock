package org.bioshock.entities.powerup;

import org.bioshock.entities.SquareEntity;
import org.bioshock.entities.players.Hider;
import org.bioshock.scenes.SceneManager;

public abstract class PowerUp {
    /**
     * The entity that has collected the power up
     */
    protected SquareEntity entity;

    /**
     * The duration of the power up
     */
    protected double duration;

    /**
     * the time elapsed since the power up was activated
     */
    private double elapsed = 0;

    /**
     * Whether the power up is active
     */
    private boolean isActive = false;

    protected PowerUp(SquareEntity entity, double duration) {
        this.entity = entity;
        this.duration = duration;
    }


    /**
     * Performs the actions needed every frame
     * @param timeDelta time since last frame
     */
    public void tick(double timeDelta) {
        if(isActive) {
            if(elapsed > duration) {
                setActive(false);
                revert();
                elapsed = 0;
            }
            else{
                elapsed += timeDelta;
            }
        }
    }


    /**
     * Starts the power up and calls the action
     */
    public void start() {
        if (!isActive) {
            setActive(true);
            action();
            SceneManager.getMainGame().increasePowerUpScore((Hider) entity);
        }
    }


    /**
     * The effect of the power up (i.e. increase speed)
     */
    protected abstract void action();


    /**
     * Reverts the effect of the power up (i.e. set speed back to original speed)
     */
    protected abstract void revert();


    /**
     * @param b the value to set isActive to
     */
    protected void setActive(boolean b) {
        isActive = b;
    }


    /**
     * @return the value of isActive
     */
    public boolean isActive() {
        return isActive;
    }
}
