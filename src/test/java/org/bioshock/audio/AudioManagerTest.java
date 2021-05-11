package org.bioshock.audio;

import javafx.application.Platform;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

import javafx.scene.media.MediaPlayer;
import org.bioshock.gui.SettingsController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.bioshock.audio.controllers.AudioController;
import org.main.TestingApp;

import static org.bioshock.audio.AudioManager.*;
import static org.junit.jupiter.api.Assertions.*;

public class AudioManagerTest {

    private final CountDownLatch waiter = new CountDownLatch(1);

    @BeforeEach
    void setUp() {
        TestingApp.launchJavaFXThread();
    }

    @Test
    public void testInitialiseAudioControllers() {
        AudioController.initialise();
        initialiseAudioControllers();

        // Run the test
        Field[] fields = AudioManager.class.getDeclaredFields();

        // Checks if all controller fields have been initialised
        for(Field controller : fields){
            assertNotNull(controller);
        }


    }

    @Test
    public void testInitialiseBackgroundAudio() throws InterruptedException {
        // Setup
        
        initialiseBackgroundAudio();

        waiter.await(1, TimeUnit.SECONDS); // 1ms

        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        MediaPlayer.Status expectedStatus;

        // Tests if music is playing according to user's preferences
        if (prefs.getBoolean("musicOn", true)) {
            expectedStatus = MediaPlayer.Status.PLAYING;
        }
        else {
            expectedStatus = MediaPlayer.Status.READY;
        }
        assertEquals(expectedStatus, getBgMusicController().getStatus());


    }

    @Test
    public void testStopBackgroundMusic() throws InterruptedException {

        
        AudioController.initialise();
        initialiseAudioControllers();

        AudioManager.playBackgroundMusic(1.0);

        waiter.await(1, TimeUnit.SECONDS);

        // Tests if music is playing
        assertEquals(MediaPlayer.Status.PLAYING, getBgMusicController().getStatus());

        AudioManager.stopBackgroundMusic();

        waiter.await(1, TimeUnit.SECONDS);

        // Tests if music has stopped
        assertEquals(MediaPlayer.Status.STOPPED, getBgMusicController().getStatus());


    }

    @Test
    public void testPlayBackgroundMusic() throws InterruptedException {
        
        AudioController.initialise();
        initialiseAudioControllers();

        AudioManager.playBackgroundMusic(1.0);

        waiter.await(1, TimeUnit.SECONDS);

        // Tests if music is playing
        assertEquals(MediaPlayer.Status.PLAYING, getBgMusicController().getStatus());


    }

    @Test
    public void testPlayWalkingSfx() throws InterruptedException {
        
        AudioController.initialise();
        initialiseAudioControllers();

        AudioManager.playWalkingSfx();

        waiter.await(1, TimeUnit.SECONDS);

        // Tests if effect is playing
        assertTrue(getWalkingEffectController().isPlaying());


    }

    @Test
    public void testStopWalkingSfx() throws InterruptedException {
        
        AudioController.initialise();
        initialiseAudioControllers();

        AudioManager.playWalkingSfx();

        waiter.await(1, TimeUnit.SECONDS);

        // Tests if effect is playing
        assertTrue(getWalkingEffectController().isPlaying());

        AudioManager.stopWalkingSfx();

        waiter.await(1, TimeUnit.SECONDS);

        // Tests if effect has stopped
        assertFalse(getWalkingEffectController().isPlaying());


    }

    @Test
    public void testPlayWooshSfx() throws InterruptedException {
        
        AudioController.initialise();
        initialiseAudioControllers();

        AudioManager.playWooshSfx();

        waiter.await(1, TimeUnit.SECONDS);

        // Tests if effect is playing
        assertTrue(getWooshEffectController().isPlaying());


    }

    @Test
    public void testPlayPlinkSfx() throws InterruptedException {
        
        AudioController.initialise();
        initialiseAudioControllers();

        AudioManager.playPlinkSfx();

        waiter.await(1, TimeUnit.SECONDS);

        // Tests if effect is playing
        assertTrue(getPlinkEffectController().isPlaying());


    }

    @Test
    public void testPlayFreezeSfx() throws InterruptedException {
        
        AudioController.initialise();
        initialiseAudioControllers();

        AudioManager.playFreezeSfx();

        waiter.await(1, TimeUnit.SECONDS);

        // Tests if effect is playing
        assertTrue(getFreezeEffectController().isPlaying());


    }

    @Test
    public void testPlayGhostSfx() throws InterruptedException {
        
        AudioController.initialise();
        initialiseAudioControllers();

        AudioManager.playGhostSfx();

        waiter.await(1, TimeUnit.SECONDS);

        // Tests if effect is playing
        assertTrue(getGhostEffectController().isPlaying());


    }

    @Test
    public void testPlayFastAirSfx() throws InterruptedException {
        
        AudioController.initialise();
        initialiseAudioControllers();

        AudioManager.playFastAirSfx();

        waiter.await(1, TimeUnit.SECONDS);

        // Tests if effect is playing
        assertTrue(getFastAirEffectController().isPlaying());


    }

    @Test
    public void testPlayTeleportSfx() throws InterruptedException {
        
        AudioController.initialise();
        initialiseAudioControllers();

        AudioManager.playTeleportSfx();

        waiter.await(1, TimeUnit.SECONDS);

        // Tests if effect is playing
        assertTrue(getTeleportEffectController().isPlaying());


    }

    @Test
    public void testPlayBombSfx() throws InterruptedException {
        
        AudioController.initialise();
        initialiseAudioControllers();

        AudioManager.playBombSfx();

        waiter.await(1, TimeUnit.SECONDS);

        // Tests if effect is playing
        assertTrue(getBombEffectController().isPlaying());


    }

    @Test
    public void testPlayTrapSfx() throws InterruptedException {
        
        AudioController.initialise();
        initialiseAudioControllers();

        AudioManager.playTrapSfx();

        waiter.await(250, TimeUnit.MILLISECONDS);

        // Tests if effect is playing
        assertTrue(getTrapEffectController().isPlaying());

    }

    @Test
    public void testPlayWinSfx() throws InterruptedException {
        
        AudioController.initialise();
        initialiseAudioControllers();

        AudioManager.playWinSfx();

        waiter.await(1, TimeUnit.SECONDS);

        // Tests if effect is playing
        assertTrue(getWinEffectController().isPlaying());


    }

    @Test
    public void testPlayLoseSfx() throws InterruptedException {
        
        AudioController.initialise();
        initialiseAudioControllers();

        AudioManager.playLoseSfx();

        waiter.await(1, TimeUnit.SECONDS);

        // Tests if effect is playing
        assertTrue(getLoseEffectController().isPlaying());


    }

}
