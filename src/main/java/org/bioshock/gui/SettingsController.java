package org.bioshock.gui;

import java.util.prefs.Preferences;

import org.bioshock.main.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;

public class SettingsController {
    private Button backButton;
    private RadioButton musicOffRadioButton;
    private RadioButton musicOnRadioButton;
    private RadioButton sfxOffRadioButton;
    private RadioButton sfxOnRadioButton;
    private Preferences prefs;

//    private MediaPlayer bgMusic;

    @FXML
    private void switchToMainView() {
        App.setFXMLRoot("main");
    }
//    public void startMusic(boolean b) throws URISyntaxException {
//        bgMusic = new MediaPlayer(new Media(getClass().getResource("backgroundMusic.mp3").toURI().toString()));
//        if (b) {
//            bgMusic.play();
//        }
//        else {
//            bgMusic.stop();
//        }
//        //bgMusic.setAutoPlay(b);
//    }

    @FXML
    public void initialize() {
        prefs = Preferences.userRoot().node(this.getClass().getName());
        //startMusic(prefs.getBoolean("musicOn", true));

//        if (prefs.getBoolean("musicOn", true)) {
//            musicOnRadioButton.setSelected(true);
//        }
//        else {
//            musicOffRadioButton.setSelected(true);
//        }

        //prefs = Preferences.userRoot().node(this.getClass().getName());
//        musicOnRadioButton.setOnAction(e -> {
//            try {
//                playMusic(true);
//            } catch (URISyntaxException ex) {
//                ex.printStackTrace();
//            }
//        });
//        musicOffRadioButton.setOnAction(e -> {
//            try {
//                playMusic(false);
//            } catch (URISyntaxException ex) {
//                ex.printStackTrace();
//            }
//        });
    }

    //    private void playMusic(boolean bool) throws URISyntaxException {
//        if (bool) {
//            bgMusic.play();
//        }
//        else {
//            bgMusic.stop();
//        }
//    }
    @FXML
    public void toggleMusicOn(ActionEvent actionEvent) {
        prefs.putBoolean("musicOn", true);
//        if (!musicPlaying()) {
//            System.out.println("Yes");
//            //startMusic(prefs.getBoolean("musicOn", true));
//        }
    }
    @FXML
    public void toggleMusicOff(ActionEvent actionEvent) {
        prefs.putBoolean("musicOn", false);
//        if (musicPlaying()) {
//            startMusic(prefs.getBoolean("musicOn", false));
//        }
    }

    public void toggleSfx(ActionEvent actionEvent) {
        //TODO
    }
}
