package org.bioshock.utils;

import javafx.scene.text.Font;

public class FontManager {
    private FontManager() {}

    public static void loadFonts() {
        Font.loadFont(FontManager.class.getResourceAsStream("Ubuntu-B.ttf"), 16);
    }
}


