package org.bioshock.audio.controllers;

import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.bioshock.audio.settings.MusicSettings;
import org.bioshock.main.App;

import java.nio.file.Path;

public class MusicController {
    /** The media player used to play the music file. */
    private final MediaPlayer player;

    /** Whether the player has been disposed. */
    private boolean hasBeenDisposed = false;

    /**
     * Constructs a new Music object.
     *
     * @param path
     *          The path of the music file.
     */
    public MusicController(final Path path) {
        Media media = new Media(path.toFile().toURI().toString());
        player = new MediaPlayer(media);
    }

    /** @see MediaPlayer#dispose() */
    public void dispose() {
        if (!hasBeenDisposed) {
            stop();
            Platform.runLater(player::dispose);

            hasBeenDisposed = true;

            App.logger.debug(
                "Disposing Music: {}",
                player.getMedia().getSource()
            );
        }
    }

    /**
     * Starts playback.
     *
     * @see MediaPlayer#play()
     *
     * @param settings
     *          The settings to apply to the music, before starting playback.
     */
    public void play(MusicSettings settings) {
        if (hasBeenDisposed) {
            try {
                throw new IllegalStateException(
                    "This music has been disposed, you cannot play it."
                );
            } catch (IllegalStateException e) {
                App.logger.error(e);
            }
        }

        if (settings != null) {
            settings = settings.deepCopy();
            player.setVolume(settings.getVolume());
            player.setCycleCount(settings.getCycleCount());
        }

        Platform.runLater(() -> {
            App.logger.debug(
                "Playing Music: {}",
                player.getMedia().getSource()
            );

            player.play();
        });
        player.seek(Duration.ZERO);
    }

    /**
     * Pauses playback.
     *
     * @see MediaPlayer#pause()
     */
    public void pause() {
        try {
            throw new IllegalStateException(
                "This music has been disposed, you cannot pause it."
            );
        } catch (IllegalStateException e) {
            App.logger.error(e);
        }

        Platform.runLater(() -> {
            App.logger.debug(
                "Pausing Music: {}",
                player.getMedia().getSource()
            );

            player.pause();
        });
    }

    /**
     * Stops playback.
     *
     * @see MediaPlayer#stop()
     */
    public void stop() {
        if (hasBeenDisposed) {
            try {
                throw new IllegalStateException(
                    "This music has been disposed, you cannot stop it."
                );
            } catch (IllegalStateException e) {
                App.logger.error(e);
            }
        }

        Platform.runLater(() -> {
            App.logger.debug(
                "Stopping Music: {}",
                player.getMedia().getSource()
            );

            player.stop();
        });
    }

    public MediaPlayer.Status getStatus() {
        return player.getStatus();
    }
}