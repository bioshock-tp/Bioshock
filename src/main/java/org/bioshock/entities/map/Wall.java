package org.bioshock.entities.map;

import org.bioshock.components.NetworkC;
import org.bioshock.entities.SquareEntity;
import org.bioshock.rendering.renderers.WallRenderer;
import org.bioshock.rendering.renderers.components.SimpleRendererC;
import org.bioshock.utils.GlobalConstants;
import org.bioshock.utils.Size;

import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * 
 * A class that represents walls that are used to represent the walls 
 * in rooms and also objects in the rooms
 *
 */
public class Wall extends SquareEntity {
    /**
     * The image that the wall renders with
     */
    private Image image;
    /**
     * size of an image in units
     * With the image always being square
     */
    private int imageSize;
    /**
     * The number of whole images needed to texture the wall
     */
    private int numImages;
    /**
     * Whether the wall repeats horizontally or vertically
     */
    private boolean horizontal = true;
    /**
     * Boolean to represent whether a cropped image is needed for rendering
     */
    private boolean needsCroppedImage = false;
    /**
     * The scaler to say how much smaller the cropped image needs to be compared to the original image
     */
    private double wallScaler;

    /**
     * Constructs a new wall
     * @param p The top left of the wall
     * @param com The network component of the wall
     * @param s The Size of the wall
     * @param c The colour of the wall
     * @param image The image that is repeated to texture the wall
     * @param wallWidth The wallWidth 
     */
    public Wall(Point3D p, NetworkC com, Size s, Color c, Image image, double wallWidth) {
        super(p, com, new SimpleRendererC(), s, 0, c);
        renderer = WallRenderer.class;
        this.image = image;

        //Get the width and height of the wall in terms of units
        int width = (int) (s.getWidth() / GlobalConstants.UNIT_WIDTH);
        int height = (int) (s.getHeight() / GlobalConstants.UNIT_HEIGHT);
        
        //If either the width or height is less than the wall width
        //an entire image can't be fit in so set the number of images to 0 and 
        //then set the wallScaler and needsCroppedImage to true
        if(width < wallWidth) {
            imageSize = (int) wallWidth;
            numImages = 0;
            wallScaler = (double)width/(double) height;
            needsCroppedImage = true;
        }
        else if(height < wallWidth) {
            imageSize = (int) wallWidth;
            numImages = 0;
            wallScaler = (double) height/(double) width;
            needsCroppedImage = true;
        }
        //In the following two cases 1 or more images can be repeated so set the repetition direction
        //and then set the number of whole images that can be repeated and then work out if a cropped
        //image is needed and what the scaler is
        else if(height <= width) {
            imageSize = height;
            horizontal = true;
            numImages = width / height;

            if((width % height) != 0) {
                wallScaler = (double)(width % height)/(double) height;
                needsCroppedImage = true;
            }
        }
        else {
            imageSize = width;
            horizontal = false;
            numImages = height / width;

            if((height % width) != 0) {
                wallScaler = (double)(height % width)/(double) width;
                needsCroppedImage = true;
            }
        }


    }

    /**
     * 
     * @return The wall scaler
     */
    public double getWallScaler() {
        return wallScaler;
    }

    @Override
    protected void tick(double timeDelta) {
        /* This entity does not change */
    }

    /**
     * 
     * @return The image the wall is textured with
     */
    public Image getImage() {
        return image;
    }

    /**
     * 
     * @return The image size of the un-cropped image
     */
    public int getImageSize() {
        return imageSize;
    }

    /**
     * 
     * @return The number of un-cropped images
     */
    public int getNumImages() {
        return numImages;
    }

    /**
     * 
     * @return horizontal
     */
    public boolean isHorizontal() {
        return horizontal;
    }

    /**
     * 
     * @return if the image needs to be cropped or not
     */
    public boolean needsCroppedImage() {
        return needsCroppedImage;
    }

}
