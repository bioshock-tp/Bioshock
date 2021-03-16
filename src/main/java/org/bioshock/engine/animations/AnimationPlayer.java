package org.bioshock.engine.animations;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.bioshock.engine.core.GameLoop;
import org.bioshock.engine.rendering.RenderManager;
import org.bioshock.engine.scene.SceneManager;
import org.bioshock.utils.ImageUtils;

public class AnimationPlayer {
    public static Image img;
    static {
        AnimationPlayer.img = ImageUtils.loadImage("src/main/resources/org/bioshock/images/sprites_without_border.png");
    }

    public static Image getSpriteSheet(){
        return img;
    }

    public static void playAnimation(Sprite sprite, double x, double y) {
        double time = GameLoop.getSDelta();
        Canvas canvas = SceneManager.getCanvas();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        if (sprite.hasValidSpriteImages) {
            playAnimation(sprite.spriteImages, sprite.playSpeed, x, y, sprite.width*sprite.getScale(), sprite.height*sprite.getScale());
        }
        else {
            playAnimation(gc, time, sprite.actualSize, sprite.spriteLocationOnSheetX, sprite.spriteLocationOnSheetY, sprite.numberOfFrames, x, y, sprite.width, sprite.height, sprite.getScale(), sprite.reversePlay, sprite.playSpeed);
        }

    }

    public static void playAnimation(Image[] imgs, double speed, double x, double y, double w, double h) {
        double time = GameLoop.getSDelta();
        Canvas canvas = SceneManager.getCanvas();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        int numberOfFrames = imgs.length;
        int index = findCurrentFrame(time, numberOfFrames, speed);
        gc.drawImage(imgs[index], x, y, RenderManager.getRenWidth(w), RenderManager.getRenHeight(h));
    }

    public static void playAnimation(GraphicsContext gc, double time, int actualSize, int startingPointX, int startingPointY, int numberOfFrames, double x, double y, double width, double height, double scale, boolean reversePlay, double playSpeed) {

        double speed = playSpeed >= 0 ? playSpeed : 0.3;

        // index represents the index of image to be drawn from the set of images representing frames of animation
        int index = findCurrentFrame(time, numberOfFrames, speed);

        // newX represents the X coordinate of image in the sprite-sheet image to be drawn on screen
        int newSpriteSheetX = reversePlay ? startingPointX + index * actualSize : startingPointX;
        // newY represents the X coordinate of image in the sprite-sheet image to be drawn on screen
        int newSpriteSheetY = reversePlay ? startingPointY : startingPointY + index * actualSize;

        gc.drawImage(img, newSpriteSheetX, newSpriteSheetY, width, height, x, y, RenderManager.getRenWidth(width * scale), RenderManager.getRenHeight(height * scale));
    }

    private static int findCurrentFrame(double time, int totalFrames, double speed) {
        return (int) (time % (totalFrames * speed) / speed);
    }
}
