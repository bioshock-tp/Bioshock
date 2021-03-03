package org.bioshock.engine.core;

import java.util.List;

import org.bioshock.engine.scene.SceneManager;

import javafx.scene.Node;
import javafx.scene.control.Label;

public class FrameRate {
    private static final int N = 100;
    private static final long[] frames = new long[N];
    private static final Label label = new Label("0");
    private static int frameTimeIndex = 0;
    private static boolean arrayFilled = false;

    private FrameRate() {}

    public static final void tick(long now) {
        List<Node> children = SceneManager.getPane().getChildren();
        if (!children.contains(label)) {
            label.setTranslateX((double) WindowManager.getWindowWidth() / 2 - 10);
            label.setTranslateY(10 - (double) WindowManager.getWindowHeight() / 2);
            children.add(label);
        }

        long oldFrameTime = frames[frameTimeIndex];
        frames[frameTimeIndex] = now;
        frameTimeIndex = (frameTimeIndex + 1) % N;
        if (frameTimeIndex == 0) {
            arrayFilled = true;
        }
        if (arrayFilled) {
            long elapsedNanos = now - oldFrameTime;
            long elapsedNanosPerFrame = elapsedNanos / N;
            double frameRate = 1_000_000_000.0 / elapsedNanosPerFrame;
            label.setText(String.format("%.0f", frameRate));
        }
    }
}
