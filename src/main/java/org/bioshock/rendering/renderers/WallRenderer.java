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

        for (int i = 0; i < wall.getNumImages(); i++) {
            gc.drawImage(
                wall.getImage(),
                getRenX(x + i * imageWidth * horizontalMultiplier),
                getRenY(y + i * imageHeight* (1 - horizontalMultiplier)),
                getRenWidth(imageWidth),
                getRenHeight(imageHeight)
            );
        }
        if (wall.needsCroppedImage()) {
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