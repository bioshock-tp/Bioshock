package org.bioshock.entities.items.food;

public class Donut extends Food {
    private static final String PATH = Food.class.getResource(
        "/org/bioshock/images/food/donut.png"
    ).getPath();

    public Donut(long seed) { super(PATH, seed); }
}
