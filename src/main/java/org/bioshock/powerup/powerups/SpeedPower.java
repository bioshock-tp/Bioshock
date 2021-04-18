package org.bioshock.powerup.powerups;

import org.bioshock.entities.SquareEntity;
import org.bioshock.powerup.PowerUp;

public class SpeedPower extends PowerUp {

    double speedIncrease = 4;
    double oldSpeed;

    public SpeedPower(SquareEntity entity, double duration){
        super(entity, duration);
        oldSpeed = entity.getMovement().getSpeed();
    }

    @Override
    protected void action() {
        entity.getMovement().setSpeed(oldSpeed + speedIncrease);
    }

    @Override
    protected void revert() {
        entity.getMovement().setSpeed(oldSpeed);
    }
}
