package org.bioshock.entities.items.loot;

public class Key extends Loot {
    private static final String PATH = Loot.class.getResource(
        "/org/bioshock/images/items/loot/loot05key.png"
    ).getPath();

    public Key(long seed) {
        super(PATH, seed);
    }
}
