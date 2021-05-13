package org.bioshock.entities.items;

import org.bioshock.audio.AudioManager;
import org.bioshock.components.NetworkC;
import org.bioshock.entities.players.Hider;
import org.bioshock.utils.Size;

public class Teleporter extends Item {
    /**
     * Path to the image that should be displayed
     */
    private static final String PATH = Item.class.getResource(
        "/org/bioshock/images/items/teleport.png"
    ).getPath();

    public Teleporter(long seed) {
        super(
            spawn(seed),
            new Size(DEFAULT_SIZE, DEFAULT_SIZE),
            new NetworkC(true),
            PATH
        );
    }


    @Override
    protected void apply(Hider hider) {
        hider.setTeleported(true);
        hider.setPosition(
            hider.getInitPositionX(),
            hider.getInitPositionY()
        );
    }


    @Override
    protected void playCollectSound() {
        AudioManager.playTeleportSfx();
    }

    protected void tick(double timeDelta) {}
}
