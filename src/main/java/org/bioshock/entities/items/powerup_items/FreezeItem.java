package org.bioshock.entities.items.powerup_items;

import org.bioshock.entities.Entity;
import org.bioshock.entities.items.food.Food;
import org.bioshock.entities.players.Hider;

public class FreezeItem extends Food {
    private static final String PATH = Food.class.getResource(
            "/org/bioshock/images/food/ice_cube.png"
    ).getPath();

    public FreezeItem(long seed) { super(PATH, seed); }

    @Override
    protected void apply(Entity entity){
        if(entity instanceof Hider){
            ((Hider) entity).getPowerUpManager().getFreezePower().start();
        }
    }
}