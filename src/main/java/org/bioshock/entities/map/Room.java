package org.bioshock.entities.map;

import static org.bioshock.utils.GlobalConstants.UNIT_HEIGHT;
import static org.bioshock.utils.GlobalConstants.UNIT_WIDTH;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bioshock.components.NetworkC;
import org.bioshock.engine.pathfinding.Graph;
import org.bioshock.engine.pathfinding.GraphNode;
import org.bioshock.entities.map.utils.ConnType;
import org.bioshock.entities.map.utils.RoomType;
import org.bioshock.utils.ArrayUtils;
import org.bioshock.utils.Direction;
import org.bioshock.utils.GlobalConstants;
import org.bioshock.utils.Size;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class Room extends GraphNode {
    /***
     *  Stores the total size of the room
     *  i.e. the roomSize + corridor width * 2 in both dimensions
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
    private List<Wall> walls = new ArrayList<>();

    /**
     * A list of points at each of the exits in the room, used for seeker pathfinding calculations
     */
    private List<Pair<Point2D, Direction>> corridorPoints = new ArrayList<>();
    /***
     * Stores the position of the top left of the room
     */
    private Point3D pos;
    /**
     * the wall width (in terms of units)
     */
    private int wallWidth;
    /**
     * the corridor size of all exits (in terms of units)
     */
    private Size coriSize;
    /**
     * the colour of the walls in the room
     */
    private Color colour;
    /**
     * the probability of a wall being spawned at any given location
     */
    private double wallProb = 0.25;
    /**
     * a graph representing which positions are traversable in the room
     */
    private Graph<GraphNode, Pair<Direction, Double>> traversableGraph;
    /**
     * the node representing the centre of the room in the traversable graph and array
     */
    private GraphNode centreNode;
    /**
     * an array representing which positions are traversable in the room
     */
    GraphNode[][] traversableArray;
    /**
     * Stores the Rooms type (currently not useful as every room will be a SINGLE_ROOM
     * But useful for expandability purposes
     */
    private RoomType roomType = RoomType.SINGLE_ROOM;
    /**
     * a list of rooms that it is openly connected to
     * i.e. all adjacent rooms which are part of the same big room
     */
    private List<Room> openlyConnectedRooms = new ArrayList<>();
    /**
     * Stores the connection type for each direction
     */
    EnumMap<Direction, ConnType> connections = new EnumMap<>(Direction.class);
    /**
     * an array representing where there is floor space
     * and where floor should be rendered
     */
    boolean[][] floorSpace;
    /**
     * The random number generator used to generate the room
     */
    private Random rand = new Random();

    /***
     * Generates a room with the position being the top left of the room
     *
     * This is the first step in room generation so as to allow to have the room object for
     * referencing purposes
     *
     * @param newPos the position of the top left of the room
     * @param wallWidth the width of the walls that make up the room (in terms
     * of units)
     * @param newRoomSize the size of the central room (in terms of units)
     * @param coriSize the corridor size of all exits (in terms of units)
     * @param c the colour of the room
     */
    public Room(
        Point3D newPos,
        int wallWidth,
        Size newRoomSize,
        Size coriSize,
        Color c
    ) {
        //Initialise members
        this.pos = newPos;
        this.wallWidth = wallWidth;
        this.roomSize = newRoomSize;
        this.totalSize = new Size(
            roomSize.getWidth() + 2 * coriSize.getHeight(),
            roomSize.getHeight() + 2 * coriSize.getHeight()
        );
        this.coriSize = coriSize;
        this.colour = c;

        //initialise with every connection being NO_EXIT
        connections.put(Direction.NORTH, ConnType.NO_EXIT);
        connections.put(Direction.SOUTH, ConnType.NO_EXIT);
        connections.put(Direction.EAST, ConnType.NO_EXIT);
        connections.put(Direction.WEST, ConnType.NO_EXIT);

        //Set its GraphNode location
        setLocation(new Point2D(
            getRoomCenter().getX(),
            getRoomCenter().getY()
        ));
    }

    /**
     * The second stage in generating a room and spawns objects randomly in the room
     * based off the seed making sure there's a clear path between the centre of each room
     * @param edges A list of edges leaving the room you are generating which represent what
     * the type of sides there are in each direction
     * @param wallProbObj The probability between 0 and 1 of an object being spawned in a arbitrary position
     * @param seed The seed of the random generator used to generate where objects spawn in the room
     */
    public void init(
        List<Pair<Direction, ConnType>> edges,
        Double wallProbObj,
        Long seed
    ) {
        // if the roomProb is not null use the given value
        if (wallProbObj != null) {
            wallProb = wallProbObj;
        }

        boolean[][] locationsToSpawn = new boolean
            [(int) totalSize.getHeight()]
            [(int) totalSize.getWidth()];

        /*
         * if a seed is given use a seeded random number generator
         * otherwise use a non seeded one
         */

        if (seed != null) {
            rand = new Random(seed);
        }

        // generate a random array of RoomTypes
        for (int i = 0; i < locationsToSpawn.length; i++) {
            for (int j = 0; j < locationsToSpawn[0].length; j++) {
                locationsToSpawn[i][j] = rand.nextDouble() < wallProb;
            }
        }

        init(edges, locationsToSpawn);
    }

    /**
     * The second stage in generating a room and spawns objects in the room based off the
     * locationsToSpawn array making sure there's a clear path between the centre of each room
     * @param edges
     * @param locationsToSpawn
     */
    private void init(
        List<Pair<Direction, ConnType>> edges,
        boolean[][] locationsToSpawn
    ) {
        //Add the connection type for each direction
        for (Pair<Direction, ConnType> edge : edges) {
            connections.replace(edge.getKey(), edge.getValue());
        }

        //make an array filled with true that represents which positions you can traverse through in the room
        boolean[][] traversable = new boolean
            [(int) totalSize.getHeight()]
            [(int) totalSize.getWidth()];

        ArrayUtils.fill2DArray(traversable, true);

        //Add the sides relevant for the connection type in each direction
        ArrayUtils.copyInArray(
            traversable,
            topSide(
                pos.add(coriSize.getHeight() * UNIT_WIDTH, 0, 0),
                wallWidth, coriSize, colour, connections.get(Direction.NORTH)
            ),
            0,
            (int) coriSize.getHeight()
        );

        /*
         * then check if it is a sub-room connection and if it is you need to add
         * sides to connect to the other sub-room that makes up the bigger room
         */
        if (
            connections.get(Direction.NORTH) == ConnType.SUB_ROOM
            && connections.get(Direction.EAST) != ConnType.SUB_ROOM
        ) {
            Pair<Wall, boolean[][]> sideAndArray = Sides.side(
                pos.add(
                    (coriSize.getHeight() + roomSize.getWidth()) * UNIT_WIDTH,
                    0,
                    0
                ),
                new Size(wallWidth, coriSize.getHeight()),
                colour,
                GlobalConstants.VERT_WALL_IMAGE,
                wallWidth
            );

            walls.add(sideAndArray.getKey());
            ArrayUtils.copyInArray(
                traversable,
                sideAndArray.getValue(),
                0,
                (int) (coriSize.getHeight() + roomSize.getWidth())
            );
        }

        if (
            connections.get(Direction.NORTH) == ConnType.SUB_ROOM
            && connections.get(Direction.WEST) != ConnType.SUB_ROOM
        ) {
            Pair<Wall, boolean[][]> sideAndArray = Sides.side(
                pos.add((coriSize.getHeight() - wallWidth) * UNIT_WIDTH, 0, 0),
                new Size(wallWidth, coriSize.getHeight()),
                colour,
                GlobalConstants.VERT_WALL_IMAGE,
                wallWidth
            );

            walls.add(sideAndArray.getKey());
            ArrayUtils.copyInArray(
                traversable,
                sideAndArray.getValue(),
                0,
                (int) (coriSize.getHeight() - wallWidth)
            );
        }

        // Add the sides relevant for the connection type in the south direction
        ArrayUtils.copyInArray(
            traversable,
            botSide(
                pos.add(
                    coriSize.getHeight() * UNIT_WIDTH,
                    (coriSize.getHeight() + roomSize.getHeight()) * UNIT_HEIGHT,
                    0
                ),
                wallWidth,
                coriSize,
                colour,
                connections.get(Direction.SOUTH)
            ),
            (int) (coriSize.getHeight() + roomSize.getHeight()),
            (int) coriSize.getHeight()
        );

        /*
         * then check if it is a sub-room connection and if it is add sides to
         * connect to the other sub-room that makes up the bigger room
         */
        if(
            connections.get(Direction.SOUTH) == ConnType.SUB_ROOM
            && connections.get(Direction.EAST) != ConnType.SUB_ROOM
        ) {
            Pair<Wall, boolean[][]> sideAndArray = Sides.side(
                pos.add(
                    (coriSize.getHeight() + roomSize.getWidth()) * UNIT_WIDTH,
                    (coriSize.getHeight() + roomSize.getHeight()) * UNIT_HEIGHT,
                    0
                ),
                new Size(wallWidth, coriSize.getHeight()),
                colour,
                GlobalConstants.VERT_WALL_IMAGE,
                wallWidth
            );

            walls.add(sideAndArray.getKey());
            ArrayUtils.copyInArray(
                traversable,
                sideAndArray.getValue(),
                (int) (coriSize.getHeight() + roomSize.getHeight()),
                (int) (coriSize.getHeight() + roomSize.getWidth())
            );
        }

        if (
            connections.get(Direction.SOUTH) == ConnType.SUB_ROOM
            && connections.get(Direction.WEST) != ConnType.SUB_ROOM
        ) {
            Pair<Wall, boolean[][]> sideAndArray = Sides.side(
                pos.add(
                    (coriSize.getHeight() - wallWidth) * UNIT_WIDTH,
                    (coriSize.getHeight() + roomSize.getHeight()) * UNIT_HEIGHT,
                    0
                ),
                new Size(wallWidth, coriSize.getHeight()),
                colour,
                GlobalConstants.VERT_WALL_IMAGE,
                wallWidth
            );

            walls.add(sideAndArray.getKey());
            ArrayUtils.copyInArray(
                traversable,
                sideAndArray.getValue(),
                (int) (coriSize.getHeight() + roomSize.getHeight()),
                (int) (coriSize.getHeight() - wallWidth)
            );
        }

        // Add the sides relevant for the connection type in the east direction
        ArrayUtils.copyInArray(
            traversable,
            rightSide(
                pos.add(
                    (coriSize.getHeight() + roomSize.getWidth()) * UNIT_WIDTH,
                    coriSize.getHeight() * UNIT_HEIGHT,
                    0
                ),
                wallWidth, coriSize, colour, connections.get(Direction.EAST)),
            (int) coriSize.getHeight(),
            (int) (coriSize.getHeight() + roomSize.getWidth())
        );

        /*
         * then check if it is a sub-room connection and if it is you need to
         * add sides to connect to the other sub-room that makes up the bigger
         * room
         */
        if (
            connections.get(Direction.EAST) == ConnType.SUB_ROOM
            && connections.get(Direction.NORTH) != ConnType.SUB_ROOM
        ) {
            Pair<Wall, boolean[][]> sideAndArray = Sides.side(
                pos.add(
                    (coriSize.getHeight() + roomSize.getWidth()) * UNIT_WIDTH,
                    (coriSize.getHeight() - wallWidth) * UNIT_HEIGHT,
                    0
                ),
                new Size(coriSize.getHeight(), wallWidth),
                colour,
                GlobalConstants.TOP_HORI_WALL_IMAGE,
                wallWidth
            );

            walls.add(sideAndArray.getKey());
            ArrayUtils.copyInArray(
                traversable,
                sideAndArray.getValue(),
                (int) (coriSize.getHeight() - wallWidth),
                (int) (coriSize.getHeight() + roomSize.getWidth())
            );
        }
        if (
            connections.get(Direction.EAST) == ConnType.SUB_ROOM
            && connections.get(Direction.SOUTH) != ConnType.SUB_ROOM
        ) {
            Pair<Wall, boolean[][]> sideAndArray = Sides.side(
                pos.add(
                    (coriSize.getHeight() + roomSize.getWidth()) * UNIT_WIDTH,
                    (coriSize.getHeight() + roomSize.getHeight()) * UNIT_HEIGHT,
                    0
                ),
                new Size(coriSize.getHeight(), wallWidth),
                colour,
                GlobalConstants.BOT_HORI_WALL_IMAGE,
                wallWidth
            );

            walls.add(sideAndArray.getKey());
            ArrayUtils.copyInArray(
                traversable,
                sideAndArray.getValue(),
                (int) (coriSize.getHeight() + roomSize.getHeight()),
                (int) (coriSize.getHeight() + roomSize.getWidth())
            );
        }

        // Add the sides relevant for the connection type in the west direction
        ArrayUtils.copyInArray(
            traversable,
            leftSide(
                pos.add(0, coriSize.getHeight() * UNIT_HEIGHT, 0),
                wallWidth, coriSize, colour, connections.get(Direction.WEST)
            ),
            (int) coriSize.getHeight(),
            0
        );

        /*
         * then check if it is a sub-room connection and if it is you need to
         * add sides to connect to the other sub-room that makes up the bigger
         * room
         */
        if (
            connections.get(Direction.WEST) == ConnType.SUB_ROOM
            && connections.get(Direction.NORTH) != ConnType.SUB_ROOM
        ) {
            Pair<Wall, boolean[][]> sideAndArray = Sides.side(
                pos.add(
                    0,
                    (coriSize.getHeight() - wallWidth) * UNIT_HEIGHT,
                    0
                ),
                new Size(coriSize.getHeight(), wallWidth),
                colour,
                GlobalConstants.TOP_HORI_WALL_IMAGE,
                wallWidth
            );

            walls.add(sideAndArray.getKey());
            ArrayUtils.copyInArray(
                traversable,
                sideAndArray.getValue(),
                (int) (coriSize.getHeight() - wallWidth),
                0
            );
        }
        if (
            connections.get(Direction.WEST) == ConnType.SUB_ROOM
            && connections.get(Direction.SOUTH) != ConnType.SUB_ROOM
        ) {
            Pair<Wall, boolean[][]> sideAndArray = Sides.side(
                pos.add(
                    0,
                    (coriSize.getHeight() + roomSize.getHeight()) * UNIT_HEIGHT,
                    0
                ),
                new Size(coriSize.getHeight(), wallWidth),
                colour,
                GlobalConstants.BOT_HORI_WALL_IMAGE,
                wallWidth
            );

            walls.add(sideAndArray.getKey());
            ArrayUtils.copyInArray(
                traversable,
                sideAndArray.getValue(),
                (int) (coriSize.getHeight() + roomSize.getHeight()),
                0
            );
        }

        /*
         * if none of the sides are a SUB_ROOM connection add the corner in the
         * top left of the room
         */
        if (
            connections.get(Direction.WEST) != ConnType.SUB_ROOM
            && connections.get(Direction.NORTH) != ConnType.SUB_ROOM
        ) {
            //corner connecting bottom and right
            corner(
                traversable,
                (int) (coriSize.getHeight() - wallWidth),
                (int) (coriSize.getHeight() - wallWidth),
                GlobalConstants.BOT_RIGHT_CORNER_WALL_IMAGE
            );
        }

        //if none of the sides are a SUB_ROOM connection add the corner in the top right of the room
        if(
            connections.get(Direction.EAST) != ConnType.SUB_ROOM
            && connections.get(Direction.NORTH) != ConnType.SUB_ROOM
        ) {
            //corner connecting bottom and left
            corner(
                traversable,
                (int) (coriSize.getHeight() + roomSize.getWidth()),
                (int) (coriSize.getHeight() - wallWidth),
                GlobalConstants.BOT_LEFT_CORNER_WALL_IMAGE
            );
        }

        //if none of the sides are a SUB_ROOM connection add the corner in the bottom left of the room
        if (
            connections.get(Direction.WEST) != ConnType.SUB_ROOM
            && connections.get(Direction.SOUTH) != ConnType.SUB_ROOM
        ) {
            //corner connecting top and right
            corner(
                traversable,
                (int) (coriSize.getHeight() - wallWidth),
                (int) (coriSize.getHeight() + roomSize.getHeight()),
        		GlobalConstants.TOP_RIGHT_CORNER_WALL_IMAGE
            );
        }

        //if none of the sides are a SUB_ROOM connection add the corner in the bottom right of the room
        if (
            connections.get(Direction.EAST) != ConnType.SUB_ROOM
            && connections.get(Direction.SOUTH) != ConnType.SUB_ROOM
        ) {
            //corner connecting top and left
            corner(
                traversable,
                (int) (coriSize.getHeight() + roomSize.getWidth()),
                (int) (coriSize.getHeight() + roomSize.getHeight()),
        		GlobalConstants.TOP_LEFT_CORNER_WALL_IMAGE
            );
        }

        //generate a Graph node at every traversable position
        GraphNode[][] traversableNodes = new GraphNode
            [traversable.length]
            [traversable[0].length];

        //The node represents the centre of each traversable location
        for (int i = 0; i < traversableNodes.length; i++) {
            for (int j = 0; j < traversableNodes[0].length; j++) {
                if (traversable[i][j]) {
                    traversableNodes[i][j] = new GraphNode(new Point2D(
                        pos.getX() + j * UNIT_WIDTH + UNIT_WIDTH / 2f,
                        pos.getY() + i * UNIT_HEIGHT + UNIT_HEIGHT / 2f
                    ));
                }
            }
        }

        // get the node that represents the centre of the room
        centreNode = traversableNodes
            [traversableNodes.length / 2]
            [traversableNodes[0].length / 2];

        /*
         * get a graph representing the positions reachable from the centre of
         * the room
         */
        Graph<GraphNode, Pair<Direction, Double>> graph = new Graph<>(
            traversableNodes,
            new TraversableEdgeGenerator()
        );

        traversableGraph = graph.getConnectedSubgraph(centreNode);

        /*
         * generate an array representing which locations you can spawn walls
         * into the room this is effectively the traversable array but it stops
         * things spawning in a line between room centres
         */
        boolean[][] spawnableLocations = traversable.clone();

        ArrayUtils.copyInArray(
            spawnableLocations,
            new boolean[1][spawnableLocations[0].length],
            spawnableLocations.length / 2,
            0
        );

        ArrayUtils.copyInArray(
            spawnableLocations,
            new boolean[spawnableLocations.length][1],
            0,
            spawnableLocations[0].length / 2
        );

        floorSpace = new boolean[traversable.length][traversable[0].length];

        /*
         * iterate through locations to spawn and then spawn objects in in the
         * location if:
         *     it want's to be spawned there,
         *     it can be spawned there,
         *     the position it would spawn in is reachable from the centre
         *     position
         */
        for (
            int i = 0;
            i < locationsToSpawn.length && i < spawnableLocations.length;
            i++
        ) {
            for (
                int j = 0;
                j < locationsToSpawn[i].length
                && j < spawnableLocations[i].length;
                j++
            ) {
                if (traversableGraph.getNodes().contains(traversableNodes[i][j])) {
                    floorSpace[i][j] = true;
                }

                if (
                    locationsToSpawn[i][j] && spawnableLocations[i][j]
                    && traversableGraph.getNodes()
                        .contains(traversableNodes[i][j])
                ) {
                    walls.add(new Wall(
                        pos.add(
                            (double) j * UNIT_WIDTH,
                            (double) i * UNIT_HEIGHT,
                            0
                        ),
                        new NetworkC(false),
                        new Size(UNIT_WIDTH, UNIT_HEIGHT),
                        colour.invert(),
                        //Make the object randomly be one of the possible options
                        GlobalConstants.IN_ROOM_OBJECTS.get(
                            rand.nextInt(
                                GlobalConstants.IN_ROOM_OBJECTS.size()
                            )
                        ),
                        1
                    ));

                    //Make the location you spawned in an object non traversable by making the corresponding node null
                    traversableNodes[i][j] = null;
                }
            }
        }

        traversableArray = traversableNodes;

        /*
         * get a graph representing the positions reachable from the centre of
         * the room again now walls have been spawned into the room
         */
        graph = new Graph<>(traversableNodes, new TraversableEdgeGenerator());

        traversableGraph = graph.getConnectedSubgraph(centreNode);
    }

	/***
     * add a corner at the given position and put it in traversable
     * @param traversable The traversable array to copy the corner into
     * @param relX The relative X position in the traversable array to spawn the corner in
     * @param relY The relative Y position in the traversable array to spawn the corner in
     *
     * Where the X and Y position represent the top left of the corner
     */
    private void corner(
		boolean[][] traversable,
		int relX,
		int relY,
		Image image
    ) {
        //add the entity representing the corner
    	Wall corner4 = new Wall(
            pos.add(
                (double) relX * UNIT_WIDTH,
                (double) relY * UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(
                (double) wallWidth * UNIT_WIDTH,
                (double) wallWidth * UNIT_HEIGHT
            ),
            colour,
            image,
            wallWidth
        );

        walls.add(corner4);

        // fill the array making the corner not traversable
        ArrayUtils.copyInArray(
            traversable,
            new boolean[(int) wallWidth][(int) wallWidth],
            relY,
            relX
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
        int wallWidth,
        Size coriSize,
        Color c,
        ConnType con
    ) {
        Pair<List<Wall>, boolean[][]> wallsAndArray;
        // chooses either a top side with or without an exit
        switch (con) {
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
                        getLocation().getY()
                        - roomSize.getHeight() / 2 * UNIT_HEIGHT
                    ),
                    Direction.NORTH
                ));
                break;
            case SUB_ROOM:
                /*
                * if it is a sub_room connection there should be no side so have a
                * placeholder array so you don't get null pointers
                */
                wallsAndArray = new Pair<>(
                    null,
                    ArrayUtils.fill2DArray(new boolean[1][1], true)
                );
                break;
            default:
                wallsAndArray = Sides.tNoExit(
                    pos,
                    wallWidth,
                    roomSize.getWidth(),
                    coriSize.getWidth(),
                    coriSize.getHeight(),
                    c
                );
                break;
        }

        if (wallsAndArray.getKey() != null) {
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
        int wallWidth,
        Size coriSize,
        Color c,
        ConnType con
    ) {
        Pair<List<Wall>,boolean[][]> wallsAndArray;
        // chooses either a bottom side with or without an exit
        switch (con) {
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
                        getLocation().getY() + roomSize.getHeight() / 2
                    ),
                    Direction.SOUTH
                ));
                break;
            case SUB_ROOM:
                /*
                * if it is a sub_room connection there should be no side so have a
                * placeholder array so you don't get null pointers
                */
                wallsAndArray = new Pair<>(
                    null, ArrayUtils.fill2DArray(new boolean[1][1], true)
                );
                break;
            default:
                wallsAndArray = Sides.bExit(
                    pos,
                    wallWidth,
                    roomSize.getWidth(),
                    coriSize.getWidth(),
                    coriSize.getHeight(),
                    c
                );
                break;
        }

        if (wallsAndArray.getKey() != null) {
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
        int wallWidth,
        Size coriSize,
        Color c,
        ConnType con
    ) {
        Pair<List<Wall>, boolean[][]> wallsAndArray;
        // chooses either a right side with or without an exit
        switch (con) {
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
                        getLocation().getX() + roomSize.getWidth() / 2,
                        getLocation().getY()
                    ),
                    Direction.EAST
                ));
                break;
            case SUB_ROOM:
                /*
                * if it is a sub_room connection there should be no side so have a
                * placeholder array so you don't get null pointers
                */
                wallsAndArray = new Pair<>(
                    null,
                    ArrayUtils.fill2DArray(new boolean[1][1], true)
                );
                break;
            default:
                wallsAndArray = Sides.rNoExit(
                    pos,
                    wallWidth,
                    roomSize.getHeight(),
                    coriSize.getWidth(),
                    coriSize.getHeight(),
                    c
                );

                break;
        }

        if (wallsAndArray.getKey() != null) {
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
        int wallWidth,
        Size coriSize,
        Color c,
        ConnType con
    ) {
        Pair<List<Wall>, boolean[][]> wallsAndArray;
        // chooses either a left side with or without an exit
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
                        getLocation().getX() - roomSize.getWidth() / 2,
                        getLocation().getY()
                    ),
                    Direction.WEST
                ));
                break;
            case SUB_ROOM:
                /*
                 * if it is a sub_room connection there should be no side so
                 * have a placeholder array so you don't get null pointers
                 */
                wallsAndArray = new Pair<>(
                    null,
                    ArrayUtils.fill2DArray(new boolean[1][1], true)
                );
                break;
            default:
                wallsAndArray = Sides.rNoExit(
                    pos,
                    wallWidth,
                    roomSize.getHeight(),
                    coriSize.getWidth(),
                    coriSize.getHeight(),
                    c
                );
                break;
        }

        if (wallsAndArray.getKey() != null) {
            walls.addAll(wallsAndArray.getKey());
        }

        return wallsAndArray.getValue();
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
            this.colour
        );
    }

    /***
     * sets the Z value of all the walls in the room to the newZ
     * @param newZ
     */
    public void setZ(double newZ) {
        for (Wall e : walls) {
            e.getRendererC().setZ(newZ);
        }
    }

    /**
     * Set the roomType of the room
     * @param roomType
     */
    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    /***
     *
     * @return The walls that make up the room
     */
    public List<Wall> getWalls() {
        return walls;
    }

    /***
     *
     * @return total size of the room with all the sides
     */
    public Size getTotalSize() {
        return new Size(
            totalSize.getWidth() * UNIT_WIDTH,
            totalSize.getHeight() * UNIT_HEIGHT
        );
    }

    /***
     *
     * @return size of the internal room
     */
    public Size getRoomSize() {
        return new Size(
            roomSize.getWidth() * UNIT_WIDTH,
            roomSize.getHeight() * UNIT_HEIGHT
        );
    }

    /**
     * @return the position of the top left of this room
     */
    public Point2D getPosition() {
        return new Point2D(pos.getX(), pos.getY());
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
    public Point2D getRoomCenter() {
        return new Point2D(pos.getX(), pos.getY()).add(
            (totalSize.getWidth() / 2) * UNIT_WIDTH,
            (totalSize.getHeight() / 2) * UNIT_HEIGHT
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

    /**
     * @return the list of corridor points
     */
    public List<Pair<Point2D, Direction>> getCorridorPoints() {
        return corridorPoints;
    }

    /**
     *
     * @return the RoomType of the room
     */
    public RoomType getRoomType() {
        return roomType;
    }

    /**
     *
     * @return The GraphNode in the Centre of the room
     */
    public GraphNode getCentreNode() {
        return centreNode;
    }

    /**
     *
     * @return The traversable array that makes up the room
     */
    public GraphNode[][] getTraversableArray() {
        return traversableArray;
    }

    /**
     *
     * @return The traversable graph that makes up the room
     */
    public Graph<GraphNode, Pair<Direction, Double>> getTraversableGraph() {
		return traversableGraph;
	}

    /**
     *
     * @return the connections from this room in each direction NSEW
     */
    public Map<Direction, ConnType> getConnections() {
        return connections;
    }

    /**
     *
     * @return the wall width (in terms of units)
     * this is given in units at this can be used vertically or horizontally
     * so when used in calculations you need to scale with either UNIT_WIDTH or UNIT_HEIGHT
     * depending on if its a height or a width
     */
    public double getWallWidth() {
        return wallWidth;
    }

    /**
     *
     * @return the corridor size (in terms of units)
     * this is given in units at this can be used vertically or horizontally
     * so when used in calculations you need to scale with either UNIT_WIDTH or UNIT_HEIGHT
     * depending on if its a height or a width
     */
    public Size getCoriSize() {
        return coriSize;
    }

    /**
     *
     * @return The floor space array
     * Used to work out where to render floor beneath objects
     */
    public boolean[][] getFloorSpace() {
        return floorSpace;
    }

    @Override
    public String toString() {
        return "Room at X: " + pos.getX() + " Y: " + pos.getY();
    }
}
