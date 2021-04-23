package org.bioshock.entities.items.food;

import org.bioshock.entities.Entity;
import org.bioshock.entities.players.Hider;
import org.bioshock.scenes.MainGame;
import org.bioshock.scenes.SceneManager;

public class Dessert extends Food {
    private static final String PATH = Food.class.getResource(
            "/org/bioshock/images/food/dessert.png"
    ).getPath();

    public Dessert(long seed) { super(PATH, seed); }
}
