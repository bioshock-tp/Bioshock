package org.bioshock.entities.map.maps;

import org.bioshock.scenes.SceneManager;
import org.bioshock.utils.GlobalConstants;
import org.bioshock.utils.Size;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.main.TestingApp;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import junit.framework.TestCase;


class GenericMapTest extends TestCase{
    @BeforeAll
    public static void init() {
        TestingApp.launchJavaFXThread();
    }
    
    @Test
    void test() throws InterruptedException{
        
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
