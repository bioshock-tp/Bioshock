package org.bioshock.gui;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import org.bioshock.utils.GlobalStrings;

public class TitleController {
    @FXML
    public Text buzzLabel;
    @FXML
    public Text killLabel;

    public void initialize() {
        buzzLabel.setText(GlobalStrings.BUZZ_TEXT);
        killLabel.setText(GlobalStrings.KILL_TEXT);
    }
}
