package org.bioshock.entities.powerup.powerups;

import java.util.Comparator;

import org.bioshock.entities.EntityManager;
import org.bioshock.entities.SquareEntity;
import org.bioshock.entities.players.SeekerAI;
import org.bioshock.entities.powerup.PowerUp;

public class FreezePower extends PowerUp {

    SeekerAI nearestSeeker;
    double oldSpeed;

    public FreezePower(SquareEntity entity, double duration) {
        super(entity, duration);
    }

    public SeekerAI findNearestSeeker() {
        return EntityManager.getSeekers().stream()
            .min(Comparator.comparing(seeker ->
                seeker.getCentre().subtract(entity.getCentre()).magnitude()
            ))
            .orElse(EntityManager.getSeekers().iterator().next());
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
