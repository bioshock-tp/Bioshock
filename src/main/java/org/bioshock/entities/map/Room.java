package org.bioshock.entities.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bioshock.components.NetworkC;
import org.bioshock.utils.DeepCopy;
import org.bioshock.utils.Direction;
import org.bioshock.utils.Size;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class Room{
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
    private double wallWidth;
    private Size coriSize;
    private Color c;

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
        Color c) {
        this.pos = newPos;
        this.wallWidth = wallWidth;
        this.roomSize = newRoomSize;
        this.totalSize = new Size(
            roomSize.getWidth() + 2 * coriSize.getHeight(),
            roomSize.getHeight() + 2 * coriSize.getHeight()
        );
        this.coriSize = coriSize;
        this.c = c;
    }
    
    /***
     * 
     */
    public void init(
        List<Pair<Direction,ConnType>> edges
    ) {
        
        /***
         * Stores the connection type for each direction
         */
        HashMap<Direction,ConnType> connections = new HashMap<>();
        connections.put(Direction.NORTH, ConnType.NO_EXIT);
        connections.put(Direction.SOUTH, ConnType.NO_EXIT);
        connections.put(Direction.EAST, ConnType.NO_EXIT);
        connections.put(Direction.WEST, ConnType.NO_EXIT);
        
        //Add the connection type for each direction
        for (Pair<Direction,ConnType> edge : edges) {
            connections.replace(edge.getKey(), edge.getValue());
        }
        
        //Add the sides relevant for the connection type in each direction
        topSide(wallWidth, coriSize, c, connections.get(Direction.NORTH));
        botSide(wallWidth, coriSize, c, connections.get(Direction.SOUTH));
        rightSide(wallWidth, coriSize, c, connections.get(Direction.EAST));
        leftSide(wallWidth, coriSize, c, connections.get(Direction.WEST));
        
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
     * Adds a top side depending on the connection type
     * @param wallWidth
     * @param coriSize
     * @param c
     * @param con
     */
    private void topSide(
        double wallWidth,
        Size coriSize,
        Color c,
        ConnType con) {
        //chooses either a top side with or without an exit
        switch(con) {
        case NO_EXIT:
            walls.addAll(Sides.tNoExit(
                pos.add(coriSize.getHeight(), 0, 0),
                wallWidth,
                roomSize.getWidth(),
                coriSize.getWidth(),
                coriSize.getHeight(),
                c
            ));
            break;
        case ROOM_TO_ROOM:
            walls.addAll(Sides.tExit(
                pos.add(coriSize.getHeight(), 0, 0),
                wallWidth,
                roomSize.getWidth(),
                coriSize.getWidth(),
                coriSize.getHeight(),
                c
            ));
            break;
        default:
            break;
        }
    }
    
    /***
     * Adds a bottom side depending on the connection type
     * @param wallWidth
     * @param coriSize
     * @param c
     * @param con
     */
    private void botSide(
        double wallWidth,
        Size coriSize,
        Color c,
        ConnType con) {
      //chooses either a bottom side with or without an exit
        switch(con) {
        case ROOM_TO_ROOM: 
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
            break;
        case NO_EXIT:
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
            break;
        default:
            break;
        }
    }
    
    /***
     * Adds a left side depending on the connection type
     * @param wallWidth
     * @param coriSize
     * @param c
     * @param con
     */
    private void rightSide(
        double wallWidth,
        Size coriSize,
        Color c,
        ConnType con) {
        //chooses either a right side with or without an exit
        switch(con) {
        case NO_EXIT:
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
            break;
        case ROOM_TO_ROOM:
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
            break;
        default:
            break;
        }
    }

    /***
     * Adds a left side depending on the connection type
     * @param wallWidth
     * @param coriSize
     * @param c
     * @param con
     */
    private void leftSide(
        double wallWidth,
        Size coriSize,
        Color c,
        ConnType con) {
        //chooses either a left side with or without an exit
        switch(con) {
        case NO_EXIT:
            walls.addAll(Sides.lNoExit(
                pos.add(0, coriSize.getHeight(), 0),
                wallWidth,
                roomSize.getHeight(),
                coriSize.getWidth(),
                coriSize.getHeight(),
                c
            ));            
            break;
        case ROOM_TO_ROOM:
            walls.addAll(Sides.lExit(
                pos.add(0, coriSize.getHeight(), 0),
                wallWidth, roomSize.getHeight(),
                coriSize.getWidth(),
                coriSize.getHeight(),
                c
            ));
            break;
        default:
            break;
          
        }
    }    
    
    @SuppressWarnings("unused")
	private boolean isSolid(ConnType con) {
        return true;
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

    /***
     * 
     * @return an uninitialised deep copy of the current room
     * i.e. you need to call init() on the room with edge info
     */
    public Room deepCopy() {
        return new Room(
            this.pos,
            this.wallWidth, 
            this.roomSize,  
            this.coriSize, 
            this.c
        );
}

}
