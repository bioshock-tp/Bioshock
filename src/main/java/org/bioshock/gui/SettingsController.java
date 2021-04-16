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

import java.util.Locale;
import java.util.ResourceBundle;
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
    public Label languageLabel;
    @FXML
    public RadioButton enRadioButton;
    @FXML
    public RadioButton roRadioButton;
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

    private ResourceBundle bundle;
    private Locale locale;

    @FXML
    private void switchToMainView() {
        App.setFXMLRoot("main");
    }

    @FXML
    public void initialize() {
        initialiseLanguageSettings();
        initialiseAudioSettings();
    }

    private void initialiseLanguageSettings() {
        switch (getPrefs().get("language", "en")) {
            case "en":
                enRadioButton.setSelected(true);
                loadLang("en");
                break;
            case "ro":
                roRadioButton.setSelected(true);
                loadLang("ro");
                break;
        }

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

    public void setLanguageEn(ActionEvent actionEvent) {
        loadLang("en");
        getPrefs().put("language", "en");
    }

    public void setLanguageRo(ActionEvent actionEvent) {
        loadLang("ro");
        getPrefs().put("language", "ro");
    }

    private void loadLang(String lang) {
        locale = new Locale(lang);
        bundle = ResourceBundle.getBundle("org.bioshock.utils.lang", locale);
        initialiseLabels();
    }

    private void initialiseLabels() {
        backButton.setText(bundle.getString("BACK_MAIN_MENU_BUTTON_TEXT"));
        musicLabel.setText(bundle.getString("MUSIC_TEXT"));
        musicVolumeLabel.setText((bundle.getString("VOLUME_TEXT") + ":"));
        sfxLabel.setText(bundle.getString("SFX_TEXT"));
        sfxVolumeLabel.setText((bundle.getString("VOLUME_TEXT") + ":"));
        musicOnRadioButton.setText(bundle.getString("ON_BUTTON_TEXT"));
        musicOffRadioButton.setText(bundle.getString("OFF_BUTTON_TEXT"));
        sfxOnRadioButton.setText(bundle.getString("ON_BUTTON_TEXT"));
        sfxOffRadioButton.setText(bundle.getString("OFF_BUTTON_TEXT"));
        languageLabel.setText(bundle.getString("LANGUAGE_TEXT"));
        enRadioButton.setText(bundle.getString("ENGLISH_BUTTON_TEXT"));
        roRadioButton.setText(bundle.getString("ROMANIAN_BUTTON_TEXT"));
    }
}
