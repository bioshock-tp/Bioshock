package org.bioshock.entities.map;

import static org.bioshock.utils.GlobalConstants.UNIT_HEIGHT;
import static org.bioshock.utils.GlobalConstants.UNIT_WIDTH;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bioshock.components.NetworkC;
import org.bioshock.engine.pathfinding.Graph;
import org.bioshock.engine.pathfinding.GraphNode;
import org.bioshock.entities.map.utils.ConnType;
import org.bioshock.entities.map.utils.RoomType;
import org.bioshock.main.App;
import org.bioshock.utils.ArrayUtils;
import org.bioshock.utils.Direction;
import org.bioshock.utils.Size;

import javafx.geometry.Point2D;
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

    private List<Pair<Point2D, Direction>> corridorPoints = new ArrayList<>();
    /***
     * Stores the position of the top left of the room
     */
    private Point3D pos;
    private double wallWidth;
    private Size coriSize;
    private Color c;
    private double wallProb = 0.25; 
    private Graph<GraphNode, Pair<Direction,Double>> traversableGraph;
    private GraphNode centreNode;
    
    private RoomType roomType = RoomType.SINGLE_ROOM;
    private List<Room> openlyConnectedRooms = new ArrayList<>();

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
    
    public void init(
        List<Pair<Direction,ConnType>> edges,
        Double wallProbObj,
        Long seed
    ) {        
        //if the room Prob is not null use the given value
        if(wallProbObj != null) {
            wallProb = wallProbObj;
        }
        
        boolean[][] locationsToSpawn = 
                new boolean[(int) totalSize.getHeight()][(int) totalSize.getWidth()];
         
        //if a seed is given use a seeded random number generator
        //otherwise use a non seeded one
        Random rand;
        if(seed == null) {
            rand = new Random();
        }
        else {
            rand = new Random(seed);
        }
        
        //generate a random array of RoomTypes 
        for(int i=0;i<locationsToSpawn.length;i++) {
            for(int j=0;j<locationsToSpawn[0].length;j++) {
                if(rand.nextDouble()<wallProb) {
                    locationsToSpawn[i][j] = true;
                }
                else {
                    locationsToSpawn[i][j] = false;
                }
            }
        }
        
        init(edges, locationsToSpawn);
    }
    
    /***
     * 
     */
    public void init(
        List<Pair<Direction,ConnType>> edges,
        boolean[][] locationsToSpawn
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
        
        //make an array filled with true that represents which positions you can traverse through in the room
        boolean[][] traversable = new boolean[(int) totalSize.getHeight()][(int) totalSize.getWidth()];        
        ArrayUtils.fill2DArray(traversable, true);
        
        //Add the sides relevant for the connection type in the north direction
        ArrayUtils.copyInArray(
            traversable, 
            topSide(pos.add(coriSize.getHeight()*UNIT_WIDTH, 0*UNIT_HEIGHT, 0),
                    wallWidth, coriSize, c, connections.get(Direction.NORTH)),
            (int) 0, 
            (int) coriSize.getHeight()
        );
        
        //then check if it is a subroom connection and if it is you need to add sides to connect 
        //to the other subroom that makes up the bigger room
        if(connections.get(Direction.NORTH) == ConnType.SUB_ROOM &&
                connections.get(Direction.EAST) != ConnType.SUB_ROOM) {
            Pair<TexRectEntity, boolean[][]> sideAndArray = Sides.side(
                pos.add((coriSize.getHeight() + roomSize.getWidth())*UNIT_WIDTH, 0*UNIT_HEIGHT, 0), 
                new Size(wallWidth, coriSize.getHeight()), 
                c);
            
            walls.add(sideAndArray.getKey());
            ArrayUtils.copyInArray(
                traversable, 
                sideAndArray.getValue(),
                0, 
                (int) (coriSize.getHeight() + roomSize.getWidth()));
        }
        if(connections.get(Direction.NORTH) == ConnType.SUB_ROOM &&
                connections.get(Direction.WEST) != ConnType.SUB_ROOM) {
            Pair<TexRectEntity, boolean[][]> sideAndArray = Sides.side(
                pos.add((coriSize.getHeight()-wallWidth)*UNIT_WIDTH, 0*UNIT_HEIGHT, 0), 
                new Size(wallWidth, coriSize.getHeight()), 
                c);
            
            walls.add(sideAndArray.getKey());
            ArrayUtils.copyInArray(
                traversable, 
                sideAndArray.getValue(),
                0, 
                (int) (coriSize.getHeight()-wallWidth));
        }
        
        //Add the sides relevant for the connection type in the south direction
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
        
        //then check if it is a subroom connection and if it is you need to add sides to connect 
        //to the other subroom that makes up the bigger room
        if(connections.get(Direction.SOUTH) == ConnType.SUB_ROOM &&
                connections.get(Direction.EAST) != ConnType.SUB_ROOM) {
            Pair<TexRectEntity, boolean[][]> sideAndArray = Sides.side(
                pos.add((coriSize.getHeight() + roomSize.getWidth())*UNIT_WIDTH, (coriSize.getHeight() + roomSize.getHeight())*UNIT_HEIGHT, 0), 
                new Size(wallWidth, coriSize.getHeight()), 
                c);
            
            walls.add(sideAndArray.getKey());
            ArrayUtils.copyInArray(
                traversable, 
                sideAndArray.getValue(),
                (int) (coriSize.getHeight() + roomSize.getHeight()), 
                (int) (coriSize.getHeight() + roomSize.getWidth()));
        }
        if(connections.get(Direction.SOUTH) == ConnType.SUB_ROOM &&
                connections.get(Direction.WEST) != ConnType.SUB_ROOM) {
            Pair<TexRectEntity, boolean[][]> sideAndArray = Sides.side(
                pos.add((coriSize.getHeight()-wallWidth)*UNIT_WIDTH, (coriSize.getHeight() + roomSize.getHeight())*UNIT_HEIGHT, 0), 
                new Size(wallWidth, coriSize.getHeight()), 
                c);
            
            walls.add(sideAndArray.getKey());
            ArrayUtils.copyInArray(
                traversable, 
                sideAndArray.getValue(),
                (int) (coriSize.getHeight() + roomSize.getHeight()), 
                (int) (coriSize.getHeight()-wallWidth));
        }
        
        //Add the sides relevant for the connection type in the east direction
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
        
        //then check if it is a subroom connection and if it is you need to add sides to connect 
        //to the other subroom that makes up the bigger room
        if(connections.get(Direction.EAST) == ConnType.SUB_ROOM &&
                connections.get(Direction.NORTH) != ConnType.SUB_ROOM) {
            Pair<TexRectEntity, boolean[][]> sideAndArray = Sides.side(
                pos.add((coriSize.getHeight() + roomSize.getWidth())*UNIT_WIDTH, (coriSize.getHeight() - wallWidth)*UNIT_HEIGHT, 0), 
                new Size(coriSize.getHeight(), wallWidth), 
                c);
            
            walls.add(sideAndArray.getKey());
            ArrayUtils.copyInArray(
                traversable, 
                sideAndArray.getValue(),
                (int) (coriSize.getHeight() - wallWidth), 
                (int) (coriSize.getHeight() + roomSize.getWidth()));
        }
        if(connections.get(Direction.EAST) == ConnType.SUB_ROOM &&
                connections.get(Direction.SOUTH) != ConnType.SUB_ROOM) {
            Pair<TexRectEntity, boolean[][]> sideAndArray = Sides.side(
                pos.add((coriSize.getHeight() + roomSize.getWidth())*UNIT_WIDTH, (coriSize.getHeight() + roomSize.getHeight())*UNIT_HEIGHT, 0), 
                new Size(coriSize.getHeight(), wallWidth), 
                c);
            
            walls.add(sideAndArray.getKey());
            ArrayUtils.copyInArray(
                traversable, 
                sideAndArray.getValue(),
                (int) (coriSize.getHeight() + roomSize.getHeight()), 
                (int) (coriSize.getHeight() + roomSize.getWidth()));
        }
        
        //Add the sides relevant for the connection type in the west direction
        ArrayUtils.copyInArray(
            traversable,
            leftSide(pos.add(0*UNIT_WIDTH, coriSize.getHeight()*UNIT_HEIGHT, 0),
                wallWidth, coriSize, c, connections.get(Direction.WEST)),
            (int) coriSize.getHeight(),
            (int) 0
        );
        
        //then check if it is a subroom connection and if it is you need to add sides to connect 
        //to the other subroom that makes up the bigger room
        if(connections.get(Direction.WEST) == ConnType.SUB_ROOM &&
                connections.get(Direction.NORTH) != ConnType.SUB_ROOM) {
            Pair<TexRectEntity, boolean[][]> sideAndArray = Sides.side(
                pos.add(0*UNIT_WIDTH, (coriSize.getHeight()-wallWidth)*UNIT_HEIGHT, 0), 
                new Size(coriSize.getHeight(), wallWidth), 
                c);
            
            walls.add(sideAndArray.getKey());
            ArrayUtils.copyInArray(
                traversable, 
                sideAndArray.getValue(),
                (int) (coriSize.getHeight()-wallWidth), 
                (int) 0);
        }
        if(connections.get(Direction.WEST) == ConnType.SUB_ROOM &&
                connections.get(Direction.SOUTH) != ConnType.SUB_ROOM) {
            Pair<TexRectEntity, boolean[][]> sideAndArray = Sides.side(
                pos.add((0)*UNIT_WIDTH, (coriSize.getHeight() + roomSize.getHeight())*UNIT_HEIGHT, 0), 
                new Size(coriSize.getHeight(), wallWidth), 
                c);
            
            walls.add(sideAndArray.getKey());
            ArrayUtils.copyInArray(
                traversable, 
                sideAndArray.getValue(),
                (int) (coriSize.getHeight() + roomSize.getHeight()), 
                (int) (0));
        }
        
        //if none of the sides are a SUB_ROOM connection add the corner in the top left of the room
        if(connections.get(Direction.WEST) != ConnType.SUB_ROOM && 
                connections.get(Direction.NORTH) != ConnType.SUB_ROOM) {
            //corner connecting bottom and right
            corner(
        		traversable, 
        		(int)(coriSize.getHeight() - wallWidth), 
                (int)(coriSize.getHeight() - wallWidth));
        }

        //if none of the sides are a SUB_ROOM connection add the corner in the top right of the room
        if(connections.get(Direction.EAST) != ConnType.SUB_ROOM && 
                connections.get(Direction.NORTH) != ConnType.SUB_ROOM) {
            //corner connecting bottom and left
            corner(
        		traversable, 
        		(int)(coriSize.getHeight() + roomSize.getWidth()), 
                (int)(coriSize.getHeight() - wallWidth));
        }

        //if none of the sides are a SUB_ROOM connection add the corner in the bottom left of the room
        if(connections.get(Direction.WEST) != ConnType.SUB_ROOM && 
                connections.get(Direction.SOUTH) != ConnType.SUB_ROOM) {
            //corner connecting top and right
            corner(
        		traversable,
        		(int)(coriSize.getHeight() - wallWidth),
        		(int)(coriSize.getHeight() + roomSize.getHeight()));
        }
       
        //if none of the sides are a SUB_ROOM connection add the corner in the bottom right of the room
        if(connections.get(Direction.EAST) != ConnType.SUB_ROOM &&
                connections.get(Direction.SOUTH) != ConnType.SUB_ROOM) {
            //corner connecting top and left
            corner(
        		traversable, 
        		(int)(coriSize.getHeight() + roomSize.getWidth()), 
        		(int)(coriSize.getHeight() + roomSize.getHeight()));
        }
        
        //generate a Graph node at every traversable position
        GraphNode[][] traversableNodes = new GraphNode[traversable.length][traversable[0].length];
        for (int i=0;i<traversableNodes.length;i++) {
        	for (int j=0;j<traversableNodes[0].length;j++) {
        		if(traversable[i][j]) {
	        		traversableNodes[i][j] = new GraphNode(new Point2D(
	    				pos.getX() + j*UNIT_WIDTH + UNIT_WIDTH/2, 
	    				pos.getY() + i*UNIT_HEIGHT + UNIT_HEIGHT/2));
        		}
        	}
        }
        //get the node that represents the centre of the room
        centreNode = traversableNodes[traversableNodes.length/2][traversableNodes[0].length/2];
        
        //get a graph representing the positions reachable from the centre of the room
        traversableGraph = (new Graph<>(traversableNodes, new TraversableEdgeGenerator()))
    		.getConnectedSubgraph(centreNode);
        
        //generate an array representing which locations you can spawn walls into the room
        //this is effectively the traversable array but it stops things spawning in a line between room centres
        boolean[][] spawnableLocations = traversable.clone();
        ArrayUtils.copyInArray(
            spawnableLocations, 
            new boolean[1][spawnableLocations[0].length], 
            spawnableLocations.length/2, 
            0
        );        
        ArrayUtils.copyInArray(
            spawnableLocations, 
            new boolean[spawnableLocations.length][1], 
            0, 
            spawnableLocations[0].length/2
        );        
//        ArrayUtils.log2DArray(spawnableLocations);
        
        //iterate through locations to spawn and then spawn walls in in the location if:
        //it want's to be spawned there, 
        //it can be spawned there,
        //the position it would spawn in is reachable from the centre position
        for (int i=0;i<locationsToSpawn.length&&i<spawnableLocations.length;i++) {
            for (int j=0;j<locationsToSpawn[0].length&&j<spawnableLocations[0].length;j++) {
                if(locationsToSpawn[i][j] == true && spawnableLocations[i][j] == true
                        && traversableGraph.getNodes().contains(traversableNodes[i][j])) {
                    walls.add(new TexRectEntity(
                        pos.add(j*UNIT_WIDTH, i*UNIT_HEIGHT,0), 
                        new NetworkC(false), 
                        new Size(UNIT_WIDTH, UNIT_HEIGHT), 
                        c.invert()));
                    traversableNodes[i][j] = null;
                }
            }
        }
        
        //get a graph representing the positions reachable from the centre of the room again now walls have been spawned into the room
        traversableGraph = (new Graph<>(traversableNodes, new TraversableEdgeGenerator()))
                .getConnectedSubgraph(centreNode);
    }
    
    public Graph<GraphNode, Pair<Direction, Double>> getTraversableGraph() {
		return traversableGraph;
	}

	/***
     * add a corner at the given position and put it in traversable
     * @param traversable
     * @param relX
     * @param relY
     */
    private void corner(
		boolean[][] traversable,
		int relX,
		int relY) {
        //add the entity representing the corner
    	TexRectEntity corner4 = new TexRectEntity(
            pos.add(
                relX*UNIT_WIDTH,
                relY*UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(wallWidth*UNIT_WIDTH, wallWidth*UNIT_HEIGHT),
            c
        );
        walls.add(corner4);
        //fill the array making the corner not traversable
        ArrayUtils.copyInArray(
            traversable, 
            new boolean[(int) wallWidth][(int) wallWidth], 
            (int)relY, 
            (int)relX
        );
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
            corridorPoints.add(new Pair<>(
                    new Point2D(
                            getLocation().getX(),
                            getLocation().getY()- roomSize.getHeight()/2*UNIT_HEIGHT),
                    Direction.NORTH));
            break;
        case SUB_ROOM:
            //if it is a sub_room connection there should be no side so have a placeholder array so
            //you don't get null pointers
            wallsAndArray = new Pair<>(null, ArrayUtils.fill2DArray(new boolean[1][1],true));
            break;
        default:
            break;
        }
        
        if(wallsAndArray.getKey() != null) {
            walls.addAll(wallsAndArray.getKey());            
        }
        
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
            corridorPoints.add(new Pair<>(
                    new Point2D(
                            getLocation().getX(),
                            getLocation().getY() + roomSize.getHeight()/2),
                    Direction.SOUTH));
            break;
        case SUB_ROOM:
            //if it is a sub_room connection there should be no side so have a placeholder array so
            //you don't get null pointers
            wallsAndArray = new Pair<>(null, ArrayUtils.fill2DArray(new boolean[1][1],true));
            break;
        default:
            break;
        }
        
        if(wallsAndArray.getKey() != null) {
            walls.addAll(wallsAndArray.getKey());            
        }
        
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
            corridorPoints.add(new Pair<>(
                    new Point2D(
                            getLocation().getX() + roomSize.getWidth()/2,
                            getLocation().getY()),
                    Direction.EAST));
            break;
        case SUB_ROOM:
            //if it is a sub_room connection there should be no side so have a placeholder array so
            //you don't get null pointers
            wallsAndArray = new Pair<>(null, ArrayUtils.fill2DArray(new boolean[1][1],true));
            break;
        default:
            break;
        }
        
        if(wallsAndArray.getKey() != null) {
            walls.addAll(wallsAndArray.getKey());            
        }
        
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
            corridorPoints.add(new Pair<>(
                    new Point2D(
                            getLocation().getX() - roomSize.getWidth()/2,
                            getLocation().getY()),
                    Direction.WEST));
            break;
        case SUB_ROOM:
            //if it is a sub_room connection there should be no side so have a placeholder array so
            //you don't get null pointers
            wallsAndArray = new Pair<>(null, ArrayUtils.fill2DArray(new boolean[1][1],true));
            break;
        default:
            break;
        }
        
        if(wallsAndArray.getKey() != null) {
            walls.addAll(wallsAndArray.getKey());            
        }
        
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

    public List<Pair<Point2D, Direction>> getCorridorPoints(){
        return corridorPoints;
    }
    
    public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
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
    
    /***
     * 
     * @return a list of rooms that the current room is openly connected to
     * i.e. adjacent rooms that are part of the same big room
     */
	public List<Room> getOpenlyConnectedRooms() {
		return openlyConnectedRooms;
	}

}
