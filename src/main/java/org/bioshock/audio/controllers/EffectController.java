package org.bioshock.audio.controllers;

import java.nio.file.Path;

import org.bioshock.audio.settings.EffectSettings;

import javafx.application.Platform;
import javafx.scene.media.AudioClip;
import lombok.NonNull;

public class EffectController {
    /** The audio clip used to play the effect file. */
    private final AudioClip clip;

    /**
     * Constructs a new Effect object.
     *
     * @param path
     *          The path of the effect file.
     */
    public EffectController(final @NonNull Path path) {
        clip = new AudioClip(path.toUri().toString());
    }

    /**
     * Starts playback.
     *
     * @see AudioClip#play()
     *
     * @param settings
     *          The settings to apply to the effect, before starting playback.
     */
    public void play(EffectSettings settings) {
        if (settings != null) {
            settings = settings.deepCopy();
            clip.setVolume(settings.getVolume());
            clip.setCycleCount(settings.getCycleCount());
        }

        Platform.runLater(() -> {
//            App.logger.debug(
//                "Playing Effect: {}",
//                clip.getSource()
//            );

            clip.play();
        });
    }

    /**
     * Stops playback.
     *
     * @see AudioClip#stop()
     */
    public void stop() {
        Platform.runLater(() -> {
//                App.logger.debug(
//                    "Stopping Effect: ",
//                    clip.getSource()
//                );

            clip.stop();
        });
    }
}