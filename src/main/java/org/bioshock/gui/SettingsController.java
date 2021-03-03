package org.bioshock.gui;

import java.util.prefs.Preferences;

import org.bioshock.main.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SettingsController {
    private Preferences prefs;

    @FXML
    private void switchToMainView() {
        App.setFXMLRoot("main");
    }

    @FXML
    public void initialize() {
        prefs = Preferences.userRoot().node(this.getClass().getName());
    }

    @FXML
    public void toggleMusicOn(ActionEvent actionEvent) {
        prefs.putBoolean("musicOn", true);
    }

    @FXML
    public void toggleMusicOff(ActionEvent actionEvent) {
        prefs.putBoolean("musicOn", false);
    }

    public void toggleSfx(ActionEvent actionEvent) {
        //TODO
    }
}
