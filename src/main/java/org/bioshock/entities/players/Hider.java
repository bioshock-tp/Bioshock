package org.bioshock.entities.players;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.bioshock.audio.AudioManager.playWalkingSfx;
import static org.bioshock.audio.AudioManager.stopWalkingSfx;

/**
 * 
 * A class representing a hider
 *
 */
public class Hider extends SquareEntity implements Collisions {
    /**
     * The powerup manager for the hider
     */
    private final PowerUpManager powerUpManager = new PowerUpManager(this);
    /**
     * Boolean of whether the hider is dead or alive
     */
    private boolean dead = false;
    /**
     * The current sprite for the hider
     */
    private Sprite currentSprite;
    /**
     * The Hider animations
     */
    private HiderAnimations hiderAnimations;
    /**
     * Boolean of if the sound effect has been played or not
     */
    private boolean playedSfx = false;
    /**
     * The name of the Hider
     */
    private String name;

    /**
     * Construct a new Hider
     * @param p The top left position of the hider
     * @param com The network component
     * @param s The size of the hider
     * @param r The radius of the FOV of the hider
     * @param c The colour of the Hider
     */
    public Hider(Point3D p, NetworkC com, Size s, int r, Color c) {
        super(p, com, new PlayerRendererC(), s, r, c);

        renderer = PlayerSpriteRenderer.class;

        initCollision(this);
    }

    @Override
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
        List<SeekerAI> seekers = EntityManager.getSeekers();

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

    /**
     * Initialize the animations of the hider
     */
    public void initAnimations() {
        hiderAnimations = new HiderAnimations(
            this,
            (int) GlobalConstants.PLAYER_SCALE
        );
        currentSprite = hiderAnimations.getPlayerIdleSprite();
    }

    /**
     * Set the name of the hider
     * @param hiderName The new name
     */
    public void setName(String hiderName){
        name = hiderName;
    }

    /**
     * Set the current sprite
     * @param s The new sprite
     */
    private void setCurrentSprite(Sprite s) {
        if (s != null) {
            currentSprite = s;
        } else {
            App.logger.error("Sprite is missing!");
        }
    }
    
    /**
     * Set the hider to be dead or not
     * @param d
     */
    public void setDead(boolean d) {
        if (!dead && d) {
            rendererC.setColour(Color.GREY);
            if (App.isNetworked()) {
                NetworkManager.kill(this);
            }

            hitbox = new Rectangle();

            RenderManager.setClip(false);
        }

        dead = d;
        if(d) {
            setCurrentSprite(hiderAnimations.getPlayerDying());
        }
    }

    /**
     * Set the hiders animation based on how it moved from the last tick
     */
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

    /**
     * Set the walking sound effect based on how the hider moved compared 
     * to the last tick
     */
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

    /**
     * Getter
     * @return If the hider is dead or not
     */
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

    /**
     * Getter
     * @return The hiders name
     */
    public String getName() {
        return name;
    }
    
    /**
     * 
     * @return The current sprite
     */
    public Sprite getCurrentSprite() {
        return currentSprite;
    }

    /**
     * 
     * @return The powerup manager
     */
    public PowerUpManager getPowerUpManager() { return powerUpManager; }
}
