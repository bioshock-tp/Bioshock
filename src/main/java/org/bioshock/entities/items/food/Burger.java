package org.bioshock.entities.items.food;

import org.bioshock.entities.Entity;
import org.bioshock.entities.players.Hider;
import org.bioshock.scenes.MainGame;
import org.bioshock.scenes.SceneManager;

public class Burger extends Food {
    private static final String PATH = Food.class.getResource(
        "/org/bioshock/images/food/burger.png"
    ).getPath();

    //Invisible powerup

    public Burger() { super(PATH); }

    @Override
    protected void apply(Entity entity){
        if(entity instanceof Hider){
            ((Hider) entity).getPowerUpManager().getInvisiblePower().start();
        }
        ((MainGame) SceneManager.getScene()).collectFood();
    }
}
