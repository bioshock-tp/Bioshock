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

/**
 * A class representing a hider
 */
public class Hider extends SquareEntity implements Collisions {

    private double initPositionX = 0;

    private double initPositionY = 0;


    /**
     * True if this {@link Hider} is dead
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
     * True if sound effect has been played
     */
    private boolean playedSfx = false;

    /**
     * The name of the Hider
     */
    private String name = "Hider";


    /**
     * Construct a new Hider
     * @param position The top left position of the hider
     * @param networkComponent The network component
     * @param size The size of the hider
     * @param fovRadius The radius of the FOV of the hider
     * @param colour The colour of the Hider
     */
    public Hider(
        Point3D position,
        NetworkC networkComponent,
        Size size,
        int fovRadius,
        Color colour
    ) {
        super(
            position,
            networkComponent,
            new PlayerRendererC(),
            size,
            fovRadius,
            colour
        );

        renderer = PlayerSpriteRenderer.class;

        initCollision(this);
    }


    @Override
    protected void tick(double timeDelta) {
        if (
            !dead
            && !movement.movementPaused()
        ) {
            movement.tick(timeDelta);
            setAnimation();
            setWalkingSfx();
        } else if (!dead) {
            setAnimation(hiderAnimations.getPlayerIdleSprite());
        }
    }


    @Override
    public void collisionTick(Set<Entity> collisions) {
        if (dead) return;

        /* Collision Candidates*/

        /* Other players */
        List<Hider> otherPlayers = new ArrayList<>(EntityManager.getPlayers());
        otherPlayers.remove(EntityManager.getCurrentPlayer());

        /* Seekers */
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
     * Initialise the animations of the hider
     */
    public void initAnimations() {
        hiderAnimations = new HiderAnimations(
            this,
            (int) GlobalConstants.PLAYER_SCALE
        );
        currentSprite = hiderAnimations.getPlayerIdleSprite();
    }


    /**
     * @param hiderName The new name
     */
    public void setName(String hiderName) {
        name = hiderName;
    }


    /**
     * @param sprite The new sprite
     */
    private void setCurrentSprite(Sprite sprite) {
        if (sprite != null) {
            currentSprite = sprite;
        } else {
            App.logger.error("Sprite is missing!");
        }
    }


    /**
     * @param dead True if player is dead
     */
    public void setDead(boolean dead) {
        /* Only run of first death */
        if (!this.dead && dead) {
            rendererC.setColour(Color.GREY);
            if (App.isNetworked()) {
                NetworkManager.kill(this);
            }

            hitbox = new Rectangle();

            if (this == EntityManager.getCurrentPlayer()) {
                RenderManager.setClip(false);
            }

            setCurrentSprite(hiderAnimations.getPlayerDying());
        }

        this.dead = dead;
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
     * Manually sets current animation
     * @param sprite The animation to be played
     */
    private void setAnimation(Sprite sprite) {
        setCurrentSprite(sprite);
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
     * @return True if hider is dead
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
     * @return The hiders name
     */
    public String getName() {
        return name;
    }


    /**
     * @return The current sprite
     */
    public Sprite getCurrentSprite() {
        return currentSprite;
    }


    /**
     * @param x the x coordinate of the position that this hider initially
     * had
     */
    public void setInitPositionX(double x) {
        initPositionX = x;
    }


    /**
     * @param y the y coordinate of the position that this hider initially had
     */
    public void setInitPositionY(double y) {
        initPositionY = y;
    }


    /**
     * @return the x coordinate of the position that this hider initially had
     */
    public double getInitPositionX() {
        return  initPositionX;
    }


    /**
     * @return the y coordinate of the position that this hider initially had
     */
    public double getInitPositionY() {
        return initPositionY;
    }
}
