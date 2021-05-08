package org.bioshock.entities.items.powerups;

import org.bioshock.audio.AudioManager;
import org.bioshock.entities.players.Hider;

public class InvisibilityItem extends PowerUp {
    /**
     * Path to the image that should be displayed
     */
    private static final String PATH = PowerUp.class.getResource(
        "/org/bioshock/images/items/dark_ghost.png"
    ).getPath();


    public InvisibilityItem(long seed) { super(PATH, seed); }


    @Override
    protected void apply(Hider hider) {
        hider.setInvisible(true);
        hider.getColourAdjust().setBrightness(-0.5);
    }


    @Override
    protected void revert(Hider hider) {
        hider.setInvisible(false);
        hider.getColourAdjust().setBrightness(0);
    }


    @Override
    protected void playCollectSound() {
        AudioManager.playGhostSfx();
    }
}
