package org.bioshock.powerup;

import org.bioshock.entities.SquareEntity;

public abstract class PowerUp {
    protected SquareEntity entity;
    protected double DURATION;
    private double elapsed = 0;
    private boolean isActive = false;

    protected PowerUp(SquareEntity entity, double DURATION){
        this.entity = entity;
        this.DURATION = DURATION;
    }

    public void tick(double timeDelta){
        if(isActive){
            if(elapsed > DURATION){
                revert();
                elapsed = 0;
            }
            else{
                elapsed += timeDelta;
            }
        }
    }

    public void start(){
        if(!isActive){
            setActive(true);
            action();
        }
    }

    protected abstract void action();

    protected abstract void revert();

    protected void setActive(boolean b){
        isActive = b;
    }

    public boolean getActive(){
        return isActive;
    }


}
