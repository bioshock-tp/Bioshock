package org.bioshock.audio;

import org.bioshock.audio.controllers.AudioController;
import org.bioshock.audio.controllers.MusicController;
import org.bioshock.audio.settings.MusicSettings;
import org.bioshock.gui.SettingsController;
import org.bioshock.main.App;

import java.util.prefs.Preferences;

public class AudioManager {
    public static void initialiseAudio(App app) {
        AudioController.initialise();
        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        double volume = prefs.getDouble("musicVolume", 1.0);
        if(prefs.getBoolean("musicOn", true)) {
            playBackgroundMusic(volume);
        }
    }

    public static void stopBackgroundMusic() {
		MusicController musicController = AudioController.loadMusicController(
            "background-music"
        );
		musicController.stop();
	}

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
