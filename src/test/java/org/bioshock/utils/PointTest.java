package org.bioshock.utils;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class PointTest {
    @Test
    public void testMutibility() {
        Point point = new Point(0, 0);

        point.setX(10);
        point.setY(10);

        Assertions.assertEquals(10, point.getX(), "Could not change x value");
        Assertions.assertEquals(10, point.getY(), "Could not change y value");
    }
}
