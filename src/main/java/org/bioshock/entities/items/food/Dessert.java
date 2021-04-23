package org.bioshock.entities.items.food;

import lombok.Builder;
import org.bioshock.entities.Entity;
import org.bioshock.entities.players.Hider;
import org.bioshock.scenes.MainGame;
import org.bioshock.scenes.SceneManager;

public class Dessert extends Food {
    private static final String PATH = Food.class.getResource(
        "/org/bioshock/images/food/iceCube2.png"
    ).getPath();

    public Dessert() { super(PATH); }

    @Override
    protected void apply(Entity entity){
        if(entity instanceof Hider){
            ((Hider) entity).getPowerUpManager().getFreezePower().start();
        }
        ((MainGame) SceneManager.getScene()).collectFood();
    }
}
