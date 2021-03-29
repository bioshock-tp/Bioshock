package org.bioshock.entities.items.food;

public class Burger extends Food {
    private static final String PATH = Food.class.getResource(
        "/org/bioshock/images/food/burger.png"
    ).getPath();

    public Burger() { super(PATH); }
}
