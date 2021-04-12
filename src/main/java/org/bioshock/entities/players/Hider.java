package org.bioshock.entities.players;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bioshock.animations.PlayerAnimations;
import org.bioshock.animations.Sprite;
import org.bioshock.components.NetworkC;
import org.bioshock.entities.Entity;
import org.bioshock.entities.EntityManager;
import org.bioshock.entities.SquareEntity;
import org.bioshock.entities.map.Room;
import org.bioshock.main.App;
import org.bioshock.networking.NetworkManager;
import org.bioshock.physics.Collisions;
import org.bioshock.rendering.renderers.PlayerSpriteRenderer;
import org.bioshock.rendering.renderers.components.PlayerRendererC;
import org.bioshock.utils.GlobalConstants;
import org.bioshock.utils.Size;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

public class Hider extends SquareEntity implements Collisions {
    private boolean dead = false;
    private Sprite currentSprite;
    private PlayerAnimations playerAnimations;

    public Hider(Point3D p, NetworkC com, Size s, int r, Color c) {
        super(p, com, new PlayerRendererC(), s, r, c);

        renderer = PlayerSpriteRenderer.class;

        initCollision(this);
    }

    protected void tick(double timeDelta) {
        movement.tick(timeDelta);
        if (!dead) setAnimation();
    }

    @Override
    public void collisionTick(Set<Entity> collisions) {
        if (dead) return;

        /* Collision Candidates*/

        /* Other players */
        List<Hider> otherPlayers = new ArrayList<>(EntityManager.getPlayers());
        otherPlayers.remove(EntityManager.getCurrentPlayer());

        /* Seeker */
        SeekerAI seeker = EntityManager.getSeeker();

        /* Walls of two nearest rooms */
        Set<Entity> walls = new HashSet<>();
        Room[] rooms = getCurrentRooms();
        walls.addAll(rooms[0].getWalls());
        walls.addAll(rooms[1].getWalls());

        Set<Entity> collisionCandidates = new HashSet<>();
        collisionCandidates.addAll(otherPlayers);
        collisionCandidates.add(seeker);
        collisionCandidates.addAll(walls);

        collisions.retainAll(collisionCandidates);

        if (!collisions.isEmpty()) {
            movement.moveBack(collisions);
        }
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
            App.logger.error("Sprite is missing!");
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
