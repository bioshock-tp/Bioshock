package org.bioshock.entities.items.loot;

public class Bag extends Loot {
    private static final String PATH = Loot.class.getResource(
        "/org/bioshock/images/items/loot/loot03bag.png"
    ).getPath();

    public Bag(long seed) {
        super(PATH, seed);
    }
}
