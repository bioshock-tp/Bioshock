package org.bioshock.animations;

import org.bioshock.engine.core.GameLoop;
import org.bioshock.rendering.RenderManager;
import org.bioshock.scenes.SceneManager;
import org.bioshock.utils.ImageUtils;
import org.bioshock.utils.Size;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class AnimationPlayer {

    /** Path of the sprite image file. */
    private static final String IMAGE_PATH =
        "src/main/resources/org/bioshock/images/spritesheet.png";

    private static Image img;
    static {
        AnimationPlayer.img = ImageUtils.loadImage(IMAGE_PATH);
    }

    private AnimationPlayer() {}

    /**
     * Plays the selected animation for the current sprite, given its position and size.
     * @param sprite Sprite to perform the animation on.
     * @param position Co-ordinates of where to render the animation.
     * @param renderSize Size of the animation to render.
     */
    public static void playAnimation(
        Sprite sprite,
        Point2D position,
        Size renderSize
    ) {
        double time = GameLoop.getCurrentGameTime();
        Canvas canvas = SceneManager.getCanvas();
        GraphicsContext gc = canvas.getGraphicsContext2D();

        if (sprite.hasValidSpriteImages()) {
            playAnimation(
                sprite.getSpriteImages(),
                sprite.getPlaySpeed(),
                position,
                renderSize
            );
        }

        else {
            playAnimation(
                gc,
                time,
                sprite.getActualSize(),
                sprite.getSpriteLocationOnSheet(),
                sprite.getNumberOfFrames(),
                position,
                sprite.getSize(),
                sprite.isReversePlay(),
                sprite.getPlaySpeed(),
                renderSize
            );
        }
    }

    /**
     * Plays the animation for the given images and animation settings.
     * @param imgs List of images to play in the animation.
     * @param speed Speed of the animation playback.
     * @param position Co-ordinates of where to render the animation.
     * @param renderSize Size of the animation to render.
     */
    private static void playAnimation(
        Image[] imgs,
        double speed,
        Point2D position,
        Size renderSize
    ) {
        double time = GameLoop.getCurrentGameTime();
        Canvas canvas = SceneManager.getCanvas();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        int numberOfFrames = imgs.length;
        int index = findCurrentFrame(time, numberOfFrames, speed);
        gc.drawImage(
            imgs[index],
            position.getX(),
            position.getY(),
            RenderManager.getRenWidth(renderSize.getWidth()),
            RenderManager.getRenHeight(renderSize.getHeight())
        );
    }

    /**
     * Renders the animation to the graphics context.
     * @param gc The graphics context to render to.
     * @param time Current game time.
     * @param actualSize Actual size of the image on the sprite sheet.
     * @param startingPoint Position of where to place the animation.
     * @param numberOfFrames Number of frames in the animation.
     * @param position Co-ordinates of where to render the animation.
     * @param size Size of the frames in the animation.
     * @param reversePlay Plays the animation in reverse.
     * @param playSpeed Speed of the animation playback.
     * @param renderSize Size of the animation to render.
     */
    private static void playAnimation(
        GraphicsContext gc,
        double time,
        int actualSize,
        Point2D startingPoint,
        int numberOfFrames,
        Point2D position,
        Size size,
        boolean reversePlay,
        double playSpeed,
        Size renderSize
    ) {
        double speed = playSpeed >= 0 ? playSpeed : 0.3;

        /*
         * index represents the index of image to be drawn from the set of
         * images representing frames of animation
         */
        int index = findCurrentFrame(time, numberOfFrames, speed);

        /*
         * newX represents the X coordinate of image in the sprite-sheet image
         * to be drawn on screen
         */
        int newSpriteSheetX = (int) (reversePlay ?
            startingPoint.getX() + index * actualSize
            : startingPoint.getX());

        /*
         * newY represents the Y coordinate of image in the sprite-sheet image
         * to be drawn on screen
         */
        int newSpriteSheetY = (int) (reversePlay ?
            startingPoint.getY()
            : startingPoint.getY() + index * actualSize);

        gc.drawImage(
            img,
            newSpriteSheetX,
            newSpriteSheetY,
            size.getWidth(),
            size.getHeight(),
            position.getX(),
            position.getY(),
            RenderManager.getRenWidth(renderSize.getWidth()),
            RenderManager.getRenHeight(renderSize.getHeight())
        );
    }

    private static int findCurrentFrame(
        double time, int totalFrames, double speed
    ) {
        return (int) (time % (totalFrames * speed) / speed);
    }

}
