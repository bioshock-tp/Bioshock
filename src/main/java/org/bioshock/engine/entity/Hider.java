package org.bioshock.engine.entity;

import org.bioshock.engine.animations.PlayerAnimations;
import org.bioshock.engine.animations.Sprite;
import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.networking.NetworkManager;
import org.bioshock.engine.renderers.PlayerSpriteRenderer;
import org.bioshock.engine.renderers.components.PlayerRendererC;
import org.bioshock.main.App;
import org.bioshock.utils.GlobalConstants;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

public class Hider extends SquareEntity {
    private boolean dead = false;
    private Sprite currentSprite;
    private PlayerAnimations playerAnimations;


    public Hider(Point3D p, NetworkC com, Size s, int r, Color c) {
        super(p, com, new PlayerRendererC(), s, r, c);

        renderer = PlayerSpriteRenderer.class;
    }

    protected void tick(double timeDelta) {
        if (!dead) movement.tick(timeDelta);
    }

    public void initAnimations() {
        playerAnimations = new PlayerAnimations(
            this,
            GlobalConstants.PLAYER_SCALE
        );
        currentSprite = playerAnimations.getPlayerIdleSprite();
    }

    private void setCurrentSprite(Sprite s) {
        if (s != null) {
            currentSprite = s;
        } else {
            App.logger.debug("Sprite is missing!");
        }
    }

    public void setDead(boolean d) {
        if (!dead && d) {
            rendererC.setColour(Color.GREY);
            if (App.isNetworked()) NetworkManager.kill(this);

            hitbox = new Rectangle();
        }

        dead = d;

        setCurrentSprite(playerAnimations.getPlayerDying());
    }

    @Override
    public void setAnimation() {
        Point2D translation = movement.getDirection();

        int x = (int) translation.getX();
        int y = (int) translation.getY();

        Sprite animation = playerAnimations.getPlayerIdleSprite();

        if (x > 0) animation = playerAnimations.getMoveRightSprite();

        else if (x < 0) animation = playerAnimations.getMoveLeftSprite();

        else if (y > 0) animation = playerAnimations.getMoveDownSprite();

        else if (y < 0) animation = playerAnimations.getMoveUpSprite();

        setCurrentSprite(animation);
    }

    public boolean isDead() {
        return dead;
    }

    @Override
    public Pair<Point2D, Point2D> getRenderArea() {
        Point2D centre = getCentre();
        double radius = getRadius();
        return new Pair<>(
            centre.subtract(radius, radius),
            centre.add(radius, radius)
        );
    }

    public Sprite getCurrentSprite() {
        return currentSprite;
    }
}
