package org.bioshock.entities.powerup.powerups;

import org.bioshock.entities.SquareEntity;
import org.bioshock.entities.powerup.PowerUp;

public class InvisiblePower extends PowerUp {

    public InvisiblePower(SquareEntity entity, double duration) {
        super(entity, duration);
    }

    /**
     * Sets invisibility to true
     */
    @Override
    protected void action() {
        entity.setInvisible(true);
    }

    /**
     * Sets invisibility to false
     */
    @Override
    protected void revert() {
        entity.setInvisible(false);
    }
}
