package org.bioshock.entities.players;


import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Pair;
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
import org.bioshock.entities.map.TexRectEntity;
import org.bioshock.entities.map.utils.ConnType;
import org.bioshock.physics.Movement;
import org.bioshock.rendering.renderers.SeekerRenderer;
import org.bioshock.rendering.renderers.components.SimpleRendererC;
import org.bioshock.scenes.SceneManager;
import org.bioshock.utils.Direction;
import org.bioshock.utils.GlobalConstants;
import org.bioshock.utils.Point;
import org.bioshock.utils.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SeekerAI extends SquareEntity {
    private Hider target;
    private final Arc swatterHitbox;
    private final Graph<Room,Pair<Direction,ConnType>> roomGraph = SceneManager.getMap().getRoomGraph();
    private PathfindingC<Room,Pair<Direction, ConnType>> roomPathfinding = new PathfindingC<>(roomGraph,
            SceneManager.getMap().getRoomArray(),
            SceneManager.getMap().getRoomArray()[0][0].getTotalSize().getWidth(),
            SceneManager.getMap().getRoomArray()[0][0].getTotalSize().getHeight());
    private PathfindingC<GraphNode,Pair<Direction, Double>> nodePathfinding = new PathfindingC<>(SceneManager.getMap().getTraversableGraph(),
            SceneManager.getMap().getTraversableArray(),
            GlobalConstants.UNIT_WIDTH,
            GlobalConstants.UNIT_HEIGHT);
    private List<Point2D> path = new ArrayList<>();
    private Room currRoom;
    private Room prevRoom;
    private Point2D currentTargetLocation;
    private Point2D lastSeenPosition;
    private Point lastSeekerPosition;

    private static final double TIME_BETWEEN_SWINGS = 1.0;
    private static final double TIME_SWINGING = 1.0;
    private static final double TIME_STILL = 0.1;

    private double timeBetweenSwings = 0;
    private double timeSwinging = 0;
    private double timeStill = 0;

    private boolean isActive = false;
    private boolean isSearching = false;
    private boolean colorChanged = false;
    private boolean wooshSoundPlayed = false;

    private Random rand = new Random();

    /**
     *
     * Constructor
     *
     * @param p The location to spawn at
     * @param com The network component
     * @param s The width and height of the seeker
     * @param r Radius of fov
     * @param c Colour of seeker
     * @param e The initial player to follow
     */
    public SeekerAI(Point3D p, NetworkC com, Size s, int r, Color c, Hider e) {
        super(p, com, new SimpleRendererC(), s, r, c);

        target = e;

        movement.setSpeed(movement.getSpeed() / 2);

        renderer = SeekerRenderer.class;

        swatterHitbox = new Arc(
                getCentre().getX(),
                getCentre().getY(),
                50, 50, 30, 120
        );

        swatterHitbox.setType(ArcType.ROUND);

        currRoom = this.findCurrentRoom();
        prevRoom = currRoom;
        //nodePathfinding = new PathfindingC<>(currRoom.getTraversableGraph());
        lastSeekerPosition = getCentre();
        currentTargetLocation = new Point2D(getCentre().getX(), getCentre().getY());
    }

    protected void tick(double timeDelta) {
        timeBetweenSwings += timeDelta;
        if(getIsActive()){
            timeSwinging += timeDelta;
        }
        doActions();
        checkStill(timeDelta, getCentre());
        lastSeekerPosition = getCentre();
        setSwatterPos();
        setSwatterRot();
    }

    private void checkStill(double timeDelta, Point centre) {
        if (centre.equals(lastSeekerPosition)){
            timeStill+=timeDelta;
        }
        else{
            timeStill = 0;
        }
    }

    /***
     * The main behaviour tree for the seeker
     */
    private void doActions() {
        setSearch(true);
        Hider firstPlayer = EntityManager.getPlayers().get(0);
        boolean masterPlayer = firstPlayer == EntityManager.getCurrentPlayer();
        colorChanged = false;
        EntityManager.getPlayers().forEach(entity -> {
            if (
                    EntityManager.isManaged(this, entity)
                    		&& getIsActive()
                    		&& !entity.isDead()
                            && intersects(entity, "swatter") 
            ) {
                entity.setDead(true);
            }
            if (
                    EntityManager.isManaged(this, entity)
                    		&& !entity.isDead()
                            && intersects(entity, "fov")                           
                            && checkLineOfSight(entity)
            ) {
                if(timeBetweenSwings >= TIME_BETWEEN_SWINGS){
                    setActive(true);
                    if(!wooshSoundPlayed){
                        AudioManager.playWooshSfx();
                        wooshSoundPlayed = true;
                    }
                    if(timeSwinging >= TIME_SWINGING){
                        setActive(false);
                        wooshSoundPlayed = false;
                        timeSwinging = 0;
                        timeBetweenSwings = 0;
                    }
                }
                rendererC.setColour(Color.ORANGE);
                colorChanged = true;
                target = entity;
                if (masterPlayer) chasePlayer(target);
            }
        });
        
        if (!colorChanged) {
            rendererC.setColour(Color.INDIANRED);
        }

        if (masterPlayer && isSearching) {
        	setActive(false);
        	search();
        }
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
                entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight()
        );

        switch(type) {
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
            for(TexRectEntity wall : room.getWalls()) {
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
    private void chasePlayer(Entity entity) {
        setSearch(false);
        path.clear();
        lastSeenPosition = new Point2D(entity.getX(), entity.getY());
        path = nodePathfinding.createBestPath(this.getCentre(), lastSeenPosition);
        if(!path.isEmpty()){
            currentTargetLocation = path.remove(0);
        }
        search();
    }


    /**
     * Contains the behaviour tree for patrolling the map
     */
    public void search() {
        updateRoom(this);
        if(path.isEmpty()){
            path = roomPathfinding.createRandomPath(this.getCentre(), prevRoom, getPreferred());
            path.add(path.get((path.size())-1));
            List<Point2D> pathToNextRoom = nodePathfinding.createBestPath(this.getCentre(), path.get(0));
            path.addAll(0, pathToNextRoom);
            currentTargetLocation = path.remove(0);
        }
        else{
            //continue searching
            movement.moveTo(currentTargetLocation.subtract(new Point2D(getWidth()/2, getHeight()/2)));

            double absX = Math.abs(currentTargetLocation.getX() - getWidth()/2 - getX());
            double absY = Math.abs(currentTargetLocation.getY() - getHeight()/2 - getY());

            if(absX < 1 && absY < 1){
                currentTargetLocation = path.remove(0);
            }
        }

    }

    private Room getPreferred() {
        if(lastSeenPosition == null){
            return null;
        }
        Room room = findCurrentRoom(lastSeenPosition);
        Room finalRoom;
        List<Pair<Point2D, Direction>> points = room.getCorridorPoints();
        if(points == null || points.size() == 0){
            return null;
        }

        if(!room.equals(currRoom)){
            return room;
        }

        Direction direction = points.get(0).getValue();
        double shortest = WindowManager.getWindowWidth() * WindowManager.getWindowHeight();
        Point2D temp;

        for(Pair<Point2D, Direction> point : points){
            temp = point.getKey().subtract(lastSeenPosition);
            if(temp.magnitude() < shortest){
                shortest = temp.magnitude();
                direction = point.getValue();
            }
        }
        finalRoom = roomGraph.getNodeFromEdge(new Pair<>(direction, ConnType.ROOM_TO_ROOM),room);
        if(finalRoom == null) {
            finalRoom = roomGraph.getNodeFromEdge(new Pair<>(direction, ConnType.SUB_ROOM),room);
        }
        return finalRoom;
    }

    private void updateRoom(Entity entity){
        Room newRoom = entity.findCurrentRoom();
        if(!newRoom.equals(currRoom)){
            prevRoom = currRoom;
            currRoom = newRoom;
        }
    }

    public void setActive(boolean b) { isActive = b; }

    public void setSearch(boolean b) {isSearching = b;}

    public void setSwatterRange(double range){
        swatterHitbox.setRadiusX(range);
        swatterHitbox.setRadiusY(range);
    }

    public void setSwatterPos() {
        swatterHitbox.setCenterX(getCentre().getX());
        swatterHitbox.setCenterY(getCentre().getY());
    }

    public void setSwatterRot() {
        double r = Movement.getFacingRotate(
                target.getPosition().subtract(getPosition())
        );
        swatterHitbox.setStartAngle(30 + r);
    }

    public Arc getSwatterHitbox() { return swatterHitbox; }

    public SquareEntity getTarget() { return target; }

    public boolean getIsActive() { return isActive; }

    @Override
    public Rectangle getRenderArea() {
        Point2D centre = getCentre();
        double radius = getRadius();
        return new Rectangle(centre.getX()-radius, centre.getY()-radius, radius*2, radius*2);
    }
}
