package org.bioshock.entities.items.food;

public class HotDog extends Food {
    private static final String PATH = Food.class.getResource(
        "/org/bioshock/images/food/hot_dog.png"
    ).getPath();

    public HotDog() { super(PATH); }
}
