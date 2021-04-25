package org.bioshock.entities.players;

import static org.bioshock.audio.AudioManager.playWalkingSfx;
import static org.bioshock.audio.AudioManager.stopWalkingSfx;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bioshock.animations.HiderAnimations;
import org.bioshock.animations.Sprite;
import org.bioshock.components.NetworkC;
import org.bioshock.entities.Entity;
import org.bioshock.entities.EntityManager;
import org.bioshock.entities.SquareEntity;
import org.bioshock.entities.map.Wall;
import org.bioshock.entities.powerup.PowerUpManager;
import org.bioshock.main.App;
import org.bioshock.networking.NetworkManager;
import org.bioshock.physics.Collisions;
import org.bioshock.rendering.RenderManager;
import org.bioshock.rendering.renderers.PlayerSpriteRenderer;
import org.bioshock.rendering.renderers.components.PlayerRendererC;
import org.bioshock.utils.GlobalConstants;
import org.bioshock.utils.Size;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Hider extends SquareEntity implements Collisions {
    private final PowerUpManager powerUpManager = new PowerUpManager(this);
    private boolean dead = false;
    private Sprite currentSprite;
    private HiderAnimations hiderAnimations;
    private boolean playedSfx = false;

    private String name;

    public Hider(Point3D p, NetworkC com, Size s, int r, Color c) {
        super(p, com, new PlayerRendererC(), s, r, c);

        renderer = PlayerSpriteRenderer.class;

        initCollision(this);
    }

    protected void tick(double timeDelta) {
        if (!dead) {
            movement.tick(timeDelta);
            setAnimation();
            setWalkingSfx();
            powerUpManager.tick(timeDelta);
        }
    }


    @Override
    public void collisionTick(Set<Entity> collisions) {
        if (dead) return;

        /* Collision Candidates*/

        /* Other players */
        List<Hider> otherPlayers = new ArrayList<>(EntityManager.getPlayers());
        otherPlayers.remove(EntityManager.getCurrentPlayer());

        /* Seeker */
        List<SeekerAI> seekers = EntityManager.getSeeker();

        /* Walls of room */
        List<Wall> walls = new ArrayList<>(4);
        find4ClosestRooms().forEach(room -> walls.addAll(room.getWalls()));

        Set<Entity> collisionCandidates = new HashSet<>();
        collisionCandidates.addAll(otherPlayers);
        collisionCandidates.addAll(seekers);
        collisionCandidates.addAll(walls);

        collisions.retainAll(collisionCandidates);
        collisions.forEach(collision -> collision.getRendererC().setColour(Color.RED));

        if (!collisions.isEmpty()) {
            movement.moveBack(collisions);
        }
    }

    public void initAnimations() {
        hiderAnimations = new HiderAnimations(
            this,
            (int) GlobalConstants.PLAYER_SCALE
        );
        currentSprite = hiderAnimations.getPlayerIdleSprite();
    }

    public void setName(String hiderName){
        name = hiderName;
    }

    private void setCurrentSprite(Sprite s) {
        if (s != null) {
            currentSprite = s;
        } else {
            App.logger.error("Sprite is missing!");
        }
    }

    public void setDead(boolean d) {
        if (!dead && d) {
            rendererC.setColour(Color.GREY);
            if (App.isNetworked()) NetworkManager.kill(this);

            hitbox = new Rectangle();

            RenderManager.setClip(false);
        }

        dead = d;

        setCurrentSprite(hiderAnimations.getPlayerDying());
    }

    public void setAnimation() {
        Point2D translation = movement.getDirection();

        double x = translation.getX();
        double y = translation.getY();

        Sprite animation = hiderAnimations.getPlayerIdleSprite();

        if (x > 0) animation = hiderAnimations.getMoveRightSprite();

        else if (x < 0) animation = hiderAnimations.getMoveLeftSprite();

        else if (y > 0) animation = hiderAnimations.getMoveDownSprite();

        else if (y < 0) animation = hiderAnimations.getMoveUpSprite();

        setCurrentSprite(animation);
    }

    public void setWalkingSfx() {
        Point2D translation = movement.getDirection();

        int x = (int) translation.getX();
        int y = (int) translation.getY();


        if (x != 0 || y != 0) {
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
        return new Rectangle(
            centre.getX() - radius,
            centre.getY() - radius,
            radius * 2,
            radius * 2
        );
    }

    public String getName() {
        return name;
    }

    public Sprite getCurrentSprite() {
        return currentSprite;
    }

    public PowerUpManager getPowerUpManager() { return powerUpManager; }
}
