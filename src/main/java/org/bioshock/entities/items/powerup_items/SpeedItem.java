package org.bioshock.entities.items.powerup_items;

import org.bioshock.entities.Entity;
import org.bioshock.entities.items.food.Food;
import org.bioshock.entities.players.Hider;
import org.bioshock.scenes.MainGame;
import org.bioshock.scenes.SceneManager;

public class SpeedItem extends Food {
    private static final String PATH = Food.class.getResource(
            "/org/bioshock/images/food/running_shoes.png"
    ).getPath();

    public SpeedItem(long seed) { super(PATH, seed); }

    @Override
    protected void apply(Entity entity){
        if(entity instanceof Hider){
            ((Hider) entity).getPowerUpManager().getSpeedPower().start();
        }
    }
}
