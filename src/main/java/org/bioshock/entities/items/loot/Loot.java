package org.bioshock.entities.items.loot;

import org.bioshock.audio.AudioManager;
import org.bioshock.components.NetworkC;
import org.bioshock.entities.items.Item;
import org.bioshock.entities.players.Hider;
import org.bioshock.scenes.SceneManager;
import org.bioshock.utils.Size;

public abstract class Loot extends Item {
    /**
     * Creates a room in a random room in a random location
     * @param path Path to this entities image file
     */
    protected Loot(String path, long seed) {
        super(
            spawn(seed),
            new Size(DEFAULT_SIZE, DEFAULT_SIZE),
            new NetworkC(true),
            path
        );
    }


    @Override
    protected void apply(Hider hider) {
        SceneManager.getMainGame().collectLoot(hider);
    }


    @Override
    protected void playCollectSound() {
        AudioManager.playPlinkSfx();
    }

    protected void tick(double timeDelta) {}
}
