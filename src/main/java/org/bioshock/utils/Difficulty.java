package org.bioshock.utils;

import org.bioshock.main.App;

public enum Difficulty {
    EASY(App.getBundle().getString("EASY_TEXT")),
    HARD(App.getBundle().getString("HARD_TEXT"));

    private String label;

    Difficulty(String label) {
        this.label = label;
    }

    public String toString() {
        return label;
    }
}
