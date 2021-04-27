package org.bioshock.entities.powerup.powerups;

import java.util.Comparator;

import org.bioshock.entities.EntityManager;
import org.bioshock.entities.SquareEntity;
import org.bioshock.entities.players.SeekerAI;
import org.bioshock.entities.powerup.PowerUp;

public class FreezePower extends PowerUp {

    /**
     * The nearest seeker to the entity
     */
    SeekerAI nearestSeeker;

    /**
     * The unchanged speed of the nearest seeker
     */
    double oldSpeed;

    public FreezePower(SquareEntity entity, double duration) {
        super(entity, duration);
    }

    /**
     * Finds the nearest seeker using the entity manager
     * @return the nearest seeker
     */
    public SeekerAI findNearestSeeker() {
        return EntityManager.getSeekers().stream()
            .min(Comparator.comparing(seeker ->
                seeker.getCentre().subtract(entity.getCentre()).magnitude()
            ))
            .orElse(EntityManager.getSeekers().iterator().next());
    }

    /**
     * Stores the nearest seekers old speed and sets its speed to 0
     */
    @Override
    protected void action() {
        nearestSeeker = findNearestSeeker();
        oldSpeed = nearestSeeker.getMovement().getSpeed();
        nearestSeeker.getMovement().setSpeed(0);
    }

    /**
     * Returns the nearest seekers speed back to the original value
     */
    @Override
    protected void revert() {
        nearestSeeker.getMovement().setSpeed(oldSpeed);
    }
}
