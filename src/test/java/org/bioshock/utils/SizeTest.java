package org.bioshock.utils;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class SizeTest {
    @Test
    public void testConstruction() {
        int width = 10;
        int height = 20;

        Size size = new Size(width, height);

        Assertions.assertEquals(width, size.getWidth());
        Assertions.assertEquals(height, size.getHeight());
    }
}
