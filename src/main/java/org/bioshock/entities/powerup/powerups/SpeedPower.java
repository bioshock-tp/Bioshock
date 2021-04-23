package org.bioshock.entities.powerup.powerups;

import org.bioshock.entities.SquareEntity;
import org.bioshock.entities.powerup.PowerUp;

public class SpeedPower extends PowerUp {

    double newspeed = 15;
    double oldSpeed;

    public SpeedPower(SquareEntity entity, double duration){
        super(entity, duration);
        oldSpeed = entity.getMovement().getSpeed();
    }

    @Override
    protected void action() {
        entity.getMovement().setSpeed(newspeed);
    }

    @Override
    protected void revert() {
        entity.getMovement().setSpeed(oldSpeed);
    }
}
