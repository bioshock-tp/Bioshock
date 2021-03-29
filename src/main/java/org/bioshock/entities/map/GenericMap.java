package org.bioshock.entities.map;

import static org.bioshock.entities.map.ConnType.ROOM_TO_ROOM;
import static org.bioshock.entities.map.RoomType.SINGLE_ROOM;
import static org.bioshock.utils.Direction.EAST;
import static org.bioshock.utils.Direction.NORTH;
import static org.bioshock.utils.Direction.SOUTH;
import static org.bioshock.utils.Direction.WEST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bioshock.engine.pathfinding.Graph;
import org.bioshock.gui.NewGameController;
import org.bioshock.utils.Direction;
import org.bioshock.utils.Size;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class GenericMap implements Map{
    

    /***
     * stores a graph representing the map
     */
    private Graph<Room,Pair<Direction,ConnType>> roomGraph = new Graph<>();
    
    /***
     * Stores a map of room pairs to connection types
     * used to work out how two different rooms should be connected
     */
    private HashMap<Pair<RoomType,RoomType>, ConnType> roomsToConnType = new HashMap<>();
    {
    	roomsToConnType.put(new Pair<>(SINGLE_ROOM, SINGLE_ROOM), ROOM_TO_ROOM);
    }
    
    /***
     * Generate a new generic map
     * @param newPos the top left position of the map (even if there is no room in the top left position)
     * @param wallWidth the width of the walls that make up the map
     * @param newRoomSize the size of the internal room
     * @param coriSize the size of all corridors
     * @param c the colour of the map
     * @param roomTypes the 2d array which the map is going to be generated from
     */
    public GenericMap(
        Point3D newPos,
        double wallWidth,
        Size newRoomSize,
        Size coriSize,
        Color c,
        RoomType[][] roomTypes
    ) {
      //stores the rooms in a map
        Room[][] rooms;
        
        //Makes sure the map you have provided isn't null or empty
        if (roomTypes == null || roomTypes.length == 0 || roomTypes[0].length == 0) {
            throw new RuntimeException("Map is empty");
        }
        
        //Creates a new array to store room objects in the relevant location
        rooms = new Room[roomTypes.length][roomTypes[0].length];
        
        //Gets the total room width and height to calculate the position of each new room
        double tRoomWidth = newRoomSize.getWidth() + coriSize.getHeight()*2;
        double tRoomHeight = newRoomSize.getHeight() + coriSize.getHeight()*2;
        
        //iterate through the room array in row major order
        for (int i=0;i<roomTypes.length;i++) {
            for (int j=0;j<roomTypes[0].length;j++) {
                
                //If the current position should be a room
                if (roomTypes[i][j] != RoomType.NO_ROOM) {
                    //generate a new room at the current position
                    rooms[i][j] = new Room(newPos.add(j*tRoomWidth, i*tRoomHeight, 0), 
                            wallWidth, newRoomSize, coriSize, c);  
                    
                    //add the newly generated room to the room graph
                    roomGraph.addNode(rooms[i][j]);
                }
            }
        }
        
        //iterate through the room array in row major order
        for (int i=0;i<roomTypes.length;i++) {
            for (int j=0;j<roomTypes[0].length;j++) {                
                //If the current position should be a room
                if (rooms[i][j] != null) {
                	//add an edge in the graph if there should be one in each possible direction
                    safeAddEdge(i, j, i-1, j, NORTH, roomTypes, rooms);
                    safeAddEdge(i, j, i+1, j, SOUTH, roomTypes, rooms);
                    safeAddEdge(i, j, i, j+1, EAST, roomTypes, rooms);
                    safeAddEdge(i, j, i, j-1, WEST, roomTypes, rooms);
                    
//                    //Initialise the room with the newly created edges
//                    rooms[i][j].init(roomGraph.getEdgesInfo(rooms[i][j]));
                }
            }
        }

        /***
         * list of all unchecked rooms in the parentGraph
         */
        List<Room> uncheckedRooms = roomGraph.getNodes();
        Graph<Room,Pair<Direction,ConnType>> currGraph = new Graph<>();
        
        //Find the biggest connected subgraph and set the room graph to be the biggest connected subgraph
        while(!uncheckedRooms.isEmpty()) {
            Graph<Room,Pair<Direction,ConnType>> newGraph = roomGraph.getConnectedSubgraph(uncheckedRooms.get(0), null);            
            uncheckedRooms.removeAll(newGraph.getNodes());
            
            if(currGraph.getNodes().size()<newGraph.getNodes().size()) {
                currGraph = newGraph;
            }
        }        
        
        roomGraph = currGraph;
        initRoomsFromGraph();
    }
    
    private void initRoomsFromGraph() {
        for(Room r: roomGraph.getNodes()) {
            r.init(roomGraph.getEdgesInfo(r));
        }
    }
    
    /***
     * Adds a 1 directional edge between the room at 
     * (i1,j1) and (i2,j2) in the rooms array
     * 
     * The connection type is dictated by the hash 
     * map from pairs of room types to connection types
     * @param i1
     * @param j1
     * @param i2
     * @param j2
     * @param d the direction for the edge between the nodes
     * @param roomTypes The array that stores the types of each room
     * @param rooms the array which stores the actual rooms
     */
    private void safeAddEdge(int i1, int j1, int i2, int j2, Direction d,
		RoomType[][] roomTypes, Room[][] rooms) {
    	//If all the points are in the array then add the edge
    	if(0 <= i1 && i1 < roomTypes.length
			&& 0 <= i2 && i2 < roomTypes.length
			&& 0 <= j1 && j1 < roomTypes[0].length
			&& 0 <= j2 && j2 < roomTypes[0].length) {
    		
    		//If both the rooms to add an edge between are not null start adding the edge
    		if (rooms[i1][j1] != null && rooms[i2][j2] != null) {
    			//Get the connection type between the two rooms
    			ConnType con = roomsToConnType.get(
					new Pair<>(roomTypes[i1][j1], roomTypes[i2][j2])
				);
    			
    			//Adds the edge to the room graph
    			roomGraph.addEdge(
					rooms[i1][j1], 
					new Pair<>(rooms[i2][j2], new Pair<>(d, con)), 
					false
				);
    		}
    	}
    }
    
    /***
    *
    * @return the rooms in a map
    */
   public List<Room> getRooms() {
       return roomGraph.getNodes();
   }

   /***
    *
    * @return all the walls that make up a map
    */
   public List<TexRectEntity> getWalls() {
       List<TexRectEntity> walls = new ArrayList<>();
       for (Room r : roomGraph.getNodes()) {
           walls.addAll(r.getWalls());
       }
       return walls;
   }

   /***
    * @return the room graph representing the map
    */
   public Graph<Room,Pair<Direction,ConnType>> getRoomGraph(){
       return roomGraph;
   }
}
