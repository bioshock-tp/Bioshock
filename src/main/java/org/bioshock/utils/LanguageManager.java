package org.bioshock.utils;

import org.bioshock.gui.SettingsController;
import org.bioshock.main.App;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class LanguageManager {
    public static void initialiseLanguageSettings() {
        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        switch (prefs.get("language", "en")) {
            case "en":
                loadLang("en");
                break;
            case "ro":
                loadLang("ro");
                break;
        }
        App.setNAME(App.getBundle().getString("BUZZ_TEXT") + App.getBundle().getString("KILL_TEXT"));
    }

    public static void loadLang(String lang) {
        Locale locale = new Locale(lang);
        App.setLocale(locale);
        ResourceBundle bundle = ResourceBundle.getBundle("org.bioshock.utils.lang", locale);
        App.setBundle(bundle);
    }
}
