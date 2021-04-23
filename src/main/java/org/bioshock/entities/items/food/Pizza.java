package org.bioshock.entities.items.food;

import org.bioshock.entities.Entity;
import org.bioshock.entities.players.Hider;
import org.bioshock.scenes.MainGame;
import org.bioshock.scenes.SceneManager;

public class Pizza extends Food {
    private static final String PATH = Food.class.getResource(
        "/org/bioshock/images/food/pizza.png"
    ).getPath();

	//Pizza will give you speed boost
    public Pizza(long seed) { super(PATH, seed); }
}
