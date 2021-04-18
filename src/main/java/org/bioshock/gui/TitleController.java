package org.bioshock.gui;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import org.bioshock.main.App;

public class TitleController {
    @FXML
    public Text buzzLabel;
    @FXML
    public Text killLabel;

    public void initialize() {
        buzzLabel.setText(App.getBundle().getString("BUZZ_TEXT"));
        killLabel.setText(App.getBundle().getString("KILL_TEXT"));
    }
}
