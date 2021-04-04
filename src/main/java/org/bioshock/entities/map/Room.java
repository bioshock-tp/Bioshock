package org.bioshock.entities.map;

import static org.bioshock.utils.GlobalConstants.UNIT_HEIGHT;
import static org.bioshock.utils.GlobalConstants.UNIT_WIDTH;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.geometry.Point2D;
import org.bioshock.components.NetworkC;
import org.bioshock.engine.pathfinding.GraphNode;
import org.bioshock.entities.map.utils.ConnType;
import org.bioshock.main.App;
import org.bioshock.utils.ArrayUtils;
import org.bioshock.utils.DeepCopy;
import org.bioshock.utils.Direction;
import org.bioshock.utils.Size;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class Room extends GraphNode {
    /***
     *  Stores the total size of the room
     *  i.e. the roomSize + corridor width*2 in both dimensions 
     *  (in terms of units)
     */
    private Size totalSize;
    /***
     * Stores the internal room size
     * (in terms of units)
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
     * @param wallWidth the width of the walls that make up the room (in terms of units)
     * @param newRoomSize the size of the central room (in terms of units)
     * @param coriSize the corridor size of all exits (in terms of units)
     * @param c the colour of the room
     */
    public Room(
        Point3D newPos,
        double wallWidth,
        Size newRoomSize,
        Size coriSize,
        Color c) {
        super();
        this.pos = newPos;
        this.wallWidth = wallWidth;
        this.roomSize = newRoomSize;
        this.totalSize = new Size(
            roomSize.getWidth() + 2 * coriSize.getHeight(),
            roomSize.getHeight() + 2 * coriSize.getHeight()
        );
        this.coriSize = coriSize;
        this.c = c;
        setLocation(new Point2D(getRoomCenter().getX(),getRoomCenter().getY()));
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
        
        boolean[][] traversable = new boolean[(int) totalSize.getWidth()][(int) totalSize.getHeight()];
        
        ArrayUtils.fill2DArray(traversable, true);
        
        //Add the sides relevant for the connection type in each direction
        ArrayUtils.copyInArray(
            traversable, 
            topSide(pos.add(coriSize.getHeight()*UNIT_WIDTH, 0*UNIT_HEIGHT, 0),
                    wallWidth, coriSize, c, connections.get(Direction.NORTH)),
            (int) 0, 
            (int) coriSize.getHeight()
        );
        
        ArrayUtils.copyInArray(
            traversable,
            botSide(pos.add(
                    coriSize.getHeight()*UNIT_WIDTH,
                    (coriSize.getHeight() + roomSize.getHeight())*UNIT_HEIGHT,
                    0
                ),
                wallWidth, coriSize, c, connections.get(Direction.SOUTH)),
            (int) (coriSize.getHeight() + roomSize.getHeight()),
            (int) coriSize.getHeight()
        );
        
        ArrayUtils.copyInArray(
            traversable,
            rightSide(pos.add(
                    (coriSize.getHeight() + roomSize.getWidth())*UNIT_WIDTH,
                    coriSize.getHeight()*UNIT_HEIGHT,
                    0
                ),
                wallWidth, coriSize, c, connections.get(Direction.EAST)),
            (int) coriSize.getHeight(),
            (int) (coriSize.getHeight() + roomSize.getWidth())
        );
        
        ArrayUtils.copyInArray(
            traversable,
            leftSide(pos.add(0*UNIT_WIDTH, coriSize.getHeight()*UNIT_HEIGHT, 0),
                wallWidth, coriSize, c, connections.get(Direction.WEST)),
            (int) coriSize.getHeight(),
            (int) 0
        );
        
        
        //corner connecting bottom and right
        TexRectEntity corner1 = new TexRectEntity(
            pos.add(
                (coriSize.getHeight() - wallWidth)*UNIT_WIDTH,
                (coriSize.getHeight() - wallWidth)*UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(wallWidth*UNIT_WIDTH, wallWidth*UNIT_HEIGHT),
            c
        );
        walls.add(corner1);
        ArrayUtils.copyInArray(
            traversable, 
            new boolean[(int) wallWidth][(int) wallWidth], 
            (int)(coriSize.getHeight() - wallWidth), 
            (int)(coriSize.getHeight() - wallWidth)
        );

        //corner connecting bottom and left
        TexRectEntity corner2 = new TexRectEntity(
            pos.add(
                (coriSize.getHeight() + roomSize.getWidth())*UNIT_WIDTH,
                (coriSize.getHeight() - wallWidth)*UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(wallWidth*UNIT_WIDTH, wallWidth*UNIT_HEIGHT),
            c
        );
        walls.add(corner2);
        ArrayUtils.copyInArray(
            traversable, 
            new boolean[(int) wallWidth][(int) wallWidth], 
            (int)(coriSize.getHeight() + roomSize.getWidth()), 
            (int)(coriSize.getHeight() - wallWidth)
        );

        //corner connecting top and right
        TexRectEntity corner3 = new TexRectEntity(
            pos.add(
                (coriSize.getHeight() - wallWidth)*UNIT_WIDTH,
                (coriSize.getHeight() + roomSize.getHeight())*UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(wallWidth*UNIT_WIDTH, wallWidth*UNIT_HEIGHT),
            c
        );
        walls.add(corner3);
        ArrayUtils.copyInArray(
            traversable, 
            new boolean[(int) wallWidth][(int) wallWidth], 
            (int)(coriSize.getHeight() - wallWidth), 
            (int)(coriSize.getHeight() + roomSize.getHeight())
        );

        //corner connecting top and left
        TexRectEntity corner4 = new TexRectEntity(
            pos.add(
                (coriSize.getHeight() + roomSize.getWidth())*UNIT_WIDTH,
                (coriSize.getHeight() + roomSize.getHeight())*UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(wallWidth*UNIT_WIDTH, wallWidth*UNIT_HEIGHT),
            c
        );
        walls.add(corner4);
        ArrayUtils.copyInArray(
            traversable, 
            new boolean[(int) wallWidth][(int) wallWidth], 
            (int)(coriSize.getHeight() + roomSize.getWidth()), 
            (int)(coriSize.getHeight() + roomSize.getHeight())
        );
        
        App.logger.debug("Full Room:");
        ArrayUtils.log2DArray(traversable);
    }
    
    
    
    /***
     * Adds a top side depending on the connection type
     * @param wallWidth
     * @param coriSize
     * @param c
     * @param con
     */
    private boolean[][] topSide(
        Point3D pos,
        double wallWidth,
        Size coriSize,
        Color c,
        ConnType con) {
        Pair<List<TexRectEntity>,boolean[][]> wallsAndArray = null;
        //chooses either a top side with or without an exit
        switch(con) {
        case NO_EXIT:
            wallsAndArray = Sides.tNoExit(
                pos,
                wallWidth,
                roomSize.getWidth(),
                coriSize.getWidth(),
                coriSize.getHeight(),
                c
            );
            break;
        case ROOM_TO_ROOM:
            wallsAndArray = Sides.tExit(
                pos,
                wallWidth,
                roomSize.getWidth(),
                coriSize.getWidth(),
                coriSize.getHeight(),
                c
            );
            break;
        default:
            break;
        }
        
        walls.addAll(wallsAndArray.getKey());
        return wallsAndArray.getValue();
    }
    
    /***
     * Adds a bottom side depending on the connection type
     * @param wallWidth
     * @param coriSize
     * @param c
     * @param con
     */
    private boolean[][] botSide(
        Point3D pos,
        double wallWidth,
        Size coriSize,
        Color c,
        ConnType con) {
        Pair<List<TexRectEntity>,boolean[][]> wallsAndArray = null;
      //chooses either a bottom side with or without an exit
        switch(con) {
        case ROOM_TO_ROOM: 
            wallsAndArray = Sides.bExit(
                pos,
                wallWidth,
                roomSize.getWidth(),
                coriSize.getWidth(),
                coriSize.getHeight(),
                c
            );
            break;
        case NO_EXIT:
            wallsAndArray = Sides.bNoExit(
                pos,
                wallWidth,
                roomSize.getWidth(),
                coriSize.getWidth(),
                coriSize.getHeight(),
                c
            );
            break;
        default:
            break;
        }
        walls.addAll(wallsAndArray.getKey());
        return wallsAndArray.getValue();
    }
    
    /***
     * Adds a left side depending on the connection type
     * @param wallWidth
     * @param coriSize
     * @param c
     * @param con
     */
    private boolean[][] rightSide(
        Point3D pos,
        double wallWidth,
        Size coriSize,
        Color c,
        ConnType con) {
        Pair<List<TexRectEntity>,boolean[][]> wallsAndArray = null;
        //chooses either a right side with or without an exit
        switch(con) {
        case NO_EXIT:
            wallsAndArray = Sides.rNoExit(
                pos,
                wallWidth,
                roomSize.getHeight(),
                coriSize.getWidth(),
                coriSize.getHeight(),
                c
            );
            break;
        case ROOM_TO_ROOM:
            wallsAndArray = Sides.rExit(
                pos,
                wallWidth,
                roomSize.getHeight(),
                coriSize.getWidth(),
                coriSize.getHeight(),
                c
            );
            break;
        default:
            break;
        }
        walls.addAll(wallsAndArray.getKey());
        return wallsAndArray.getValue();
    }

    /***
     * Adds a left side depending on the connection type
     * @param wallWidth
     * @param coriSize
     * @param c
     * @param con
     */
    private boolean[][] leftSide(
        Point3D pos,
        double wallWidth,
        Size coriSize,
        Color c,
        ConnType con) {
        Pair<List<TexRectEntity>,boolean[][]> wallsAndArray = null;
        //chooses either a left side with or without an exit
        switch(con) {
        case NO_EXIT:
            wallsAndArray = Sides.lNoExit(
                pos,
                wallWidth,
                roomSize.getHeight(),
                coriSize.getWidth(),
                coriSize.getHeight(),
                c
            );            
            break;
        case ROOM_TO_ROOM:
            wallsAndArray = Sides.lExit(
                pos,
                wallWidth, 
                roomSize.getHeight(),
                coriSize.getWidth(),
                coriSize.getHeight(),
                c
            );
            break;
        default:
            break;          
        }
        walls.addAll(wallsAndArray.getKey());
        return wallsAndArray.getValue();
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
        return new Size(
            totalSize.getWidth()*UNIT_WIDTH,
            totalSize.getHeight()*UNIT_HEIGHT
        );
    }

    /***
     *
     * @return size of the internal room
     */
    public Size getRoomSize() {
        return new Size(
            roomSize.getWidth()*UNIT_WIDTH,
            roomSize.getHeight()*UNIT_HEIGHT
        );
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
            (totalSize.getWidth() / 2)*UNIT_WIDTH,
            (totalSize.getHeight() / 2)*UNIT_HEIGHT,
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
