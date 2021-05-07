package org.bioshock.entities.items.powerup_items;

import org.bioshock.audio.AudioManager;
import org.bioshock.entities.Entity;
import org.bioshock.entities.items.food.Food;
import org.bioshock.entities.players.Hider;

public class FreezeItem extends Food {
    /**
     * Path to the image that should be displayed
     */
    private static final String PATH = Food.class.getResource(
        "/org/bioshock/images/food/ice_ball.png"
    ).getPath();

    public FreezeItem(long seed) { super(PATH, seed); }

    /**
     * Set the item to activate the freeze power up when collected by a hider
     * @param entity the entity that has collected
     */
    @Override
    protected void apply(Entity entity){
        if(entity instanceof Hider){
            ((Hider) entity).getPowerUpManager().getFreezePower().start();
        }
    }

    /**
     * Plays frozen sound effect
     */
    @Override
    protected void playCollectSound(){
        AudioManager.playFreezeSfx();
    }
}