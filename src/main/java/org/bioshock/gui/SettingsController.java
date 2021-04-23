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
import org.bioshock.utils.LanguageManager;

import java.util.prefs.Preferences;

public class SettingsController extends App {
    /**
     * String for corresponding language
     */
    private static final String ENGLISH = "en";

    /**
     * String for corresponding language
     */
    private static final String ROMANIAN = "ro";

    /**
     * Used for preferences
     */
    private static final String LANGUAGE = "language";

    /**
     * Used for preferences
     */
    private static final String MUSIC_ON = "musicOn";

    /**
     * Used for preferences
     */
    private static final String MUSIC_VOLUME = "musicVolume";

    /**
     * Used for preferences
     */
    private static final String SFX_ON = "sfxOn";

    /**
     * Used for preferences
     */
    private static final String SFX_VOLUME = "sfxVolume";


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



    @FXML
    private void switchToMainView() {
        App.setFXMLRoot("main");
    }

    @FXML
    public void initialize() {
        initialiseLanguageSettings();
        initialiseAudioSettings();
        initialiseLabels();
    }

    private void initialiseLanguageSettings() {
        switch (getPrefs().get(LANGUAGE, ENGLISH)) {
            case ENGLISH:
                enRadioButton.setSelected(true);
                break;
            case ROMANIAN:
                roRadioButton.setSelected(true);
                break;
            default:
                enRadioButton.setSelected(true);
        }
    }

    private void initialiseAudioSettings() {
        if (getPrefs().getBoolean(MUSIC_ON, true)) {
            musicOnRadioButton.setSelected(true);
        }
        else {
            musicOffRadioButton.setSelected(true);
        }

        if (getPrefs().getBoolean(SFX_ON, true)) {
            sfxOnRadioButton.setSelected(true);
        }
        else {
            sfxOffRadioButton.setSelected(true);
        }

        musicVolumeSlider.setValue(getPrefs().getDouble(MUSIC_VOLUME, 1.0));

        musicVolumeSlider.setOnMouseReleased(event -> {
            getPrefs().putDouble(MUSIC_VOLUME, musicVolumeSlider.getValue());

            if (getPrefs().getBoolean(MUSIC_ON, true)) {
                AudioManager.playBackgroundMusic(musicVolumeSlider.getValue());
            }

        });

        sfxVolumeSlider.setValue(getPrefs().getDouble(SFX_VOLUME, 1.0));

        sfxVolumeSlider.setOnMouseReleased(event -> {
            getPrefs().putDouble(SFX_VOLUME, sfxVolumeSlider.getValue());

            if (getPrefs().getBoolean(SFX_ON, true)) {
                final EffectSettings settings = new EffectSettings();
                settings.setVolume(sfxVolumeSlider.getValue());
                getSfxController().play(settings);
            }
        });
    }

    @FXML
    public void toggleMusicOn(ActionEvent actionEvent) {
        AudioManager.playBackgroundMusic(getPrefs().getDouble(MUSIC_VOLUME, 1.0));
        getPrefs().putBoolean(MUSIC_ON, true);
    }

    @FXML
    public void toggleMusicOff(ActionEvent actionEvent) {
        AudioManager.stopBackgroundMusic();
        getPrefs().putBoolean(MUSIC_ON, false);
    }

    @FXML
    public void toggleSfxOn(ActionEvent actionEvent) {
        final EffectSettings settings = new EffectSettings();
        settings.setVolume(getPrefs().getDouble(SFX_VOLUME, 1.0));
        getSfxController().play(settings);
        getPrefs().putBoolean(SFX_ON, true);
    }

    @FXML
    public void toggleSfxOff(ActionEvent actionEvent) {
        getSfxController().stop();
        getPrefs().putBoolean(SFX_ON, false);
    }

    private Preferences getPrefs() {
        return Preferences.userNodeForPackage(SettingsController.class);
    }

    private EffectController getSfxController() {
        return AudioController.loadEffectController("enabled");
    }

    public void setLanguageEn(ActionEvent actionEvent) {
        LanguageManager.loadLang(ENGLISH);
        initialiseLabels();
        getPrefs().put(LANGUAGE, ENGLISH);
    }

    public void setLanguageRo(ActionEvent actionEvent) {
        LanguageManager.loadLang(ROMANIAN);
        initialiseLabels();
        getPrefs().put(LANGUAGE, ROMANIAN);
    }

    private void initialiseLabels() {
        backButton.setText(getBundle().getString("BACK_MAIN_MENU_BUTTON_TEXT"));
        musicLabel.setText(getBundle().getString("MUSIC_TEXT"));
        musicVolumeLabel.setText((getBundle().getString("VOLUME_TEXT") + ":"));
        sfxLabel.setText(getBundle().getString("SFX_TEXT"));
        sfxVolumeLabel.setText((getBundle().getString("VOLUME_TEXT") + ":"));
        musicOnRadioButton.setText(getBundle().getString("ON_BUTTON_TEXT"));
        musicOffRadioButton.setText(getBundle().getString("OFF_BUTTON_TEXT"));
        sfxOnRadioButton.setText(getBundle().getString("ON_BUTTON_TEXT"));
        sfxOffRadioButton.setText(getBundle().getString("OFF_BUTTON_TEXT"));
        languageLabel.setText(getBundle().getString("LANGUAGE_TEXT"));
        enRadioButton.setText(getBundle().getString("ENGLISH_BUTTON_TEXT"));
        roRadioButton.setText(getBundle().getString("ROMANIAN_BUTTON_TEXT"));
    }
}
