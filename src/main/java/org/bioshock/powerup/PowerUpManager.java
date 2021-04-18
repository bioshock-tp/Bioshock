package org.bioshock.powerup;

import org.bioshock.entities.SquareEntity;
import org.bioshock.powerup.powerups.FreezePower;
import org.bioshock.powerup.powerups.InvisiblePower;
import org.bioshock.powerup.powerups.SpeedPower;

public class PowerUpManager {

    private final FreezePower freezePower;
    private final SpeedPower speedPower;
    private final InvisiblePower invisiblePower;

    public PowerUpManager(SquareEntity entity){
        freezePower = new FreezePower(entity, 10);
        speedPower = new SpeedPower(entity, 10);
        invisiblePower = new InvisiblePower(entity, 10);
    }

    public void tick(double timeDelta){
        freezePower.tick(timeDelta);
        speedPower.tick(timeDelta);
        invisiblePower.tick(timeDelta);
    }

    public FreezePower getFreezePower() {
        return freezePower;
    }

    public SpeedPower getSpeedPower() {
        return speedPower;
    }

    public InvisiblePower getInvisiblePower() {
        return invisiblePower;
    }

}
