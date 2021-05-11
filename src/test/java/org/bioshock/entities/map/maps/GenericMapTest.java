package org.bioshock.entities.map.maps;

import static org.bioshock.entities.map.utils.RoomType.NO_ROOM;
import static org.bioshock.entities.map.utils.RoomType.SINGLE_ROOM;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.bioshock.JavaFxTest;
import org.bioshock.entities.map.utils.RoomType;
import org.bioshock.main.App;
import org.bioshock.main.MockJFXApp;
import org.bioshock.scenes.SceneManager;
import org.bioshock.utils.GlobalConstants;
import org.bioshock.utils.Size;
import org.junit.jupiter.api.Test;

import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;


class GenericMapTest extends JavaFxTest{

    
    @Test
    void test() throws InterruptedException{
        init();
        
        Map map = new GenericMap(
            new Point3D(0, 0, 0),
            1,
            new Size(5, 7),
            new Size(3, 5),
            Color.SADDLEBROWN,
            GlobalConstants.TEST_MAP,
            0
        );
        
        SceneManager.setMap(map);
        
        assertTrue(true);
    }

}
