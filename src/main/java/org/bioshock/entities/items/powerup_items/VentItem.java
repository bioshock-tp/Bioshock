package org.bioshock.entities.items.powerup_items;

import org.bioshock.components.NetworkC;
import org.bioshock.entities.items.Item;
import org.bioshock.entities.items.food.Food;
import org.bioshock.entities.players.Hider;
import org.bioshock.utils.Size;

public class VentItem extends Item {
    /**
     * Path to the image that should be displayed
     */
    private static final String PATH = Food.class.getResource(
        "/org/bioshock/images/food/vent.png"
    ).getPath();

    public VentItem(long seed) {
        super(
            spawn(seed),
            new Size(DEFAULT_SIZE, DEFAULT_SIZE),
            new NetworkC(false),
            PATH
        );
    }


    @Override
    protected void apply(Hider hider) {
        hider.setPosition(
            hider.getInitPositionX(),
            hider.getInitPositionY()
        );
    }


    @Override
    protected void playCollectSound() {
        // TODO
    }


    @Override
    protected void tick(double timeDelta) {
        /* This entity does not change */
    }
}
