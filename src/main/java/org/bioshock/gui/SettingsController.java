package org.bioshock.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import org.bioshock.audio.AudioController;
import org.bioshock.audio.EffectController;
import org.bioshock.main.App;

import java.util.prefs.Preferences;

public class SettingsController extends App {
    public Slider musicVolumeSlider;
    public Slider sfxVolumeSlider;
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

        musicVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {

            getPrefs().putDouble("musicVolume", newValue.doubleValue());

            if (getPrefs().getBoolean("musicOn", true)) {
                playBackgroundMusic(newValue.doubleValue());
            }

            //textField.setText(Double.toString(newValue.intValue()));


        });
    }

    @FXML
    public void toggleMusicOn(ActionEvent actionEvent) {
        playBackgroundMusic(getPrefs().getDouble("musicVolume", 1.0));
        getPrefs().putBoolean("musicOn", true);
    }

    @FXML
    public void toggleMusicOff(ActionEvent actionEvent) {
        stopBackgroundMusic();
        getPrefs().putBoolean("musicOn", false);
    }

    @FXML
    public void toggleSfxOn(ActionEvent actionEvent) {
        EffectController effectController =
            AudioController.loadEffectController("enabled");
        effectController.play(null);
        getPrefs().putBoolean("sfxOn", true);
    }

    @FXML
    public void toggleSfxOff(ActionEvent actionEvent) {
        EffectController effectController =
            AudioController.loadEffectController("enabled");
        effectController.stop();
        getPrefs().putBoolean("sfxOn", false);
    }

    private Preferences getPrefs() {
        return Preferences.userNodeForPackage(SettingsController.class);
    }
}
