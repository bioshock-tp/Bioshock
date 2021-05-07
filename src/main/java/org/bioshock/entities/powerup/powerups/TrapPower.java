package org.bioshock.entities.powerup.powerups;

import java.util.Comparator;

import org.bioshock.entities.EntityManager;
import org.bioshock.entities.SquareEntity;
import org.bioshock.entities.players.SeekerAI;
import org.bioshock.entities.powerup.PowerUp;

public class TrapPower extends PowerUp {

    /**
     * The unchanged speed of the hider
     */
    double oldSpeed;

    public TrapPower(SquareEntity entity, double duration) {
        super(entity, duration);
        oldSpeed = entity.getMovement().getSpeed();
    }

    /**
     * Stores the nearest seekers old speed and sets its speed to 0
     */
    @Override
    protected void action() {
        entity.getMovement().setSpeed(0);
    }

    /**
     * Returns the nearest seekers speed back to the original value
     */
    @Override
    protected void revert() {
        entity.getMovement().setSpeed(oldSpeed);
    }
}
