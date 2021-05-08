package org.bioshock.entities.items.powerups;

import org.bioshock.audio.AudioManager;
import org.bioshock.entities.players.Hider;
import org.bioshock.scenes.SceneManager;

import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class Trap extends PowerUp {
    /**
     * Path to the image that should be displayed
     */
    private static final String PATH = PowerUp.class.getResource(
        "/org/bioshock/images/items/trap.png"
    ).getPath();

    /**
     * The unchanged speed of the hider
     */
    private double oldSpeed;

    public Trap(long seed) { super(PATH, seed); }


    @Override
    protected void collect(Hider hider) {
        SceneManager.getMainGame().increasePowerUpScore(hider);
        PauseTransition pause = new PauseTransition(Duration.seconds(length));
        pause.setOnFinished(e -> revert(hider));
        pause.play();
    }


    @Override
    protected void apply(Hider hider) {
        oldSpeed = hider.getMovement().getSpeed();
        hider.getMovement().setSpeed(0);
    }


    @Override
    protected void revert(Hider hider) {
        hider.getMovement().setSpeed(oldSpeed);
    }


    @Override
    protected void playCollectSound(){
        AudioManager.playTrapSfx();
    }
}
