package org.bioshock.entities.items.powerup_items;

import org.bioshock.audio.AudioManager;
import org.bioshock.entities.Entity;
import org.bioshock.entities.items.food.Food;
import org.bioshock.entities.players.Hider;

public class BombItem extends Food {

    /**
     * Path to the image that should be displayed
     */
    private static final String PATH = Food.class.getResource(
            "/org/bioshock/images/food/bomb.png"
    ).getPath();

    public BombItem(long seed) { super(PATH, seed); }

    /**
     * When collected by a hider, set the item to teleport the hider through the vent to the position that he initially had
     * @param hider the entity that has collected
     */

    @Override
    protected void apply(Hider hider){

        hider.setDead(true);
        this.destroy();
    }

    /**
     * Plays bomb sound effect
     */
    @Override
    protected void playCollectSound(){
        AudioManager.playBombSfx();
    }

}
