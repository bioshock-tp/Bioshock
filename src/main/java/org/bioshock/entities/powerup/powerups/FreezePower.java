package org.bioshock.entities.powerup.powerups;

import org.bioshock.engine.core.WindowManager;
import org.bioshock.entities.EntityManager;
import org.bioshock.entities.SquareEntity;
import org.bioshock.entities.players.SeekerAI;
import org.bioshock.entities.powerup.PowerUp;

public class FreezePower extends PowerUp {

    SeekerAI nearestSeeker;
    double oldSpeed;

    public FreezePower(SquareEntity entity, double duration){
        super(entity, duration);
    }

    public SeekerAI findNearestSeeker(){
        SeekerAI nearest = EntityManager.getSeeker().get(0);

        double shortest = WindowManager.getWindowWidth() * WindowManager.getWindowHeight();
        double temp;

        for(SeekerAI s : EntityManager.getSeeker()){
            temp = ((SeekerAI) s).getCentre().subtract(entity.getCentre()).magnitude();
            if(temp < shortest){
                shortest = temp;
                nearest = (SeekerAI) s;
            
            }
        }

        return nearest;
    }


    @Override
    protected void action() {
        nearestSeeker = findNearestSeeker();
        oldSpeed = nearestSeeker.getMovement().getSpeed();
        nearestSeeker.getMovement().setSpeed(0);
    }

    @Override
    protected void revert() {
        nearestSeeker.getMovement().setSpeed(oldSpeed);
    }
}
