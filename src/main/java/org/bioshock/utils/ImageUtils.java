package org.bioshock.utils;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import org.bioshock.main.App;

import java.io.File;


public class ImageUtils {
    public static Image loadImage(String path) {
        File file = new File(path);
        App.logger.debug("Loading Sprite sheet " + file.exists());
        String imagePath = file.getAbsolutePath();
        if (File.separatorChar == '\\') {
            imagePath = imagePath.replace('/', File.separatorChar);
            imagePath = imagePath.replace("\\", "\\\\");
        }
        else {
            imagePath = imagePath.replace('\\', File.separatorChar);
        }
        imagePath = "file:" + imagePath;
        return new Image(imagePath);
    }
    public static Image crop(Image img,int x,int y,int w,int h){
        PixelReader reader = img.getPixelReader();
        return new WritableImage(reader, x, y, w, h);
    }
}
