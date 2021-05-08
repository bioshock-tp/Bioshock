package org.bioshock.entities.items.loot;

public class Necklace extends Loot {
    private static final String PATH = Loot.class.getResource(
        "/org/bioshock/images/items/loot/loot08necklace.png"
    ).getPath();

    public Necklace(long seed) {
        super(PATH, seed);
    }
}
