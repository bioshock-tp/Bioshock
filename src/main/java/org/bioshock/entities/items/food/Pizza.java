package org.bioshock.entities.items.food;

public class Pizza extends Food {
    private static final String PATH = Food.class.getResource(
        "/org/bioshock/images/food/pizza.png"
    ).getPath();

	//Pizza will give you speed boost
    public Pizza(long seed) { super(PATH, seed); }
}
