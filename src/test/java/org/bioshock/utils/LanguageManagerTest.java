package org.bioshock.utils;

import org.bioshock.gui.SettingsController;
import org.bioshock.main.App;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.prefs.Preferences;

public class LanguageManagerTest {

    @Test
    public void testInitialiseLanguageSettingsEnglish() {
        // Sets language in preferences as English
        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        prefs.put("language", "en");

        LanguageManager.initialiseLanguageSettings();

        assertEquals(App.getName(), "BuzzKill");
    }

    @Test
    public void testInitialiseLanguageSettingsRomanian() {
        // Sets language in preferences as Romanian
        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        prefs.put("language", "ro");

        LanguageManager.initialiseLanguageSettings();

        assertEquals(App.getName(), "NimicireaMustelor");
    }

}
