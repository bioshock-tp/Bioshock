package org.bioshock.entities.players;

import org.bioshock.animations.PlayerAnimations;
import org.bioshock.animations.Sprite;
import org.bioshock.components.NetworkC;
import org.bioshock.entities.SquareEntity;
import org.bioshock.main.App;
import org.bioshock.networking.NetworkManager;
import org.bioshock.rendering.renderers.PlayerSpriteRenderer;
import org.bioshock.rendering.renderers.components.PlayerRendererC;
import org.bioshock.utils.GlobalConstants;
import org.bioshock.utils.Size;

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
    public Rectangle getRenderArea() {
        Point2D centre = getCentre();
        double radius = getRadius();
        return new Rectangle(centre.getX()-radius, centre.getY()-radius, radius*2, radius*2);
    }

    public Sprite getCurrentSprite() {
        return currentSprite;
    }
}
