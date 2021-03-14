package org.bioshock.engine.ai;


import javafx.geometry.Point2D;
import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.entity.*;
import org.bioshock.engine.renderers.SeekerRenderer;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import org.bioshock.entities.map.Room;
import org.bioshock.entities.map.ThreeByThreeMap;
import org.bioshock.main.App;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class SeekerAI extends SquareEntity {
    private Hider target;
    private Arc swatterHitbox;
    private ThreeByThreeMap map;
    private List<Room> path = new ArrayList<>();
    private Room currentRoom;
    private Room lastSeenPosition;

    private boolean isActive = false;
    private boolean isSearching = false;

    public SeekerAI(Point3D p, NetworkC com, Size s, int r, Color c, Hider e, ThreeByThreeMap m) {
        super(p, com, s, r, c);

        target = e;

        movement.setSpeed(3.5);

        renderer = SeekerRenderer.class;

        map = m;

        swatterHitbox = new Arc(getCentre().getX(), getCentre().getY(), 150,150,30, 120);
        swatterHitbox.setType(ArcType.ROUND);

        currentRoom = findCurrentRoom(this);


    }

    private boolean intersects(SquareEntity entity, String type) {
        Shape intersect;
        Rectangle entityHitbox = new Rectangle(
            entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight()
        );

        switch(type){
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



    public void doActions() {
        setSearch(true);
        EntityManager.getPlayers().forEach(entity -> {
            if (
                    EntityManager.isManaged(this, entity)
                            && intersects(entity, "swatter")
            ) {
                setActive(true);
                entity.setDead(true);
                rendererC.setColor(Color.GREEN);
            }
            if (
                    EntityManager.isManaged(this, entity)
                            && intersects(entity, "fov")
            ) {
                target = entity;
                chasePlayer(target);
            }
        });
        if(isSearching){
            search();
        }
    }

    public void search(){
        if(path.isEmpty()){
            if(lastSeenPosition != null){
                currentRoom = lastSeenPosition;
            }
            if(Math.abs(currentRoom.getRoomCenter().getX() - getX()) < 5 && Math.abs(currentRoom.getRoomCenter().getY() - getY()) < 5){
                if(lastSeenPosition != null){
                    path = createPath(lastSeenPosition);
                    lastSeenPosition = null;
                }
                else {
                    path = createPath(findCurrentRoom(this));
                }
                currentRoom = path.remove(0);
            }
            else{
                moveToCentre(currentRoom);
            }
        }
        else {
            moveToCentre(currentRoom);
            if(Math.abs(currentRoom.getRoomCenter().getX() - getX()) < 5 && Math.abs(currentRoom.getRoomCenter().getY() - getY()) < 5){
                currentRoom = path.remove(0);
            }
        }
    }

    private void chasePlayer(Entity e){
        setSearch(false);
        path.clear();
        lastSeenPosition = findCurrentRoom(e);
        App.logger.debug("Last seen position coordinates are "+ lastSeenPosition.getRoomCenter());


        if(Objects.equals(findCurrentRoom(e), findCurrentRoom(this))){
            movement.move(e.getPosition().subtract(this.getPosition()));
        }
        else{
            moveToCentre(lastSeenPosition);
        }

    }

    private Room findCurrentRoom(Entity e){
        Room current = null;
        double temp;
        double shortest = WindowManager.getWindowWidth() * WindowManager.getWindowHeight();

        for(Room room : map.getRooms()){
            temp = (room.getRoomCenter().subtract(new Point3D(e.getX(), e.getY(), room.getZ()))).magnitude();
            if(temp < shortest){
                shortest = temp;
                current = room;
            }
        }
        if(current == null){
            App.logger.error("Error current = null");
            return null;
        }

        return current;
    }

    private void moveToCentre(Room room){
        movement.move(new Point2D(room.getRoomCenter().getX(), room.getRoomCenter().getY()).subtract(this.getPosition()));
    }

    private List<Room> createPath(Room startRoom){
        List<Room> path = new ArrayList<>();
        List<Room> possibleMoves = new ArrayList<>();
        Room[] adjacents;
        Room destination;
        Room current;
        Random rand = new Random();
        int r;
        int c = 0;

        current = startRoom;
        destination = startRoom;
        App.logger.debug("Start room is " + startRoom.getRoomCenter());

        while(destination == startRoom){
            r = rand.nextInt(map.getRooms().size());
            destination = map.getRooms().get(r);
        }
        App.logger.debug("Destination room is " + destination.getRoomCenter());

        path.add(startRoom);
        App.logger.debug("Room " + c + " is " + startRoom.getRoomCenter());
        c++;

        while(current != destination){

            adjacents = current.getAdjacentRooms();
            for(int i = 0; i < 4; i++){
                if(adjacents[i] != null){
                    if(!path.contains(adjacents[i])){
                        possibleMoves.add(adjacents[i]);
                    }
                }
            }



            if(!possibleMoves.isEmpty()){
                r = rand.nextInt(possibleMoves.size());
                current = possibleMoves.get(r);
            }
            else{
                destination = current;
            }

            path.add(current);
            App.logger.debug("Room " + c + " is " + current.getRoomCenter());
            possibleMoves.clear();
            c++;

        }

        return path;
    }




    protected void tick(double timeDelta) {
        doActions();
        setSwatterPos();
        setSwatterRot();
    }


    public void setActive(boolean b) { isActive = b; }

    public void setSearch(boolean b) {isSearching = b;}

    public void setSwatterPos() {
        swatterHitbox.setCenterX(getCentre().getX());
        swatterHitbox.setCenterY(getCentre().getY());
    }

    public void setSwatterRot(){
        double r = movement.getFacingRotate(target.getPosition().subtract(this.getPosition()));
        swatterHitbox.setStartAngle(390-r);
    }

	public Arc getSwatterHitbox() { return swatterHitbox; }

    public SquareEntity getTarget() { return target; }

    public boolean getIsActive() { return isActive; }
}
