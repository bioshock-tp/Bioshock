package org.bioshock.audio;

import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import lombok.NonNull;
import org.bioshock.audio.settings.MusicSettings;

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
    public MusicController(final @NonNull Path path) {
        player = new MediaPlayer(new Media(path.toFile().toURI().toString()));
    }

    /** @see MediaPlayer#dispose() */
    public void dispose() {
        if (!hasBeenDisposed) {
            stop();
            Platform.runLater(player::dispose);

            hasBeenDisposed = true;

            if (AudioController.debuggingEnabled) {
                System.out.println("Disposing Music: " + player.getMedia().getSource());
            }
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
            throw new IllegalStateException("This music has been disposed, you cannot play it.");
        }

        if (settings != null) {
            settings = settings.deepCopy();
//            player.setBalance(settings.getBalance());
//            player.setCycleCount(settings.getCycleCount());
//            player.setRate(settings.getRate());
            player.setVolume(settings.getVolume());
        }

        Platform.runLater(() -> {
            if (AudioController.debuggingEnabled) {
                System.out.println("Playing Music: " + player.getMedia().getSource());
            }

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
        if (hasBeenDisposed) {
            throw new IllegalStateException("This music has been disposed, you cannot pause it.");
        }

        Platform.runLater(() -> {
            if (AudioController.debuggingEnabled) {
                System.out.println("Pausing Music: " + player.getMedia().getSource());
            }

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
            throw new IllegalStateException("This music has been disposed, you cannot stop it.");
        }

        Platform.runLater(() -> {
            if (AudioController.debuggingEnabled) {
                System.out.println("Stopping Music: " + player.getMedia().getSource());
            }

            player.stop();
        });
    }
}