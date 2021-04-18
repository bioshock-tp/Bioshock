package org.bioshock.animations;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import org.bioshock.entities.Entity;
import org.bioshock.utils.ImageUtils;
import org.bioshock.utils.SizeD;

import java.util.List;

public class Sprite {
    private double playSpeed;

    private Point2D spriteLocationOnSheet;
    private SizeD size;

    private double scale;
    private int actualSize;

    private int numberOfFrames;

    private boolean reversePlay;

    private Image[] spriteImages;

    private boolean hasValidSpriteImages;

    private Entity entityReference;

    public Sprite(
        Entity entity,
        Point2D spriteLocationOnSheet,
        SizeD size,
        int actualSize,
        int numberOfFrames,
        double scale,
        boolean leftToRight,
        double playSpeed
    ) {
        super();

        this.actualSize = actualSize;
        this.spriteLocationOnSheet = spriteLocationOnSheet;
        this.numberOfFrames = numberOfFrames;
        this.size = size;
        this.scale = scale;
        this.reversePlay = leftToRight;
        this.entityReference = entity;
        this.playSpeed = playSpeed;
    }

    public Sprite(
        Entity entity,
        SizeD size,
        int actualSize,
        double scale,
        Image spriteSheet,
        List<Rectangle> specifications,
        boolean leftToRight,
        double playSpeed
    ) {
        super();

        this.actualSize = actualSize;
        this.numberOfFrames = specifications.size();
        this.size = size;
        this.scale = scale;
        this.reversePlay = leftToRight;
        this.entityReference = entity;
        this.hasValidSpriteImages = true;
        this.playSpeed = playSpeed;

        spriteImages = new Image[specifications.size()];

        for (int i = 0; i < specifications.size(); i++) {
            Rectangle specification = specifications.get(i);

            int x = (int) specification.getX();
            int y = (int) specification.getY();
            int w = (int) specification.getWidth();
            int h = (int) specification.getHeight();

            spriteImages[i] = ImageUtils.crop(spriteSheet, x, y, w, h);
        }
    }

    public void setSize(SizeD size) {
        this.size = size;
    }

    public void setActualSize(int actualSize) {
        this.actualSize = actualSize;
    }

    public void setEntityReference(Entity entityReference) {
        this.entityReference = entityReference;
    }

    public void setSpriteLocationOnSheet(Point2D spriteLocationOnSheet) {
        this.spriteLocationOnSheet = spriteLocationOnSheet;
    }

    public void setPlaySpeed(double playSpeed) {
        this.playSpeed = playSpeed;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public void setNumberOfFrames(int numberOfFrames) {
        this.numberOfFrames = numberOfFrames;
    }

    public void setReversePlay(boolean reversePlay) {
        this.reversePlay = reversePlay;
    }

    public void setSpriteImages(Image[] spriteImages) {
        this.spriteImages = spriteImages;
    }

    public void setHasValidSpriteImages(boolean hasValidSpriteImages) {
        this.hasValidSpriteImages = hasValidSpriteImages;
    }

    public double getXPosition() {
        return entityReference.getX();
    }

    public double getYPosition() {
        return entityReference.getY();
    }

    public Entity getEntityReference() {
        return entityReference;
    }

    public double getScale() {
        return scale;
    }

    public double getPlaySpeed() {
        return playSpeed;
    }

    public Point2D getSpriteLocationOnSheet() {
        return spriteLocationOnSheet;
    }

    public SizeD getSize() {
        return size;
    }

    public int getActualSize() {
        return actualSize;
    }

    public int getNumberOfFrames() {
        return numberOfFrames;
    }

    public boolean isReversePlay() {
        return reversePlay;
    }

    public Image[] getSpriteImages() {
        return spriteImages;
    }

    public boolean hasValidSpriteImages() {
        return hasValidSpriteImages;
    }
}
