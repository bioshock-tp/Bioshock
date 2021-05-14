package org.bioshock.entities;

import org.bioshock.components.NetworkC;
import org.bioshock.main.TestingApp;
import org.bioshock.utils.Size;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import javafx.geometry.Point3D;

public class ImageEntityTest {
    @Before
    public void setUp() {
        TestingApp.launchJavaFXThread();
    }


    @Test
    public void validImageTest() {
        String PATH = getClass().getResource(
            "/org/bioshock/images/items/bomb.png"
        ).getPath();

        ImageEntity imageEntity = new ImageEntity(
            new Point3D(0, 0, 0),
            new Size(10, 10),
            new NetworkC(false),
            PATH
        ) {
            @Override
            protected void tick(double timeDelta) {}
        };


        /* Assert not in state of invalid image entity */
        Assertions.assertAll(
            () -> Assertions.assertTrue(
                imageEntity.isEnabled(),
                "Entity was disabled"
            ),
            () -> Assertions.assertNotNull(
                imageEntity.getRendererC(),
                "Render component was null"
            )
        );
    }


    @Test
    public void invalidImageTest() {
        String PATH = "hello world";

        ImageEntity imageEntity = new ImageEntity(
            new Point3D(0, 0, 0),
            new Size(10, 10),
            new NetworkC(false),
            PATH
        ) {
            @Override
            protected void tick(double timeDelta) {}
        };


        /* Assert state of invalid image entity */
        Assertions.assertAll(
            () -> Assertions.assertFalse(
                imageEntity.isEnabled(),
                "Entity was enabled"
            ),
            () -> Assertions.assertNull(
                imageEntity.getRendererC(),
                "Render component was not null"
            )
        );
    }
}
