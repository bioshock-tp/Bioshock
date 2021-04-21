package org.bioshock.rendering.renderers;

import static org.bioshock.rendering.RenderManager.getRenHeight;
import static org.bioshock.rendering.RenderManager.getRenWidth;
import static org.bioshock.rendering.RenderManager.getRenX;
import static org.bioshock.rendering.RenderManager.getRenY;

import org.bioshock.entities.map.Wall;
import org.bioshock.utils.GlobalConstants;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

public class WallRenderer implements Renderer{
    public static <E extends Wall> void render(
            GraphicsContext gc,
            E wall
        ) {
        double x = wall.getX();
        double y = wall.getY();
        double imageSize = wall.getImageSize();
        double imageWidth = imageSize*GlobalConstants.UNIT_WIDTH;
        double imageHeight = imageSize*GlobalConstants.UNIT_HEIGHT;

        gc.save();
        int horiModifier;
        if(wall.isHorizontal()) {
            horiModifier = 1;
        }
        else {
            horiModifier = 0;
        }
        
        for(int i=0; i < wall.getNumImages(); i++) {
            gc.drawImage(
                wall.getImage(), 
                getRenX(x+i*imageWidth*horiModifier), 
                getRenY(y+i*imageWidth*(1-horiModifier)), 
                getRenWidth(imageWidth), 
                getRenHeight(imageHeight));
        }
        if(wall.needsCroppedImage()) {
            Image croppedImage = wall.getCroppedImage();
            if(wall.isHorizontal()) {
                gc.drawImage(
                    wall.getImage(), 
                    getRenX(x+wall.getNumImages()*imageWidth*horiModifier), 
                    getRenY(y+wall.getNumImages()*imageWidth*(1-horiModifier)),
                    getRenWidth(imageWidth*wall.getWallScaler()),
                    getRenHeight(imageHeight));
            }
            else {
                gc.drawImage(
                    wall.getImage(), 
                    getRenX(x+wall.getNumImages()*imageWidth*horiModifier), 
                    getRenY(y+wall.getNumImages()*imageWidth*(1-horiModifier)),
                    getRenWidth(imageWidth),
                    getRenHeight(imageHeight*wall.getWallScaler()));
            }
            
//            gc.fillRect(
//                    getRenX(x+wall.getNumImages()*imageWidth*horiModifier), 
//                    getRenY(y+wall.getNumImages()*imageWidth*(1-horiModifier)),
//                    getRenWidth(croppedImage.getWidth()*imageSize),
//                    getRenHeight(croppedImage.getHeight()*imageSize));
        }
        
        

        gc.restore();
    }
}
