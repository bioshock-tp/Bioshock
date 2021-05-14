package org.bioshock.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FontManagerTest {

    private FontManager fontManagerUnderTest;

    @BeforeEach
    public void setUp() throws Exception {
        fontManagerUnderTest = new FontManager();
    }

    @Test
    public void testInitialiseFontManager() {
        // Tests if FontManager is initialised successfully
        assertNotNull(fontManagerUnderTest);
    }
}
