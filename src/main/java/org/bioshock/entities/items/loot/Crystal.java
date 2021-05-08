package org.bioshock.entities.items.loot;

public class Crystal extends Loot {
    private static final String PATH = Loot.class.getResource(
        "/org/bioshock/images/items/loot/loot02crystal.png"
    ).getPath();

    public Crystal(long seed) {
        super(PATH, seed);
    }
}
