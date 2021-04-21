package org.bioshock.animations;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.bioshock.engine.core.GameLoop;
import org.bioshock.entities.Entity;
import org.bioshock.rendering.RenderManager;
import org.bioshock.scenes.SceneManager;
import org.bioshock.utils.ImageUtils;
import org.bioshock.utils.Size;

public class AnimationPlayer {
    private static final String IMAGE_PATH =
        "src/main/resources/org/bioshock/images/sprites_without_border.png";

    private static Image img;
    static {
        AnimationPlayer.img = ImageUtils.loadImage(IMAGE_PATH);
    }

    // private static long time = 0;

    private AnimationPlayer() {}

    // public static void tick(long timeDelta) {
    //     time += timeDelta;
    // }

    public static void playAnimation(Sprite sprite, Point2D position, Size renderSize) {
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

    public static Image getSpriteSheet() {
        return img;
    }
}
