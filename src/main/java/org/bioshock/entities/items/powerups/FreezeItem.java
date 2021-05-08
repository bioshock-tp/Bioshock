package org.bioshock.entities.items.powerups;

import org.bioshock.audio.AudioManager;
import org.bioshock.entities.players.Hider;
import org.bioshock.entities.players.SeekerAI;

import javafx.scene.paint.Color;

public class FreezeItem extends PowerUp {
    /**
     * Path to the image that should be displayed
     */
    private static final String PATH = PowerUp.class.getResource(
        "/org/bioshock/images/items/ice_ball.png"
    ).getPath();

    /**
     * The nearest seeker to the entity
     */
    private SeekerAI nearestSeeker;

    /**
     * The unchanged speed of the nearest seeker
     */
    private double oldSpeed;

    public FreezeItem(long seed) { super(PATH, seed); }


    @Override
    protected void apply(Hider hider) {
        nearestSeeker = findNearestSeeker();
        oldSpeed = nearestSeeker.getMovement().getSpeed();
        nearestSeeker.getMovement().setSpeed(0);
        nearestSeeker.getColourAdjust().setHue(Color.BLUE.getHue());
    }


    @Override
    protected void revert(Hider hider) {
        nearestSeeker.getMovement().setSpeed(oldSpeed);
        nearestSeeker.getColourAdjust().setHue(0);
    }


    @Override
    protected void playCollectSound() {
        AudioManager.playFreezeSfx();
    }
}