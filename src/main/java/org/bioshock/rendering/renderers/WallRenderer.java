package org.bioshock.rendering.renderers;

import static org.bioshock.rendering.RenderManager.getRenHeight;
import static org.bioshock.rendering.RenderManager.getRenWidth;
import static org.bioshock.rendering.RenderManager.getRenX;
import static org.bioshock.rendering.RenderManager.getRenY;

import org.bioshock.entities.map.Wall;
import org.bioshock.utils.GlobalConstants;

import javafx.scene.canvas.GraphicsContext;

public class WallRenderer implements Renderer {
    private WallRenderer() {}

    /**
     * Method to render the given wall
     * @param <E>
     * @param gc The graphics context to render the wall on
     * @param wall The wall to render
     */
    public static <E extends Wall> void render(
        GraphicsContext gc,
        E wall
    ) {
        double x = wall.getX();
        double y = wall.getY();
        double imageSize = wall.getImageSize();
        double imageWidth = imageSize * GlobalConstants.UNIT_WIDTH;
        double imageHeight = imageSize * GlobalConstants.UNIT_HEIGHT;

        gc.save();
        int horizontalMultiplier = wall.isHorizontal() ? 1 : 0;
        
        //Draw the complete images
        for (int i = 0; i < wall.getNumImages(); i++) {
            gc.drawImage(
                wall.getImage(),
                getRenX(x + i * imageWidth * horizontalMultiplier),
                getRenY(y + i * imageHeight* (1 - horizontalMultiplier)),
                getRenWidth(imageWidth),
                getRenHeight(imageHeight)
            );
        }
        //If it needs a cropped image draw the cropped image
        if (wall.needsCroppedImage()) {
            //This draws the image stretched as it looks better to have the texture 
            //stretched but line up properly with the adjacent texture rather than 
            //cropping the image and not having it line up properly
            if (wall.isHorizontal()) {                
                gc.drawImage(
                    wall.getImage(),
                    getRenX(
                        x + wall.getNumImages()
                        * imageWidth * horizontalMultiplier
                    ),
                    getRenY(
                        y + wall.getNumImages()
                        * imageHeight * (1 - horizontalMultiplier)
                    ),
                    getRenWidth(imageWidth * wall.getWallScaler()),
                    getRenHeight(imageHeight)
                );
            }
            else {
                gc.drawImage(
                    wall.getImage(),
                    getRenX(
                        x + wall.getNumImages()
                        * imageWidth * horizontalMultiplier
                    ),
                    getRenY(
                        y + wall.getNumImages()
                        * imageHeight * (1 - horizontalMultiplier)
                    ),
                    getRenWidth(imageWidth),
                    getRenHeight(imageHeight * wall.getWallScaler())
                );
            }
        }

        gc.restore();
    }
}
