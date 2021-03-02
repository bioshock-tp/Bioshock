package org.bioshock.audio;

import javafx.concurrent.Task;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URISyntaxException;

public class AudioController {
    private MediaPlayer bgMusic;
    //private Preferences prefs;
    private Thread bgMusicThread;

    public AudioController(boolean play) throws URISyntaxException {
        playMusic(play);
    }

    public void playMusic(boolean b) throws URISyntaxException {
        bgMusicThread = new Thread(new Task<>() {
            @Override
            protected Object call() throws URISyntaxException {
                bgMusic = new MediaPlayer(new Media(getClass().getResource("backgroundMusic.mp3").toURI().toString()));
                if (b) {
                    bgMusic.play();
                } else {
                    bgMusic.stop();
                }
                return null;
            }
        });
        bgMusicThread.start();
        //bgMusic.setAutoPlay(b);
    }

    public boolean musicPlaying() {
        this.bgMusic.getStatus();
        return this.bgMusic.getStatus().equals(MediaPlayer.Status.READY);
    }
}
