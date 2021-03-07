package org.bioshock.gui;

import org.bioshock.audio.AudioController;
import org.bioshock.audio.EffectController;
import org.bioshock.main.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;

public class SettingsController extends App {
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
    public void toggleMusicOn(ActionEvent actionEvent) {
        playBackgroundMusic();
    }

    @FXML
    public void toggleMusicOff(ActionEvent actionEvent) {
        stopBackgroundMusic();
    }

    @FXML
    public void toggleSfxOn(ActionEvent actionEvent) {
        EffectController effectController =
            AudioController.loadEffectController("enabled");
        effectController.play(null);
    }

    @FXML
    public void toggleSfxOff(ActionEvent actionEvent) {
        EffectController effectController =
            AudioController.loadEffectController("enabled");
        effectController.stop();
    }
}
