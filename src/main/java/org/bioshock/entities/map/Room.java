package org.bioshock.entities.map;

import java.util.ArrayList;
import java.util.List;

import org.bioshock.components.NetworkC;
import org.bioshock.utils.Size;
import org.mockito.internal.util.collections.Sets;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class Room {
    /***
     *  Stores the total size of the room
     *  i.e. the roomSize + corridor width*2 in both dimensions 
     */
    private Size totalSize;
    /***
     * Stores the internal room size
     */
    private Size roomSize;
    /***
     * Stores a list of all the walls that make up the room
     */
    private List<TexRectEntity> walls = new ArrayList<>();
    /***
     * Stores the position of the top left of the room
     */
    private Point3D pos;
    /***
     * Stores the connection to the rooms to the north south east and west
     * as a pair containing the room it's connecting to and the Connection type
     */
    private ArrayList<Pair<Room,ConnType>> connections = new ArrayList<Pair<Room,ConnType>>(); //N,S,E,W
    {
        connections.add(new Pair<Room, ConnType>(null, ConnType.ROOM_TO_ROOM)); //N
        connections.add(new Pair<Room, ConnType>(null, ConnType.ROOM_TO_ROOM)); //S
        connections.add(new Pair<Room, ConnType>(null, ConnType.ROOM_TO_ROOM)); //E
        connections.add(new Pair<Room, ConnType>(null, ConnType.ROOM_TO_ROOM)); //W
    }

    /***
     * Generates a room with the position being the top left of the room
     * @param newPos the position of the top left of the room
     * @param wallWidth the width of the walls that make up the room
     * @param newRoomSize the size of the central room
     * @param coriSize the corridor size of all exits
     * @param exits the exits the room has
     * @param c the colour of the room
     */
    public Room(
        Point3D newPos,
        double wallWidth,
        Size newRoomSize,
        Size coriSize,
        Exits exits,
        Color c
    ) {
        this.pos = newPos;
        this.roomSize = newRoomSize;
        this.totalSize = new Size(
            roomSize.getWidth() + 2 * coriSize.getHeight(),
            roomSize.getHeight() + 2 * coriSize.getHeight()
        );

        //chooses either a top side with or without an exit
        if (exits.isTop()) {
            walls.addAll(Sides.tExit(
                pos.add(coriSize.getHeight(), 0, 0),
                wallWidth,
                roomSize.getWidth(),
                coriSize.getWidth(),
                coriSize.getHeight(),
                c
            ));
        }
        else {
            walls.addAll(Sides.tNoExit(
                pos.add(coriSize.getHeight(), 0, 0),
                wallWidth,
                roomSize.getWidth(),
                coriSize.getWidth(),
                coriSize.getHeight(),
                c
            ));
        }

        //chooses either a bottom side with or without an exit
        if (exits.isBot()) {
            walls.addAll(Sides.bExit(
                pos.add(
                    coriSize.getHeight(),
                    coriSize.getHeight() + roomSize.getHeight(),
                    0
                ),
                wallWidth,
                roomSize.getWidth(),
                coriSize.getWidth(),
                coriSize.getHeight(),
                c
            ));
        }
        else {
            walls.addAll(Sides.bNoExit(
                pos.add(
                    coriSize.getHeight(),
                    coriSize.getHeight() + roomSize.getHeight(),
                    0
                ),
                wallWidth,
                roomSize.getWidth(),
                coriSize.getWidth(),
                coriSize.getHeight(),
                c
            ));
        }

        //chooses either a left side with or without an exit
        if (exits.isLeft()) {
            walls.addAll(Sides.lExit(
                pos.add(0, coriSize.getHeight(), 0),
                wallWidth, roomSize.getHeight(),
                coriSize.getWidth(),
                coriSize.getHeight(),
                c
            ));
        }
        else {
            walls.addAll(Sides.lNoExit(
                pos.add(0, coriSize.getHeight(), 0),
                wallWidth,
                roomSize.getHeight(),
                coriSize.getWidth(),
                coriSize.getHeight(),
                c
            ));
        }

        //chooses either a right side with or without an exit
        if (exits.isRight()) {
            walls.addAll(Sides.rExit(
                pos.add(
                    coriSize.getHeight() + roomSize.getWidth(),
                    coriSize.getHeight(),
                    0
                ),
                wallWidth,
                roomSize.getHeight(),
                coriSize.getWidth(),
                coriSize.getHeight(),
                c
            ));
        }
        else {
            walls.addAll(Sides.rNoExit(
                pos.add(
                    coriSize.getHeight() + roomSize.getWidth(),
                    coriSize.getHeight(),
                    0
                ),
                wallWidth,
                roomSize.getHeight(),
                coriSize.getWidth(),
                coriSize.getHeight(),
                c
            ));
        }

        //corner connecting bottom and right
        TexRectEntity corner1 = new TexRectEntity(
            pos.add(
                coriSize.getHeight() - wallWidth,
                coriSize.getHeight() - wallWidth,
                0
            ),
            new NetworkC(false),
            new Size(wallWidth, wallWidth),
            c
        );
        walls.add(corner1);

        //corner connecting bottom and left
        TexRectEntity corner2 = new TexRectEntity(
            pos.add(
                coriSize.getHeight() + roomSize.getWidth(),
                coriSize.getHeight() - wallWidth,
                0
            ),
            new NetworkC(false),
            new Size(wallWidth, wallWidth),
            c
        );
        walls.add(corner2);

        //corner connecting top and right
        TexRectEntity corner3 = new TexRectEntity(
            pos.add(
                coriSize.getHeight() - wallWidth,
                coriSize.getHeight() + roomSize.getHeight(),
                0
            ),
            new NetworkC(false),
            new Size(wallWidth, wallWidth),
            c
        );
        walls.add(corner3);

        //corner connecting top and left
        TexRectEntity corner4 = new TexRectEntity(
            pos.add(
                coriSize.getHeight() + roomSize.getWidth(),
                coriSize.getHeight() + roomSize.getHeight(),
                0
            ),
            new NetworkC(false),
            new Size(wallWidth, wallWidth),
            c
        );
        walls.add(corner4);
    }
    
    /***
     * 
     */
    public void init(
        Point3D newPos,
        double wallWidth,
        Size newRoomSize,
        Size coriSize,
        Exits exits,
        Color c
    ) {
        
    }

    /***
     * sets the Z value of all the walls in the room to the newZ
     * @param newZ
     */
    public void setZ(double newZ) {
        for (TexRectEntity e : walls) {
            e.getRendererC().setZ(newZ);
        }
    }
    
    /***
     * Sets new values of the room connections
     * @param north the new value of the north connection
     * @param south the new value of the south connection
     * @param east the new value of the east connection
     * @param west the new value of the west connection
     */
    public void setConnTypes(ConnType north, ConnType south, ConnType east, ConnType west) {
        setNorthConnType(north);
        setSouthConnType(south);
        setEastConnType(east);
        setWestConnType(west);
    }
    
    /***
     * Sets the value of the north connection
     * @param north
     */
    public void setNorthConnType(ConnType north) {
        connections.set(0, new Pair<Room, ConnType>(connections.get(0).getKey(), north));
    }

    /***
     * Sets the value of the south connection
     * @param south
     */
    public void setSouthConnType(ConnType south) {
        connections.set(1, new Pair<Room, ConnType>(connections.get(1).getKey(), south));
    }
    
    /***
     * Sets the value of the east connection
     * @param east
     */
    public void setEastConnType(ConnType east) {
        connections.set(2, new Pair<Room, ConnType>(connections.get(2).getKey(), east));
    }
    
    /***
     * Sets the value of the west connection
     * @param west
     */
    public void setWestConnType(ConnType west) {
        connections.set(3, new Pair<Room, ConnType>(connections.get(3).getKey(), west));
    }
    
    /***
     *
     * @return The walls that make up the room
     */
    public List<TexRectEntity> getWalls() {
        return walls;
    }

    /***
     *
     * @return total size of the room with all the sides
     */
    public Size getTotalSize() {
        return totalSize;
    }

    /***
     *
     * @return size of the internal room
     */
    public Size getRoomSize() {
        return roomSize;
    }

    /***
     * 
     * @return the z value of the room for rendering purposes
     */
    public double getZ() {
        return pos.getZ();
    }

    /***
     *
     * @return the centre of the room for AI purposes
     */
    public Point3D getRoomCenter() {
		return pos.add(
            totalSize.getWidth() / 2,
            totalSize.getHeight() / 2,
            0
        );
    }

}
