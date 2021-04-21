package org.bioshock.engine.core;

import org.bioshock.entities.LabelEntity;
import org.bioshock.scenes.GameScene;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class FrameRate {
    private static final int N = 100;
    private static final long[] frames = new long[N];
    private static LabelEntity label = new LabelEntity(
        new Point3D(GameScene.getGameScreen().getWidth() - 100, 50, 100),
        "0",
        new Font("arial", 20),
        3,
        Color.BLACK
    );
    private static int frameTimeIndex = 0;
    private static boolean arrayFilled = false;

    private FrameRate() {}

    public static final void tick(long now) {
        long oldFrameTime = frames[frameTimeIndex];
        frames[frameTimeIndex] = now;
        frameTimeIndex = (frameTimeIndex + 1) % N;

        if (frameTimeIndex == 0) {
            arrayFilled = true;
        }

        if (arrayFilled) {
            long elapsedNanos = now - oldFrameTime;
            long elapsedNanosPerFrame = elapsedNanos / N;
            double frameRate = Math.min(1e9 / elapsedNanosPerFrame, 999);

            label.setLabel(String.format("%.0f", frameRate));
        }
    }

    public static LabelEntity getLabel() {
        return label;
    }
}
