package org.bioshock.engine.renderers;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import org.bioshock.engine.animations.Sprite;
import org.bioshock.engine.core.GameLoop;
import org.bioshock.engine.entity.EntityManager;
import org.bioshock.engine.entity.SquareEntity;
import org.bioshock.engine.rendering.RenderManager;
import org.bioshock.engine.scene.SceneManager;
import org.bioshock.main.App;
import org.bioshock.utils.ImageUtils;

import java.util.Objects;

import static org.bioshock.engine.rendering.RenderManager.*;

public class PlayerSpriteRendererHelper implements Renderer{
    public PlayerSpriteRendererHelper() {}
    
    public static <E extends SquareEntity> void render(
            GraphicsContext gc,
            E player
    ) {

        double x = player.getX();
        double y = player.getY();
        double radius = player.getRadius();
        double width = player.getWidth();
        double height = player.getHeight();

        if (player == EntityManager.getCurrentPlayer()) {
            gc.save();
            gc.beginPath();
            gc.arc(getRenX(x + width / 2),
                    getRenY(y + height / 2),
                    getRenWidth(radius),
                    getRenHeight(radius),
                    0, 360);
            gc.rect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
            gc.closePath();
            gc.clip();
            gc.setFill(new Color(0, 0, 0, 0.75));
            gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
            gc.restore();
        }


        gc.save();
        RenderManager.clipToFOV(gc);

        Rotate r = player.getRotate();
        gc.setTransform(
                r.getMxx(), r.getMyx(), r.getMxy(),
                r.getMyy(), r.getTx(), r.getTy()
        );
        gc.setFill(player.getRendererC().getColor());
        //gc.fillRect(getRenX(x), getRenY(y), getRenWidth(width), getRenHeight(height));
        App.logger.debug("Ren X: {}, Ren Y: {} RenWidth: {}, RenHeight: {}", getRenX(x), getRenY(y), getRenWidth(width), getRenHeight(height));
        gc.setLineWidth(10);
        gc.setStroke(player.getRendererC().getColor());
        gc.strokeOval(
          getRenX(x - radius + width / 2),
          getRenY(y - radius + height / 2),
          getRenWidth(radius * 2),
          getRenHeight(radius * 2)
        );

        playAnimation(Objects.requireNonNull(EntityManager.getCurrentPlayer()).getCurrentSprite(), getRenX(x), getRenY(y));

        gc.restore();
    }

    static Image img;
    static {
        img = ImageUtils.loadImage("src/main/resources/org/bioshock/images/sprites_without_border.png");
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
            playAnimation(gc, time, sprite.actualSize, sprite.spriteLocationOnSheetX, sprite.spriteLocationOnSheetY, sprite.numberOfFrames, x, y, sprite.width, sprite.height, sprite.getScale(), sprite.resersePlay, sprite.playSpeed);
        }

    }

    public static void playAnimation(Image[] imgs, double speed, double x, double y, double w, double h) {
        double time = GameLoop.getSDelta();
        Canvas canvas = SceneManager.getCanvas();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        int numberOfFrames = imgs.length;
        int index = findCurrentFrame(time, numberOfFrames, speed);
        gc.drawImage(imgs[index], x, y, w, h);
    }

    public static void playAnimation(GraphicsContext gc, double time, int actualSize, int startingPointX, int startingPointY, int numberOfFrames, double x, double y, double width, double height, double scale, boolean reversePlay, double playSpeed) {

        double speed = playSpeed >= 0 ? playSpeed : 0.3;

        // index represents the index of image to be drawn from the set of images representing frames of animation
        int index = findCurrentFrame(time, numberOfFrames, speed);

        // newX represents the X coordinate of image in the sprite-sheet image to be drawn on screen
        int newSpriteSheetX = reversePlay ? startingPointX + index * actualSize : startingPointX;
        // newY represents the X coordinate of image in the sprite-sheet image to be drawn on screen
        int newSpriteSheetY = reversePlay ? startingPointY : startingPointY + index * actualSize;

        gc.drawImage(img, newSpriteSheetX, newSpriteSheetY, width, height, x, y, width * scale, height * scale);
    }

    private static int findCurrentFrame(double time, int totalFrames, double speed) {
        return (int) (time % (totalFrames * speed) / speed);
    }
}
