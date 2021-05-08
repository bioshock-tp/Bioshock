package org.bioshock.entities.items.loot;

public class Coins extends Loot {
    private static final String PATH = Loot.class.getResource(
        "/org/bioshock/images/items/loot/loot04coins.png"
    ).getPath();

    public Coins(long seed) {
        super(PATH, seed);
    }
}
