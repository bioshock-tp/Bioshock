package org.bioshock.utils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class FontManagerTest {

    private FontManager fontManagerUnderTest;

    @Before
    public void setUp() throws Exception {
        fontManagerUnderTest = new FontManager();
    }

    @Test
    public void testInitialiseFontManager() {
        // Tests if FontManager is initialised successfully
        assertNotNull(fontManagerUnderTest);
    }
}
