package org.bioshock.audio;

import javafx.application.Platform;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javafx.scene.media.MediaPlayer;
import org.bioshock.engine.pathfinding.Graph;
import org.bioshock.engine.pathfinding.GraphNode;
import org.bioshock.entities.map.TraversableEdgeGenerator;
import org.bioshock.utils.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.bioshock.audio.controllers.AudioController;
import org.bioshock.audio.controllers.MusicController;
import org.bioshock.audio.settings.MusicSettings;

import static org.bioshock.audio.AudioManager.initialiseAudioControllers;
import static org.junit.jupiter.api.Assertions.*;

public class AudioManagerTest {

    private final CountDownLatch waiter = new CountDownLatch(1);

    private void initialiseJavafxPlatform() {
        Platform.startup(() ->
        {
            // This block will be executed on JavaFX Thread
        });
    }

    @Test
    public void testInitialiseAudioControllers() {
        // Setup
        initialiseJavafxPlatform();
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
        initialiseJavafxPlatform();

        // Run the test
//        AudioManager.initialiseBackgroundAudio();

        AudioController.initialise();
//        AudioManager.initialiseAudioControllers();

        MusicController bgMusicController = AudioController.loadMusicController(
            "background-music"
        );

        bgMusicController.play(new MusicSettings());

        waiter.await(1, TimeUnit.SECONDS); // 1ms

        assertEquals(MediaPlayer.Status.PLAYING, bgMusicController.getStatus());

        // Verify the results
    }

    @Test
    public void testStopBackgroundMusic() {
        // Setup

        // Run the test
        AudioManager.stopBackgroundMusic();

        // Verify the results
    }

    @Test
    public void testPlayBackgroundMusic() {
        // Setup

        // Run the test
        AudioManager.playBackgroundMusic(0.0);

        // Verify the results
    }

    @Test
    public void testPlayWalkingSfx() {
        // Setup

        // Run the test
        AudioManager.playWalkingSfx();

        // Verify the results
    }

    @Test
    public void testStopWalkingSfx() {
        // Setup

        // Run the test
        AudioManager.stopWalkingSfx();

        // Verify the results
    }

    @Test
    public void testPlayWooshSfx() {
        // Setup

        // Run the test
        AudioManager.playWooshSfx();

        // Verify the results
    }

    @Test
    public void testPlayPlinkSfx() {
        // Setup

        // Run the test
        AudioManager.playPlinkSfx();

        // Verify the results
    }

    @Test
    public void testPlayFreezeSfx() {
        // Setup

        // Run the test
        AudioManager.playFreezeSfx();

        // Verify the results
    }

    @Test
    public void testPlayGhostSfx() {
        // Setup

        // Run the test
        AudioManager.playGhostSfx();

        // Verify the results
    }

    @Test
    public void testPlayFastAirSfx() {
        // Setup

        // Run the test
        AudioManager.playFastAirSfx();

        // Verify the results
    }

    @Test
    public void testPlayTeleportSfx() {
        // Setup

        // Run the test
        AudioManager.playTeleportSfx();

        // Verify the results
    }

    @Test
    public void testPlayBombSfx() {
        // Setup

        // Run the test
        AudioManager.playBombSfx();

        // Verify the results
    }

    @Test
    public void testPlayTrapSfx() {
        // Setup

        // Run the test
        AudioManager.playTrapSfx();

        // Verify the results
    }

    @Test
    public void testPlayWinSfx() {
        // Setup

        // Run the test
        AudioManager.playWinSfx();

        // Verify the results
    }

    @Test
    public void testPlayLoseSfx() {
        // Setup

        // Run the test
        AudioManager.playLoseSfx();

        // Verify the results
    }

    @Test
    public void testStopWooshSfx() {
        // Setup

        // Run the test
        AudioManager.stopWooshSfx();

        // Verify the results
    }
}
