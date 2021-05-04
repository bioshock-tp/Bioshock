package org.bioshock.utils;

import javafx.scene.text.Font;

public class FontManager {

    private Font mainFont;
    private Font titleFont;

    /**
     * Initialises the object by loading in the main fonts.
     */
    public FontManager() {
        titleFont = Font.loadFont(getClass().getResourceAsStream("Minercraftory.ttf"), 16);
        mainFont = Font.loadFont(getClass().getResourceAsStream("Ubuntu-B.ttf"), 16);
    }


    public Font getMainFont() {
        return mainFont;
    }

    public Font getTitleFont() {
        return titleFont;
    }
}


