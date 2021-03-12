package org.bioshock.engine.ai;

import javafx.geometry.Point2D;
import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.entity.EntityManager;
import org.bioshock.engine.entity.Hider;
import org.bioshock.engine.entity.Size;
import org.bioshock.engine.entity.SquareEntity;
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
import org.bioshock.scenes.MainGame;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SeekerAI extends SquareEntity {
    private Hider target;
    private Arc swatterHitbox;
    private ThreeByThreeMap map;
    private List<Room> path = new ArrayList<>();
    private Room currentRoom;

    private boolean isActive = false;
    private boolean isSearching = false;

    public SeekerAI(Point3D p, NetworkC com, Size s, int r, Color c, Hider e, ThreeByThreeMap m) {
        super(p, com, s, r, c);

        target = e;

        movement.setSpeed(2.5);

        renderer = SeekerRenderer.class;

        map = m;

        swatterHitbox = new Arc(getCentre().getX(), getCentre().getY(), 150,150,30, 120);
        swatterHitbox.setType(ArcType.ROUND);

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

        if (
            EntityManager.isManaged(this, target)
            && intersects(target, "swatter")
        ) {
            setActive(true);
            target.setDead(true);
            rendererC.setColor(Color.GREEN);
        }
        if (
            EntityManager.isManaged(this, target)
            && intersects(target, "fov")
        ) {

            setSearch(false);
            path.clear();

            movement.move(target.getPosition().subtract(this.getPosition()));
        }
        else{
            if(!isSearching && !path.isEmpty()){
                path = createPath(findCurrentRoom());
                setSearch(true);
                currentRoom = path.remove(0);
            }
            else if(!path.isEmpty()){
                moveToCentre(currentRoom);
                if(currentRoom.getRoomCenter().getX() == getX() && currentRoom.getRoomCenter().getY() == getY()){
                    currentRoom = path.remove(0);
                }
            }
            else{
                setSearch(false);
            }

        }
    }

    public void search(){

    }

    private Room findCurrentRoom(){
        Room currentRoom = null;
        double temp;
        double shortest = WindowManager.getWindowWidth() * WindowManager.getWindowHeight();

        for(Room room : map.getRooms()){
            temp = (room.getRoomCenter().subtract(new Point3D(this.getX(), this.getY(), room.getZ()))).magnitude();
            if(temp < shortest){
                shortest = temp;
                currentRoom = room;
            }
        }
        
        return currentRoom;
    }

    private void moveToCentre(Room room){
        movement.move(new Point2D(room.getRoomCenter().getX(), room.getRoomCenter().getY()).subtract(this.getPosition()));
    }

    private List<Room> createPath(Room startRoom){
        List<Room> path = new ArrayList<>();
        List<Room> unvisited = map.getRooms();
        List<Room> adjacents = Arrays.asList(startRoom.getAdjacentRooms().clone());

        Room currentRoom = startRoom;

        Random rand = new Random();

        unvisited.remove(startRoom);


        int r = rand.nextInt(unvisited.size());
        Room destination = unvisited.get(r);

        path.add(startRoom);

        while(currentRoom != destination){
            adjacents.removeIf(path::contains);
            r = rand.nextInt(adjacents.size());
            currentRoom = adjacents.get(r);
            adjacents = Arrays.asList(currentRoom.getAdjacentRooms().clone());

            path.add(currentRoom);

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

    public void setSwatterRot() {
        double r = movement.getFacingRotate(
            target.getPosition().subtract(this.getPosition())
        );
        swatterHitbox.setStartAngle(390-r);
    }

	public Arc getSwatterHitbox() { return swatterHitbox; }

    public SquareEntity getTarget() { return target; }

    public boolean getIsActive() { return isActive; }
}
