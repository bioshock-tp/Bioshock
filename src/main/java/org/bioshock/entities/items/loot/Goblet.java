package org.bioshock.entities.items.loot;

public class Goblet extends Loot {
    private static final String PATH = Loot.class.getResource(
        "/org/bioshock/images/items/loot/loot01goblet.png"
    ).getPath();

    public Goblet(long seed) {
        super(PATH, seed);
    }
}
