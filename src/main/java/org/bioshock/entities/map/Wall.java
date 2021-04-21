package org.bioshock.entities.map;

import org.bioshock.components.NetworkC;
import org.bioshock.entities.SquareEntity;
import org.bioshock.main.App;
import org.bioshock.rendering.renderers.WallRenderer;
import org.bioshock.rendering.renderers.components.SimpleRendererC;
import org.bioshock.utils.GlobalConstants;
import org.bioshock.utils.Size;

import javafx.geometry.Point3D;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Wall extends SquareEntity {
    private Image image;
    /**
     * size of an image in units
     */
    private int imageSize;
    private int numImages;
    private boolean horizontal = true;
    
    private Image croppedImage;
    private boolean needsCroppedImage = false;
    private double wallScaler;
    
    
    public Wall(Point3D p, NetworkC com, Size s, Color c, Image image, double wallWidth) {
        super(p, com, new SimpleRendererC(), s, 0, c);
        renderer = WallRenderer.class;
        this.image = image;
        
        int width = (int) (s.getWidth()/GlobalConstants.UNIT_WIDTH);
        int height = (int) (s.getHeight()/GlobalConstants.UNIT_HEIGHT);
        
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
        else if(height <= width) {
            imageSize = height;
            horizontal = true;
            numImages = width/height;
            
            if((width % height) != 0) {
                wallScaler = (double)(width % height)/(double) height;
                needsCroppedImage = true;
            }
        }
        else {
            imageSize = width;
            horizontal = false;
            numImages = height/width;
            
            if((height % width) != 0) {
                wallScaler = (double)(height % width)/(double) width;
                needsCroppedImage = true;
            }
        }
        
       
    }

    public double getWallScaler() {
        return wallScaler;
    }

    @Override
    protected void tick(double timeDelta) {
        /* This entity does not change */
    }

    public Image getImage() {
        return image;
    }

    public int getImageSize() {
        return imageSize;
    }

    public int getNumImages() {
        return numImages;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public Image getCroppedImage() {
        return croppedImage;
    }

    public boolean needsCroppedImage() {
        return needsCroppedImage;
    }

}
