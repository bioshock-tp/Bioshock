package org.bioshock.entities.items.loot;

public class TreasureSack extends Loot {
    private static final String PATH = Loot.class.getResource(
        "/org/bioshock/images/items/loot/loot07treasuresack.png"
    ).getPath();

    public TreasureSack(long seed) {
        super(PATH, seed);
    }
}
