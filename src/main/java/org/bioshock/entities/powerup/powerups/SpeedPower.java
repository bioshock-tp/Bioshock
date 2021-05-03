package org.bioshock.entities.powerup.powerups;

import org.bioshock.entities.SquareEntity;
import org.bioshock.entities.powerup.PowerUp;

public class SpeedPower extends PowerUp {

    /**
     * New speed to change to
     */
    double newspeed = 12;

    /**
     * Unchanged value of the speed
     */
    double oldSpeed;

    public SpeedPower(SquareEntity entity, double duration) {
        super(entity, duration);
        oldSpeed = entity.getMovement().getSpeed();
    }

    /**
     * Sets the entity speed to the new speed
     */
    @Override
    protected void action() {
        entity.getMovement().setSpeed(newspeed);
    }

    /**
     * Sets the entity speed back to the old speed
     */
    @Override
    protected void revert() { entity.getMovement().setSpeed(oldSpeed); }
}
