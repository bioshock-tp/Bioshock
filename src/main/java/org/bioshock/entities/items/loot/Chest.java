package org.bioshock.entities.items.loot;

public class Chest extends Loot {
    private static final String PATH = Loot.class.getResource(
        "/org/bioshock/images/items/loot/loot06chest.png"
    ).getPath();

    public Chest(long seed) {
        super(PATH, seed);
    }
}
