package org.bioshock.engine.core;

import org.bioshock.entities.LabelEntity;
import org.bioshock.scenes.GameScene;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class FrameRate {
    /**
     * The number of frame updates required to calculate framerate
     */
    private static final int N = 100;

    /**
     * An array of time in-between each game tick
     */
    private static final long[] frames = new long[N];

    /**
     * The label that displays the framerate on-screen
     */
    private static LabelEntity label = new LabelEntity(
        new Point3D(GameScene.getGameScreen().getWidth() - 100, 50, 100),
        "0",
        new Font("arial", 20),
        3,
        Color.BLACK
    );

    /**
     * Index after latest frame update
     */
    private static int frameTimeIndex = 0;

    /**
     * True if {@link #frames} has been filled
     */
    private static boolean arrayFilled = false;


    private FrameRate() {}


    /**
     * Updates {@link #frames} with latest time elapsed between game ticks
     * @param now The time since the game was launched
     */
    public static final void tick(long now) {
        long previousTickTime = frames[frameTimeIndex];
        frames[frameTimeIndex] = now;
        frameTimeIndex = (frameTimeIndex + 1) % N;

        if (frameTimeIndex == 0) {
            arrayFilled = true;
        }

        if (arrayFilled) {
            long elapsedNanos = now - previousTickTime;
            long elapsedNanosPerFrame = elapsedNanos / N;
            double frameRate = Math.min(1e9 / elapsedNanosPerFrame, 999);

            label.setLabel(String.format("%.0f", frameRate));
        }
    }


    /**
     * Used to add this {@link Entity} to the game's managers
     * @return The {@link LabelEntity} responsible for displaying the current
     * frame rate
     */
    public static LabelEntity getLabel() {
        return label;
    }
}
