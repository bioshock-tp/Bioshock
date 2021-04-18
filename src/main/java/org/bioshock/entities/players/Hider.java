package org.bioshock.entities.players;

import static org.bioshock.audio.AudioManager.playWalkingSfx;
import static org.bioshock.audio.AudioManager.stopWalkingSfx;

import org.bioshock.animations.HiderAnimations;
import org.bioshock.animations.Sprite;
import org.bioshock.components.NetworkC;
import org.bioshock.entities.SquareEntity;
import org.bioshock.main.App;
import org.bioshock.networking.NetworkManager;
import org.bioshock.rendering.renderers.PlayerSpriteRenderer;
import org.bioshock.rendering.renderers.components.PlayerRendererC;
import org.bioshock.utils.GlobalConstants;
import org.bioshock.utils.SizeD;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Hider extends SquareEntity {
    private boolean dead = false;
    private Sprite currentSprite;
    private HiderAnimations hiderAnimations;
    boolean playedSfx = false;


    public Hider(Point3D p, NetworkC com, SizeD s, int r, Color c) {
        super(p, com, new PlayerRendererC(), s, r, c);

        renderer = PlayerSpriteRenderer.class;
    }

    protected void tick(double timeDelta) {
        if (!dead) movement.tick(timeDelta);
    }

    public void initAnimations() {
        hiderAnimations = new HiderAnimations(
            this,
            (int) GlobalConstants.PLAYER_SCALE
        );
        currentSprite = hiderAnimations.getPlayerIdleSprite();
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

        setCurrentSprite(hiderAnimations.getPlayerDying());
    }

    @Override
    public void setAnimation() {
        Point2D translation = movement.getDirection();

        int x = (int) translation.getX();
        int y = (int) translation.getY();

        Sprite animation = hiderAnimations.getPlayerIdleSprite();

        if (x > 0) animation = hiderAnimations.getMoveRightSprite();

        else if (x < 0) animation = hiderAnimations.getMoveLeftSprite();

        else if (y > 0) animation = hiderAnimations.getMoveDownSprite();

        else if (y < 0) animation = hiderAnimations.getMoveUpSprite();

        setCurrentSprite(animation);
    }

    @Override
    public void setWalkingSfx() {
        Point2D translation = movement.getDirection();

        int x = (int) translation.getX();
        int y = (int) translation.getY();


        if ((x != 0) || (y != 0)) {
            if (!playedSfx) {
                playWalkingSfx();
                playedSfx = true;
            }
        }
        else {
            stopWalkingSfx();
            playedSfx = false;
        }

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
