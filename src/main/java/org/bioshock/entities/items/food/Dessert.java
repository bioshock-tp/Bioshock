package org.bioshock.entities.items.food;

public class Dessert extends Food {
    private static final String PATH = Food.class.getResource(
        "/org/bioshock/images/food/dessert.png"
    ).getPath();

    public Dessert(long seed) { super(PATH, seed); }
}
