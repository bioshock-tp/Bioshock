package org.bioshock.engine.core;

import org.bioshock.entities.LabelEntity;
import org.bioshock.main.App;
import org.bioshock.scenes.GameScene;
import org.bioshock.scenes.SceneManager;

import javafx.geometry.Point3D;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class FrameRate {
    private static final int N = 100;
    private static final long[] frames = new long[N];
    private static LabelEntity label = new LabelEntity(
            new Point3D(GameScene.getGameScreen().getWidth()-100, 50, 100), 
            "Frame Rate", 
            new Font("arial", 20), 
            50, 
            Color.BLACK);;
    private static int frameTimeIndex = 0;
    private static boolean arrayFilled = false;

    private FrameRate() {}

    public static void initialise() {
    }

    public static final void tick(long now) {
        long oldFrameTime = frames[frameTimeIndex];
        frames[frameTimeIndex] = now;
        frameTimeIndex = (frameTimeIndex + 1) % N;
        App.logger.debug("frameTimeIndex = " + frameTimeIndex);

        if (frameTimeIndex == 0) {
            arrayFilled = true;
        }

        if (arrayFilled) {
//            App.logger.debug("changing Label");
            long elapsedNanos = now - oldFrameTime;
            long elapsedNanosPerFrame = elapsedNanos / N;
            double frameRate = 1e9 / elapsedNanosPerFrame;
            label.getStringBuilder().setLength(0);
            App.logger.debug("Label value = " + String.format("%.0f", frameRate));
            label.getStringBuilder().append(String.format("%.0f", frameRate));
        }
    }

    public static void updatePosition() {
//        if (label != null) {
//            label.setTranslateX(WindowManager.getWindowWidth() / 2 - 10);
//            label.setTranslateY(10 - WindowManager.getWindowHeight() / 2);
//        }
    }

    public static LabelEntity getLabel() {
        return label;
    }
}
