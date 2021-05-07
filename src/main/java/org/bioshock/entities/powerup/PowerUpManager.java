package org.bioshock.entities.powerup;

import org.bioshock.entities.SquareEntity;
import org.bioshock.entities.powerup.powerups.FreezePower;
import org.bioshock.entities.powerup.powerups.InvisiblePower;
import org.bioshock.entities.powerup.powerups.SpeedPower;
import org.bioshock.entities.powerup.powerups.TrapPower;

public class PowerUpManager {

    /**
     * Instance of the freeze power
     */
    private final FreezePower freezePower;

    /**
     * Instance of the speed power
     */
    private final SpeedPower speedPower;

    /**
     * Instance of the invisible power
     */
    private final InvisiblePower invisiblePower;

    /**
     * Instance of the trap power
     */
    private final TrapPower trapPower;

    public PowerUpManager(SquareEntity entity){
        freezePower = new FreezePower(entity, 5);
        speedPower = new SpeedPower(entity, 5);
        invisiblePower = new InvisiblePower(entity, 5);
        trapPower = new TrapPower(entity, 5);
    }

    /**
     * Performs the actions needed every frame
     * @param timeDelta time since last frame
     */
    public void tick(double timeDelta){
        freezePower.tick(timeDelta);
        speedPower.tick(timeDelta);
        invisiblePower.tick(timeDelta);
        trapPower.tick(timeDelta);
    }

    /**
     * @return the created instance of freeze power
     */
    public FreezePower getFreezePower() {
        return freezePower;
    }

    /**
     * @return the created instance of speed power
     */
    public SpeedPower getSpeedPower() {
        return speedPower;
    }

    /**
     * @return the created instance of invisible power
     */
    public InvisiblePower getInvisiblePower() {
        return invisiblePower;
    }

    public TrapPower getTrapPower() { return trapPower; }
}
