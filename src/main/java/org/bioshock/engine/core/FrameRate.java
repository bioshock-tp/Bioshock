package org.bioshock.engine.core;

import org.bioshock.scenes.SceneManager;

import javafx.scene.control.Label;

public class FrameRate {
    private static final int N = 100;
    private static final long[] frames = new long[N];
    private static Label label;
    private static int frameTimeIndex = 0;
    private static boolean arrayFilled = false;

    private FrameRate() {}

    public static void initialise() {
        label = new Label("0");
        updatePosition();
        SceneManager.getPane().getChildren().add(label);
    }

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
            double frameRate = 1e9 / elapsedNanosPerFrame;
            label.setText(String.format("%.0f", frameRate));
        }
    }

    public static void updatePosition() {
        if (label != null) {
            label.setTranslateX(WindowManager.getWindowWidth() / 2 - 10);
            label.setTranslateY(10 - WindowManager.getWindowHeight() / 2);
        }
    }
}
