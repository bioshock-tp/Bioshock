package org.bioshock.entities.items.powerups;

import org.bioshock.audio.AudioManager;
import org.bioshock.entities.players.Hider;

public class SpeedItem extends PowerUp {
    /**
     * Path to the image that should be displayed
     */
    private static final String PATH = PowerUp.class.getResource(
        "/org/bioshock/images/items/lightning.png"
    ).getPath();

    /**
     * New speed to change to
     */
    private static final double NEW_SPEED = 12;

    /**
     * Unchanged value of the speed
     */
    private double oldSpeed;

    public SpeedItem(long seed) { super(PATH, seed); }


    @Override
    protected void apply(Hider hider) {
        oldSpeed = hider.getMovement().getSpeed();
        hider.getMovement().setSpeed(NEW_SPEED);
        hider.getColourAdjust().setHue(0.15);
        hider.getColourAdjust().setContrast(0.2);
    }


    @Override
    protected void revert(Hider hider) {
        hider.getMovement().setSpeed(oldSpeed);
        hider.getColourAdjust().setHue(0);
        hider.getColourAdjust().setContrast(0);
    }


    @Override
    protected void playCollectSound() {
        AudioManager.playFastAirSfx();
    }
}
