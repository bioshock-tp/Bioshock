package org.bioshock.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import org.bioshock.audio.AudioManager;
import org.bioshock.audio.controllers.AudioController;
import org.bioshock.audio.controllers.EffectController;
import org.bioshock.audio.settings.EffectSettings;
import org.bioshock.main.App;
import org.bioshock.utils.GlobalStrings;

import java.util.prefs.Preferences;

public class SettingsController extends App {
    @FXML
    public Label musicLabel;
    @FXML
    public Label musicVolumeLabel;
    @FXML
    public Label sfxVolumeLabel;
    @FXML
    public Label sfxLabel;
    @FXML
    private Slider musicVolumeSlider;
    @FXML
    private Slider sfxVolumeSlider;
    @FXML
    private Button backButton;
    @FXML
    private RadioButton musicOffRadioButton;
    @FXML
    private RadioButton musicOnRadioButton;
    @FXML
    private RadioButton sfxOffRadioButton;
    @FXML
    private RadioButton sfxOnRadioButton;

    @FXML
    private void switchToMainView() {
        App.setFXMLRoot("main");
    }

    @FXML
    public void initialize() {
        backButton.setText(GlobalStrings.BACK_MAIN_MENU_BUTTON_TEXT);
        musicLabel.setText(GlobalStrings.MUSIC_TEXT);
        musicVolumeLabel.setText(GlobalStrings.VOLUME_TEXT + ":");
        sfxLabel.setText(GlobalStrings.SFX_TEXT);
        sfxVolumeLabel.setText(GlobalStrings.VOLUME_TEXT + ":");
        musicOnRadioButton.setText(GlobalStrings.ON_BUTTON_TEXT);
        musicOnRadioButton.setText(GlobalStrings.OFF_BUTTON_TEXT);
        sfxOnRadioButton.setText(GlobalStrings.ON_BUTTON_TEXT);
        sfxOnRadioButton.setText(GlobalStrings.OFF_BUTTON_TEXT);
        initialiseAudioSettings();
    }

    private void initialiseAudioSettings() {
        if(getPrefs().getBoolean("musicOn", true)) {
            musicOnRadioButton.setSelected(true);
        }
        else {
            musicOffRadioButton.setSelected(true);
        }

        if(getPrefs().getBoolean("sfxOn", true)) {
            sfxOnRadioButton.setSelected(true);
        }
        else {
            sfxOffRadioButton.setSelected(true);
        }

        musicVolumeSlider.setValue(getPrefs().getDouble("musicVolume", 1.0));

        musicVolumeSlider.setOnMouseReleased(event -> {

            getPrefs().putDouble("musicVolume", musicVolumeSlider.getValue());

            if (getPrefs().getBoolean("musicOn", true)) {
                AudioManager.playBackgroundMusic(musicVolumeSlider.getValue());
            }

        });

        sfxVolumeSlider.setValue(getPrefs().getDouble("sfxVolume", 1.0));

        sfxVolumeSlider.setOnMouseReleased(event -> {
            getPrefs().putDouble("sfxVolume", sfxVolumeSlider.getValue());

            if (getPrefs().getBoolean("sfxOn", true)) {
                final EffectSettings settings = new EffectSettings();
                settings.setVolume(sfxVolumeSlider.getValue());
                getSfxController().play(settings);
            }
        });
    }

    @FXML
    public void toggleMusicOn(ActionEvent actionEvent) {
        AudioManager.playBackgroundMusic(getPrefs().getDouble("musicVolume", 1.0));
        getPrefs().putBoolean("musicOn", true);
    }

    @FXML
    public void toggleMusicOff(ActionEvent actionEvent) {
        AudioManager.stopBackgroundMusic();
        getPrefs().putBoolean("musicOn", false);
    }

    @FXML
    public void toggleSfxOn(ActionEvent actionEvent) {
        final EffectSettings settings = new EffectSettings();
        settings.setVolume(getPrefs().getDouble("sfxVolume", 1.0));
        getSfxController().play(settings);
        getPrefs().putBoolean("sfxOn", true);
    }

    @FXML
    public void toggleSfxOff(ActionEvent actionEvent) {
        getSfxController().stop();
        getPrefs().putBoolean("sfxOn", false);
    }

    private Preferences getPrefs() {
        return Preferences.userNodeForPackage(SettingsController.class);
    }

    private EffectController getSfxController() {
        return AudioController.loadEffectController("enabled");
    }
}
