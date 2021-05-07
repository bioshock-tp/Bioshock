package org.bioshock.entities.items.powerup_items;

import org.bioshock.audio.AudioManager;
import org.bioshock.entities.items.food.Food;
import org.bioshock.entities.players.Hider;

public class InvisibilityItem extends Food {
    /**
     * Path to the image that should be displayed
     */
    private static final String PATH = Food.class.getResource(
        "/org/bioshock/images/food/dark_ghost.png"
    ).getPath();

    public InvisibilityItem(long seed) { super(PATH, seed); }


    @Override
    protected void apply(Hider hider) {
        hider.getPowerUpManager().getInvisiblePower().start();
    }


    @Override
    protected void playCollectSound() {
        AudioManager.playGhostSfx();
    }
}
