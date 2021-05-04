package org.bioshock.utils;

import javafx.scene.text.Font;

public class FontManager {

    /**
     * Initialises the object by loading in the main fonts.
     */
    public FontManager() {
        Font.loadFont(getClass().getResourceAsStream("Minercraftory.ttf"), 16);
        Font.loadFont(getClass().getResourceAsStream("Ubuntu-B.ttf"), 16);
        Font.loadFont(getClass().getResourceAsStream("Ubuntu-R.ttf"), 16);
    }

}


