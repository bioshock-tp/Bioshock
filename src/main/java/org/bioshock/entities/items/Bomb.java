package org.bioshock.entities.items;

import org.bioshock.audio.AudioManager;
import org.bioshock.components.NetworkC;
import org.bioshock.entities.items.powerups.PowerUp;
import org.bioshock.entities.players.Hider;
import org.bioshock.utils.Size;

public class Bomb extends Item {
    /**
     * Path to the image that should be displayed
     */
    private static final String PATH = PowerUp.class.getResource(
        "/org/bioshock/images/items/bomb.png"
    ).getPath();

    public Bomb(long seed) {
        super(
            spawn(seed),
            new Size(DEFAULT_SIZE, DEFAULT_SIZE),
            new NetworkC(true),
            PATH
        );
    }


    @Override
    protected void apply(Hider hider){
        hider.setBombed(true);
        hider.setDead(true);
    }


    @Override
    protected void playCollectSound(){
        AudioManager.playBombSfx();
    }

    protected void tick(double timeDelta) {}
}
