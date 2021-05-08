package org.bioshock.entities.items.powerups;

import java.util.Comparator;

import org.bioshock.components.NetworkC;
import org.bioshock.entities.EntityManager;
import org.bioshock.entities.items.Item;
import org.bioshock.entities.players.Hider;
import org.bioshock.entities.players.SeekerAI;
import org.bioshock.scenes.SceneManager;
import org.bioshock.utils.Size;

import javafx.animation.PauseTransition;
import javafx.util.Duration;

public abstract class PowerUp extends Item {
    /**
     * The default length of power ups
     */
    private static final int DEFAULT_LENGTH = 5;

    /**
     * Time in seconds the effects lasts
     */
    protected int length = DEFAULT_LENGTH;

    /**
     * Creates a power up that spawns in a random location based on the seed
     * provided. Effect lasts for length seconds
     * @param path path to image
     * @param seed seed of map
     */
    protected PowerUp(String path, long seed) {
        super(
            spawn(seed),
            new Size(DEFAULT_SIZE, DEFAULT_SIZE),
            new NetworkC(false),
            path
        );
    }


    @Override
    protected void tick(double timeDelta) {
        /* These entities do not change */
    }


    @Override
    protected void collect(Hider hider) {
        super.collect(hider);
        SceneManager.getMainGame().increasePowerUpScore(hider);
        PauseTransition pause = new PauseTransition(Duration.seconds(length));
        pause.setOnFinished(e -> revert(hider));
        pause.play();
    }


    /**
     * Reverts the effect of this {@link PowerUp}
     * @param hider The hider that collected this item originally
     */
    protected abstract void revert(Hider hider);


    /**
     * @return the nearest {@link SeekerAI} to this {@link PowerUp}
     */
    protected SeekerAI findNearestSeeker() {
        return EntityManager.getSeekers().stream()
            .min(Comparator.comparing(seeker ->
                seeker.getCentre().subtract(this.getCentre()).magnitude()
            ))
            .orElse(EntityManager.getSeekers().iterator().next());
    }
}
