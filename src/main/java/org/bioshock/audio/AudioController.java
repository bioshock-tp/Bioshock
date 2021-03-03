package org.bioshock.audio;

import java.net.URISyntaxException;

import org.bioshock.main.App;

import javafx.concurrent.Task;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioController {
    private static final String MUSIC = "backgroundMusic.mp3";
    private static MediaPlayer bgMusic;
    
    private AudioController() {}

    public static void playMusic() {
        final Thread bgMusicThread = new Thread(new Task<>() {
            @Override
            protected Object call() {
                try {
                    bgMusic = new MediaPlayer(new Media(getClass().getResource(MUSIC).toURI().toString()));

                    bgMusic.play();
                } catch (URISyntaxException exception) {
                    App.logger.error(
                        "{} could not be accesed. Exception: {}",
                        MUSIC,
                        exception.getMessage()
                    );
                }

                return null;
            }
        });

        bgMusicThread.start();
    }

    public static void stopMusic() {
        if (musicPlaying()) bgMusic.stop();
    }

    public static boolean musicPlaying() {
        bgMusic.getStatus();
        return bgMusic.getStatus().equals(MediaPlayer.Status.READY);
    }
}
