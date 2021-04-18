package org.bioshock.audio;

import org.bioshock.audio.controllers.AudioController;
import org.bioshock.audio.controllers.MusicController;
import org.bioshock.audio.settings.MusicSettings;
import org.bioshock.gui.SettingsController;

import java.util.prefs.Preferences;

public class AudioManager {

    /**
     * Initialises background with user saved volume preference.
     */
    public static void initialiseAudio() {
        AudioController.initialise();
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
		MusicController musicController = AudioController.loadMusicController(
            "background-music"
        );
		musicController.stop();
	}

    /**
     * PLays background music.
     */
    public static void playBackgroundMusic(double vol) {
		MusicController musicController = AudioController.loadMusicController(
            "background-music"
        );
		final MusicSettings settings = new MusicSettings();
		settings.setVolume(vol);
		settings.setCycleCount(-1);
		musicController.play(settings);
	}
}
