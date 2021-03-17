package org.bioshock.engine.ai;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.entity.Entity;
import org.bioshock.engine.entity.EntityManager;
import org.bioshock.engine.entity.Hider;
import org.bioshock.engine.entity.Size;
import org.bioshock.engine.entity.SquareEntity;
import org.bioshock.engine.physics.Movement;
import org.bioshock.engine.renderers.SeekerRenderer;
import org.bioshock.engine.renderers.components.SimpleRendererC;
import org.bioshock.engine.scene.SceneManager;
import org.bioshock.entities.TexRectEntity;
import org.bioshock.entities.map.Room;
import org.bioshock.entities.map.ThreeByThreeMap;
import org.bioshock.main.App;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Pair;

public class SeekerAI extends SquareEntity {
    private Hider target;
    private Arc swatterHitbox;
    private ThreeByThreeMap map = SceneManager.getMap();
    private List<Room> path = new ArrayList<>();
    private Room currRoom;
    private Room lastSeenPosition;

    private boolean isActive = false;
    private boolean isSearching = false;

    private Random rand = new Random();

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

        currRoom = findCurrentRoom(this);
    }

    protected void tick(double timeDelta) {
        doActions();
        setSwatterPos();
        setSwatterRot();
    }

    private void doActions() {
        setSearch(true);
        EntityManager.getPlayers().forEach(entity -> {
            if (
                EntityManager.isManaged(this, entity)
                && intersects(entity, "swatter")
                && !entity.isDead()
            ) {
                setActive(true);
                entity.setDead(true);
            }
            if (
                EntityManager.isManaged(this, entity)
                && intersects(entity, "fov")
                && !entity.isDead()
                && checkLineOfSight(entity)
            ) {
                rendererC.setColour(Color.ORANGE);
                target = entity;
                chasePlayer(target);
            }
        });

        if (isSearching) {
            search();
        }
    }

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
        roomsToCheck.add(findCurrentRoom(entity));

        for (Room room : roomsToCheck) {
            for(TexRectEntity wall : room.getWalls()) {
                wallHitbox = new Rectangle(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
                wallHitbox.getTransforms().add(wall.getRotate());
                Shape intersect = Shape.intersect(line, wallHitbox);
                if (intersect.getBoundsInLocal().getWidth() != -1) {
                    return false;
                }
            }
        }
        return true;
    }

    private void chasePlayer(Entity e) {
        setSearch(false);
        path.clear();
        lastSeenPosition = findCurrentRoom(e);
        App.logger.debug(
            "Last seen position coordinates are {}",
            lastSeenPosition.getRoomCenter()
        );

        if(Objects.equals(findCurrentRoom(e), findCurrentRoom(this))) {
            movement.moveTo(e.getPosition());
        }
        else{
            moveToCentre(lastSeenPosition);
        }
    }

    private Room findCurrentRoom(Entity e) {
        Room current = null;
        double temp;
        double shortest = WindowManager.getWindowWidth() * WindowManager.getWindowHeight();

        for(Room room : map.getRooms()) {
            temp = (room.getRoomCenter().subtract(new Point3D(e.getX(), e.getY(), room.getZ()))).magnitude();
            if(temp < shortest) {
                shortest = temp;
                current = room;
            }
        }
        if(current == null) {
            App.logger.error("Error current = null");
            return null;
        }

        return current;
    }

    private void moveToCentre(Room room) {
        movement.moveTo(
            room.getRoomCenter().getX(),
            room.getRoomCenter().getY()
        );
    }

    private List<Room> createPath(Room startRoom) {
        List<Room> pathToFollow = new ArrayList<>();
        List<Room> possibleMoves = new ArrayList<>();
        Room[] adjacents;
        Room destination;
        Room current;
        int r;
        int c = 0;

        current = startRoom;
        destination = startRoom;
        App.logger.debug("Start room is {}", startRoom.getRoomCenter());

        while(destination == startRoom) {
            r = rand.nextInt(map.getRooms().size());
            destination = map.getRooms().get(r);
        }
        App.logger.debug(
            "Destination room is {}",
            destination.getRoomCenter()
        );

        pathToFollow.add(startRoom);
        App.logger.debug("Room {} is {}", c, startRoom.getRoomCenter());
        c++;

        while (current != destination) {
            adjacents = current.getAdjacentRooms();
            for (int i = 0; i < 4; i++) {
                if (
                    adjacents[i] != null
                    && !pathToFollow.contains(adjacents[i])
                ) {
                        possibleMoves.add(adjacents[i]);
                }
            }

            if(!possibleMoves.isEmpty()) {
                r = rand.nextInt(possibleMoves.size());
                current = possibleMoves.get(r);
            }
            else{
                destination = current;
            }

            pathToFollow.add(current);
            App.logger.debug("Room {} is {}", c, current.getRoomCenter());
            possibleMoves.clear();
            c++;

        }

        return pathToFollow;
    }

    public void search() {
        if (path.isEmpty()) {
            if (lastSeenPosition != null) {
                currRoom = lastSeenPosition;
            }

            double absX = Math.abs(currRoom.getRoomCenter().getX() - getX());
            double absY = Math.abs(currRoom.getRoomCenter().getY() - getY());
            if (
                absX < 5
                && absY < 5
            ) {
                if (lastSeenPosition != null) {
                    path = createPath(lastSeenPosition);
                    lastSeenPosition = null;
                }
                else {
                    path = createPath(findCurrentRoom(this));
                }
                currRoom = path.remove(0);
            }
            else{
                moveToCentre(currRoom);
            }
        }
        else {
            moveToCentre(currRoom);
            if(Math.abs(currRoom.getRoomCenter().getX() - getX()) < 5 && Math.abs(currRoom.getRoomCenter().getY() - getY()) < 5) {
                currRoom = path.remove(0);
            }
        }
    }

    public void setActive(boolean b) { isActive = b; }

    public void setSearch(boolean b) {isSearching = b;}

    public void setSwatterPos() {
        swatterHitbox.setCenterX(getCentre().getX());
        swatterHitbox.setCenterY(getCentre().getY());
    }

    public void setSwatterRot() {
        double r = Movement.getFacingRotate(
            target.getPosition().subtract(getPosition())
        );
        swatterHitbox.setStartAngle(390 - r);
    }

    public Arc getSwatterHitbox() { return swatterHitbox; }

    public SquareEntity getTarget() { return target; }

    public boolean getIsActive() { return isActive; }

    @Override
    public Pair<Point2D, Point2D> getRenderArea() {
        Point2D centre = getCentre();
        double radius = getRadius();
        return new Pair<>(
            centre.subtract(radius, radius),
            centre.add(radius, radius)
        );
    }
}
