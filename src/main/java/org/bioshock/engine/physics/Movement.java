package org.bioshock.engine.physics;

import java.util.Stack;

import org.bioshock.engine.sprites.Sprite;
import org.bioshock.main.App;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;

public class Movement {
    public static final double SPEED = 10;

    private Sprite sprite;
    private AnimationTimer timer;
    private Stack<Point2D> movementStack = new Stack<>();

    public Movement(Sprite sprite) {
        this.sprite = sprite;

        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (!movementStack.empty()) update(movementStack.pop());
            }
        };

        timer.start();
    }

    public void move(Point2D trans) {
        App.logger.debug("Translation ({}, {})", trans.getX(), trans.getY());

        if (trans.getX() == 0 && trans.getY() == 0) 
            App.logger.error("Empty movement call");

        trans = new Point2D(trans.getX(), -trans.getY());

        Point2D target = trans.add(
            sprite.getTranslateX(),
            sprite.getTranslateY()
        );

        movementStack.push(target);
    }

    private void update(Point2D target) {
        App.logger.debug("Current {} {} Target {} {}", sprite.getTranslateX(), sprite.getTranslateY(), target.getX(), target.getY());

        // Sprite[] sprites = App.getSprites();

        // for (Sprite spr : )

        if (sprite.getTranslateX() != target.getX()) {
            double disp = target.getX() - sprite.getTranslateX();
            sprite.setTranslateX(sprite.getTranslateX() + disp / Math.abs(disp) * SPEED);
        }
        if (sprite.getTranslateY() != target.getY()) {
            double disp = target.getY() - sprite.getTranslateY();
            sprite.setTranslateY(sprite.getTranslateY() + disp / Math.abs(disp) * SPEED);
        }
        App.logger.debug("{} {}", sprite.getX(), sprite.getY());

        // if (
        //     sprite.getTranslateX() == target.getX() && 
        //     sprite.getTranslateY() == target.getY()
        // ) timer.stop();
    }
}


