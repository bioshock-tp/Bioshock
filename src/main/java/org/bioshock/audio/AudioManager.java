package org.bioshock.audio;

import org.bioshock.audio.controllers.AudioController;
import org.bioshock.audio.controllers.EffectController;
import org.bioshock.audio.controllers.MusicController;
import org.bioshock.audio.settings.EffectSettings;
import org.bioshock.audio.settings.MusicSettings;
import org.bioshock.gui.SettingsController;

import java.util.prefs.Preferences;

public class AudioManager {

    private static MusicController bgMusicController;
    private static MusicSettings bgMusicSettings;
    private static EffectController walkingEffectController;
    private static EffectSettings walkingEffectSettings;

    public static void initialiseAudioControllers() {
        bgMusicController = AudioController.loadMusicController(
            "background-music"
        );
        bgMusicSettings = new MusicSettings();
        walkingEffectController = AudioController.loadEffectController(
            "walking"
        );
        walkingEffectSettings = new EffectSettings();
    }

    /**
     * Initialises background with user saved volume preference.
     */
    public static void initialiseBackgroundAudio() {
        AudioController.initialise();
        initialiseAudioControllers();
        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        double volume = prefs.getDouble("musicVolume", 1.0);
        if (prefs.getBoolean("musicOn", true)) {
            playBackgroundMusic(volume);
        }
    }

    /**
     * Stops background music playing.
     */
    public static void stopBackgroundMusic() {
        bgMusicController.stop();
    }

    /**
     * PLays background music.
     */
    public static void playBackgroundMusic(double vol) {
        bgMusicSettings.setVolume(vol);
        bgMusicSettings.setCycleCount(-1);
        bgMusicController.play(bgMusicSettings);
    }

//    /**
//     * Initialises background with user saved volume preference.
//     */
//    public static void initialiseSfxAudio() {
//        AudioController.initialise();
//        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
//        double volume = prefs.getDouble("musicVolume", 1.0);
//        if(prefs.getBoolean("musicOn", true)) {
//            playBackgroundMusic(volume);
//        }
//    }

    /**
     * PLays walking sound.
     */
    public static void playWalkingSfx() {
        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);

        if (prefs.getBoolean("sfxOn", true)) {
            double volume = prefs.getDouble("sfxVolume", 1.0);
            walkingEffectSettings.setVolume(volume);
            walkingEffectSettings.setCycleCount(-1);
            walkingEffectController.play(walkingEffectSettings);
        }
    }

    /**
     * Stops walking sound.
     */
    public static void stopWalkingSfx() {
        walkingEffectController.stop();
    }
}
