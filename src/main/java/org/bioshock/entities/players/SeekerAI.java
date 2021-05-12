package org.bioshock.entities.players;


import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Pair;
import org.bioshock.animations.SeekerAnimations;
import org.bioshock.animations.Sprite;
import org.bioshock.animations.SwingAnimations;
import org.bioshock.audio.AudioManager;
import org.bioshock.components.NetworkC;
import org.bioshock.components.PathfindingC;
import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.pathfinding.Graph;
import org.bioshock.engine.pathfinding.GraphNode;
import org.bioshock.entities.Entity;
import org.bioshock.entities.EntityManager;
import org.bioshock.entities.SquareEntity;
import org.bioshock.entities.map.Room;
import org.bioshock.entities.map.Wall;
import org.bioshock.entities.map.utils.ConnType;
import org.bioshock.main.App;
import org.bioshock.physics.Collisions;
import org.bioshock.physics.Movement;
import org.bioshock.rendering.renderers.SeekerRenderer;
import org.bioshock.rendering.renderers.components.SimpleRendererC;
import org.bioshock.scenes.SceneManager;
import org.bioshock.utils.Direction;
import org.bioshock.utils.GlobalConstants;
import org.bioshock.utils.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A class to represent the AI Seeker entity
 * @author Kian Wells - Ben Stein
 */
public class SeekerAI extends SquareEntity implements Collisions {

    /**
     * The current target for the seeker to follow
     */
    private Hider target;

    /**
     * The arc shaped hitbox that will act as the weapon
     */
    private Arc swatterHitbox;

    /**
     * The graph of rooms in the map
     */
    private final Graph<Room, Pair<Direction, ConnType>> roomGraph =
        SceneManager.getMap().getRoomGraph();

    /**
     * A pathfinding component that is initialised with the room graph for
     * pathfinding from room to room
     */
    private PathfindingC<Room, Pair<Direction, ConnType>> roomPathfinding =
    new PathfindingC<>(
        roomGraph,
        SceneManager.getMap().getRoomArray(),
        SceneManager.getMap().getRoomArray()[0][0].getTotalSize().getWidth(),
        SceneManager.getMap().getRoomArray()[0][0].getTotalSize().getHeight()
    );

    /**
     * A pathfinding component that is initialised with the complete node map
     * for pathfinding between obstacles and within rooms
     */
    private PathfindingC<GraphNode, Pair<Direction, Double>> nodePathfinding =
    new PathfindingC<>(
        SceneManager.getMap().getTraversableGraph(),
        SceneManager.getMap().getTraversableArray(),
        GlobalConstants.UNIT_WIDTH,
        GlobalConstants.UNIT_HEIGHT
    );

    /**
     * Will contain the path that the seeker should follow
     */
    private List<Point2D> path = new ArrayList<>();

    /**
     * Contains the current room
     */
    private Room currRoom;

    /**
     * Contains the previous room to be used as a room to avoid when
     * pathfinding
     */
    private Room prevRoom;

    /**
     * The location that the seeker is currently trying to move towards
     */
    private Point2D currentTargetLocation;

    /**
     * The last seen position of the target
     */
    private Point2D lastSeenPosition;

    /**
     * The current sprite displayed
     */
    private Sprite currentSprite;

    private Sprite currentSwingAnimation;
    private SeekerAnimations seekerAnimations;
    private SwingAnimations swingAnimations;

    /**
     * The time in seconds that should elapse between the seeker attacks
     */
    private static final double TIME_BETWEEN_SWINGS = 1.0;

    /**
     * The time in seconds that should elapse between the seeker starting an
     * attack and finishing it
     */
    private static final double TIME_SWINGING = 1.0;

    /**
     * Doubles used to count until the next action is needed
     */
    private double timeBetweenSwings = 0;
    private double timeSwinging = 0;

    /**
     * True if the seeker is attacking, false otherwise
     */
    private boolean isActive = false;

    /**
     * True of the seeker is searching, false otherwise
     */
    private boolean isSearching = false;

    /**
     * True if the woosh sound has been played after an attack, used to stop it
     * repeating
     */
    private boolean wooshSoundPlayed = false;


    /**
     *
     *
     * @param p The location to spawn at
     * @param com The network component
     * @param s The width and height of the seeker
     * @param r Radius of fov
     * @param c Colour of seeker
     */
    public SeekerAI(Point3D p, NetworkC com, Size s, int r, Color c) {
        super(p, com, new SimpleRendererC(), s, r, c);

        movement.setSpeed(movement.getSpeed() / 1.5);

        renderer = SeekerRenderer.class;

        swatterHitbox = new Arc(
            getCentre().getX(),
            getCentre().getY(),
            50, 50, 30, 120
        );

        swatterHitbox.setType(ArcType.ROUND);

        currRoom = this.findCurrentRoom();
        prevRoom = currRoom;

        currentTargetLocation = new Point2D(
            getCentre().getX(),
            getCentre().getY()
        );
    }

    /**
     * Initialises animations by setting their scale and default states.
     */
    public void initAnimations() {
        seekerAnimations = new SeekerAnimations(
            this,
            GlobalConstants.PLAYER_SCALE
        );
        currentSprite = seekerAnimations.getPlayerIdleSprite();
        swingAnimations = new SwingAnimations(
            this,
            1.5
        );
        currentSwingAnimation = SwingAnimations.getIdle();
    }

    /**
     * Calls the methods needed every frame
     * @param timeDelta the time since the last frame
     */
    protected void tick(double timeDelta) {
        timeBetweenSwings += timeDelta;

        if (isActive()) {
            timeSwinging += timeDelta;
        }

        doActions();
        setSwatterPos();
        setSwatterRot();

        setAnimation();
        setSwingAnimation();

        movement.tick(timeDelta);
    }


    @Override
    public void collisionTick(Set<Entity> collisions) {
        /* Walls of current room */
        List<Wall> walls = this.findCurrentRoom().getWalls();

        collisions.retainAll(walls);

        if (!collisions.isEmpty()) movement.moveBack(collisions);
    }


    /***
     * The main behaviour tree for the seeker
     * Calls different methods depending on the game state
     */
    private void doActions() {
        setSearch(true);

        if (target == null) target = EntityManager.getCurrentPlayer();

        Hider firstPlayer = EntityManager.getPlayers().get(0);
        boolean masterPlayer = firstPlayer == EntityManager.getCurrentPlayer();

        EntityManager.getPlayers().forEach(entity -> {
            //If the swatter hitbox is active and touches the player
            if (
                EntityManager.isManaged(this, entity)
                && isActive()
                && !entity.isDead()
                && intersects(entity, "swatter")
            ) {
                entity.setDead(true);
            }

            //If the player is within the seeker's fov
            if (
                EntityManager.isManaged(this, entity)
                && !entity.isDead()
                && !entity.isInvisible()
                && intersects(entity, "fov")
                && checkLineOfSight(entity)
            ) {
                if (
                    timeBetweenSwings >= TIME_BETWEEN_SWINGS
                    && checkInSwingDistance(entity)
                ) {
                    setActive(true);

                    if (!wooshSoundPlayed) {
                        AudioManager.playWooshSfx();
                        wooshSoundPlayed = true;
                    }

                    if (timeSwinging >= TIME_SWINGING) {
                        setActive(false);
                        wooshSoundPlayed = false;
                        timeSwinging = 0;
                        timeBetweenSwings = 0;
                    }
                }

                rendererC.setColour(Color.ORANGE);
                target = entity;
                if (masterPlayer) chasePlayer(target);
            }
        });
        if (
            EntityManager.getPlayers().stream().noneMatch(entity ->
                EntityManager.isManaged(this, entity)
                && !entity.isDead()
                && !entity.isInvisible()
                && intersects(entity, "fov")
                && checkLineOfSight(entity)
            )
        ) {
            rendererC.setColour(Color.INDIANRED);
        }

        //if none of this happens
        if (masterPlayer && isSearching) {
            setActive(false);
            search();
        }
    }

    /**
     * Checks to see if the entity is within half of the vision radius
     * This is when the seeker should swing
     * @param entity the entity to check the location of
     * @return true if it is within range, false otherwise
     */
    private boolean checkInSwingDistance(SquareEntity entity) {
        return
            entity.getCentre()
                .subtract(this.getCentre())
                .magnitude()
                <= this.getRadius() / 2;
    }

    /**
     *
     * Can check intersections between an entity and the type given
     *
     * @param entity The entity to check collisions with
     * @param type The type of collision to check (fov etc)
     * @return true if it intersects, false otherwise
     */
    private boolean intersects(SquareEntity entity, String type) {
        Shape intersect;
        Rectangle entityHitbox = new Rectangle(
            entity.getX(),
            entity.getY(),
            entity.getWidth(),
            entity.getHeight()
        );

        switch (type) {
            case "fov":
                Circle fovC = new Circle(
                    getCentre().getX(),
                    getCentre().getY(),
                    getRadius()
                );
                intersect = Shape.intersect(fovC, entityHitbox);
                break;

            case "swatter":
                intersect = Shape.intersect(swatterHitbox, entityHitbox);
                break;
            default:
                return false;
        }

        return intersect.getBoundsInLocal().getWidth() != -1;
    }


    /**
     *
     * Checks line of sight from the seeker to the entity given
     * Draws a line and if line hits a wall then it is not in line of sight
     *
     * @param entity The entity to check line of sight with
     * @return true if in line of sight, false otherwise
     */
    private boolean checkLineOfSight(SquareEntity entity) {
        Rectangle wallHitbox;

        Line line = new Line(
            getCentre().getX(),
            getCentre().getY(),
            entity.getCentre().getX(),
            entity.getCentre().getY()
        );

        List<Room> roomsToCheck = new ArrayList<>();
        roomsToCheck.add(currRoom);
        roomsToCheck.add(entity.findCurrentRoom());

        for (Room room : roomsToCheck) {
            for (Wall wall : room.getWalls()) {
                wallHitbox = new Rectangle(
                    wall.getX(),
                    wall.getY(),
                    wall.getWidth(),
                    wall.getHeight()
                );

                wallHitbox.getTransforms().add(wall.getRotate());

                Shape intersect = Shape.intersect(line, wallHitbox);

                if (intersect.getBoundsInLocal().getWidth() != -1) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     *
     * Contains behaviour tree for chasing the entity
     *
     * @param entity the entity to chase
     */
    private void chasePlayer(SquareEntity entity) {
        setSearch(false);

        path.clear();

        lastSeenPosition = new Point2D(
            entity.getCentre().getX(),
            entity.getCentre().getY()
        );

        path = nodePathfinding.createBestPath(
            this.getCentre(),
            lastSeenPosition
        );

        path.add(lastSeenPosition);

        if (!path.isEmpty()) {
            currentTargetLocation = path.remove(0);
        }

        search();
    }


    /**
     * Contains the behaviour tree for patrolling the map
     * Will move towards the target location and pop a new location off the
     * path stack
     */
    public void search() {
        updateRoom(this);

        if (path.isEmpty()) {
            path = roomPathfinding.createRandomPath(
                this.getCentre(),
                prevRoom,
                getPreferred()
            );

            path.add(path.get((path.size()) - 1));

            List<Point2D> pathToNextRoom = nodePathfinding.createBestPath(
                this.getCentre(),
                path.get(0)
            );

            path.addAll(0, pathToNextRoom);

            currentTargetLocation = path.remove(0);
        }
        else{
            // continue searching
            movement.moveTo(
                currentTargetLocation.subtract(
                    new Point2D(getWidth() / 2, getHeight() / 2)
                )
            );

            double absX = Math.abs(
                currentTargetLocation.getX() - getWidth() / 2 - getX()
            );
            double absY = Math.abs(
                currentTargetLocation.getY() - getHeight() / 2 - getY()
            );

            if (absX < 1 && absY < 1) {
                currentTargetLocation = path.remove(0);
            }
        }

    }

    /**
     * Checks if the room has changed and if yes, update the current and
     * previous room
     * @param entity the entity to update the room of
     */
    private void updateRoom(Entity entity) {
        Room newRoom = entity.findCurrentRoom();
        if (!newRoom.equals(currRoom)) {
            prevRoom = currRoom;
            currRoom = newRoom;
        }
    }

    /**
     * Sets current sprite to sprite s
     * @param s the sprite to change to
     */
    private void setCurrentSprite(Sprite s) {
        if (s != null) {
            currentSprite = s;
        } else {
            App.logger.debug("Sprite is missing!");
        }
    }

    /**
     * Changes the current swing animation to sprite s
     * @param s the sprite to change to
     */
    public void setCurrentSwingAnimation(Sprite s) {
        if (s != null) {
            currentSwingAnimation = s;
        } else {
            App.logger.debug("Sprite is missing!");
        }
    }

    /**
     * Will update the position of the arc hitbox to the seekers new centre
     */
    public void setSwatterPos() {
        swatterHitbox.setCenterX(getCentre().getX());
        swatterHitbox.setCenterY(getCentre().getY());
    }

    /**
     * Will set the rotation of the arc hitbox so that it faces the target
     */
    public void setSwatterRot() {
        double r = Movement.getFacingRotate(
            target.getPosition().subtract(getPosition())
        );
        swatterHitbox.setStartAngle(30 + r);
    }

    /**
     * Sets animation of the seeker based on the movement towards the current
     * target location.
     */
    public void setAnimation() {
        Point2D translation = currentTargetLocation.subtract(getCentre());

        int x = (int) translation.getX();
        int y = (int) translation.getY();
        Sprite animation = seekerAnimations.getPlayerIdleSprite();

        if (x > 0) animation = seekerAnimations.getMoveRightSprite();

        else if (x < 0) animation = seekerAnimations.getMoveLeftSprite();

        else if (y > 0) animation = seekerAnimations.getMoveDownSprite();

        else if (y < 0) animation = seekerAnimations.getMoveUpSprite();

        setCurrentSprite(animation);
    }


    /**
     * Sets the swing animation of the seeker based on where the hider is.
     */
    public void setSwingAnimation() {
        Sprite animation = SwingAnimations.getIdle();

        if (wooshSoundPlayed && !target.isDead()) {
            Point2D translation = currentTargetLocation.subtract(getCentre());

            int x = (int) translation.getX();
            int y = (int) translation.getY();

            if (x >= 0 && y >= 0) {
                animation = SwingAnimations.getBottomRightSwing();
            }
            else if (x >= 0) {
                animation = SwingAnimations.getTopRightSwing();
            }
            else if (y >= 0) {
                animation = SwingAnimations.getBottomLeftSwing();
            }
            else {
                animation = SwingAnimations.getTopLeftSwing();
            }
        }

        setCurrentSwingAnimation(animation);
    }


    /**
     * Will use the last seen position to estimate which room should be checked
     * next</p>
     * The closest room to the last seen position will be returned
     * @return the closest room to the last seen position, or null if last seen
     * position is null
     */
    private Room getPreferred() {
        if (lastSeenPosition == null) {
            return null;
        }

        Room room = findCurrentRoom(lastSeenPosition);
        List<Pair<Point2D, Direction>> points = room.getCorridorPoints();
        if (points == null || points.isEmpty()) {
            return null;
        }

        if (!room.equals(currRoom)) {
            return room;
        }

        Direction direction = points.get(0).getValue();
        double shortest =
            WindowManager.getWindowWidth() * WindowManager.getWindowHeight();

        Point2D temp;
        for (Pair<Point2D, Direction> point : points) {
            temp = point.getKey().subtract(lastSeenPosition);
            if (temp.magnitude() < shortest) {
                shortest = temp.magnitude();
                direction = point.getValue();
            }
        }
        Room finalRoom = roomGraph.getNodeFromEdge(
            new Pair<>(direction, ConnType.ROOM_TO_ROOM),
            room
        );

        if (finalRoom == null) {
            finalRoom = roomGraph.getNodeFromEdge(
                new Pair<>(direction, ConnType.SUB_ROOM),
                room
            );
        }
        return finalRoom;
    }


    /**
     * @param b True if this {@link SeekerAI} is active
     */
    public void setActive(boolean b) { isActive = b; }


    /**
     * @param b True if this {@link SeekerAI} is currently searching
     */
    public void setSearch(boolean b) { isSearching = b; }


    /**
     * @return the current sprite
     */
    public Sprite getCurrentSprite() {
        return currentSprite;
    }

    /**
     * @return the current swing animation
     */
    public Sprite getCurrentSwingAnimation() {
        return currentSwingAnimation;
    }


    /**
     * @return the swatter hitbox
     */
    public Arc getSwatterHitbox() { return swatterHitbox; }


    /**
     * @return the current target
     */
    public SquareEntity getTarget() { return target; }


    /**
     * @return True if this {@link SeekerAI} is active
     */
    public boolean isActive() { return isActive; }


    @Override
    public Rectangle getRenderArea() {
        return new Rectangle(
            getCentre().getX() - getRadius(),
            getCentre().getY() - getRadius(),
            getRadius() * 2,
            getRadius() * 2
        );
    }
}
