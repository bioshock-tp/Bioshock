package org.bioshock.entities.map.maps;

import static org.bioshock.utils.GlobalConstants.UNIT_HEIGHT;
import static org.bioshock.utils.GlobalConstants.UNIT_WIDTH;
import static org.bioshock.utils.ArrayUtils.safeGet;

import java.util.ArrayList;
import java.util.List;

import org.bioshock.engine.pathfinding.Graph;
import org.bioshock.engine.pathfinding.GraphNode;
import org.bioshock.entities.map.Room;
import org.bioshock.entities.map.RoomEdgeGenerator;
import org.bioshock.entities.map.TexRectEntity;
import org.bioshock.entities.map.TraversableEdgeGenerator;
import org.bioshock.entities.map.utils.ConnType;
import org.bioshock.entities.map.utils.RoomType;
import org.bioshock.main.App;
import org.bioshock.utils.ArrayUtils;
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
    private Graph<GraphNode, Pair<Direction,Double>> traversableGraph;
    GraphNode[][] traversableArray;
    


    private Room[][] roomArray;
    /**
     * a list of rooms ordered first by height then by width
     */
    private List<Room> orderedRoomList = new ArrayList<>();
       
    /***
     * Generate a new generic map
     * @param newPos the top left position of the map (even if there is no room in the top left position)
     * @param wallWidth the width of the walls that make up the map (in terms of units)
     * @param newRoomSize the size of the internal room (in terms of units)
     * @param coriSize the size of all corridors (in terms of units)
     * @param c the colour of the map
     * @param roomTypes the 2d array which the map is going to be generated from
     */
     public GenericMap(
        Point3D newPos,
        double wallWidth,
        Size newRoomSize,
        Size coriSize,
        Color c,
        RoomType[][] roomTypes,
        long seed
    ) {
      //stores the rooms in a map
        Room[][] rooms;
        
        //Makes sure the map you have provided isn't null or empty
        if (roomTypes == null || roomTypes.length == 0 || roomTypes[0].length == 0) {
            throw new RuntimeException("Map is empty");
        }
        
        //Creates a new array to store room objects in the relevant location
        rooms = new Room[roomTypes.length][roomTypes[0].length];
        
        //Gets the total room width and height in units
        int tRoomWidthUnits = (int) (newRoomSize.getWidth() + coriSize.getHeight()*2);
        int tRoomHeightUnits = (int) (newRoomSize.getHeight() + coriSize.getHeight()*2);
        
        //Gets the total room width and height to calculate the position of each new room
        int tRoomWidth = tRoomWidthUnits*UNIT_WIDTH;
        int tRoomHeight = tRoomHeightUnits*UNIT_HEIGHT;
        
        //iterate through the room array in row major order
        for (int i=0;i<roomTypes.length;i++) {
            for (int j=0;j<roomTypes[0].length;j++) {
                
                //If the current position should be a room
                if (roomTypes[i][j] != RoomType.NO_ROOM) {
                    //generate a new room at the current position
                    rooms[i][j] = new Room(newPos.add(j*tRoomWidth, i*tRoomHeight, 0), 
                            wallWidth, newRoomSize, coriSize, c);
                    rooms[i][j].setRoomType(roomTypes[i][j]);
                   
                }
            }
        }
        
        //for every room try to combine the room
        for (int i=0;i<roomTypes.length;i++) {
            for (int j=0;j<roomTypes[0].length;j++) {
                tryToCombineRooms(rooms, i, j);
            }
        }
        
        //generate the room graph from the room array and then get the largest 
        //connected subgraph so its the largest possible random map
        roomGraph = (new Graph<>(rooms, new RoomEdgeGenerator())).getLargestConnectedSubgraph();     
        
        //Store all the rooms that are in the room graph in the ordered list and in an array
        roomArray = new Room[rooms.length][rooms[0].length];
        for(int i=0;i<rooms.length;i++) {
            for(int j=0;j<rooms[0].length;j++) {
                if(rooms[i][j] != null && roomGraph.getNodes().contains(rooms[i][j])) {
                    orderedRoomList.add(rooms[i][j]);
                    roomArray[i][j] = rooms[i][j];
                }
            }
        }
        
        //initialize every room
        initRooms(seed);
        
        //generate a traversable array to represent all traversable locations in the map
        traversableArray = new GraphNode[rooms.length*tRoomHeightUnits][rooms[0].length*tRoomWidthUnits];
        for(int i=0;i<rooms.length;i++) {
            for(int j=0;j<rooms[0].length;j++) {
                if(rooms[i][j] != null && roomGraph.getNodes().contains(rooms[i][j])) {
                    ArrayUtils.copyInArray(traversableArray, rooms[i][j].getTraversableArray(), i*tRoomHeightUnits, j*tRoomWidthUnits);
                }
            }
        }
        
        traversableGraph = new Graph<>(traversableArray, new TraversableEdgeGenerator()).getConnectedSubgraph(orderedRoomList.get(0).getCentreNode());
        
        //For logging and debugging purposes
//        boolean[][] traversableBooleanArray = new boolean[traversableArray.length][traversableArray[0].length];
//        for(int i=0;i<traversableBooleanArray.length;i++) {
//            for(int j=0;j<traversableBooleanArray[0].length;j++) {
//                if(traversableArray[i][j] != null && traversableGraph.getNodes().contains(traversableArray[i][j])) {
//                    traversableBooleanArray[i][j] = true;
//                }
//            }
//        }
//        ArrayUtils.log2DArray(traversableBooleanArray);
    }
    
    private boolean tryToCombineRooms(Room[][] rooms, int i, int j) {
    	return square2(rooms,i,j) || vert2(rooms,i,j) || hori2(rooms,i,j);
    }
    
    private boolean vert2(Room[][] rooms, int i, int j) {
    	List<Pair<Integer, Integer>> positions = new ArrayList<>();
    	positions.add(new Pair<>(i,j));
    	positions.add(new Pair<>(i+1,j));
    	
    	return biggerRoom(rooms, i, j, positions);
    }
    
    private boolean hori2(Room[][] rooms, int i, int j) {
    	List<Pair<Integer, Integer>> positions = new ArrayList<>();
    	positions.add(new Pair<>(i,j));
    	positions.add(new Pair<>(i,j+1));
    	
    	return biggerRoom(rooms, i, j, positions);
    }
    
    private boolean square2(Room[][] rooms, int i, int j) {
    	List<Pair<Integer, Integer>> positions = new ArrayList<>();
    	positions.add(new Pair<>(i,j));
    	positions.add(new Pair<>(i,j+1));
    	positions.add(new Pair<>(i+1,j));
    	positions.add(new Pair<>(i+1,j+1));
    	
    	return biggerRoom(rooms, i, j, positions);
    }
    
    private boolean biggerRoom(Room[][] rooms, int i, int j, List<Pair<Integer, Integer>> positions) {
    	List<Pair<Room,Pair<Integer,Integer>>> roomAndPoses = new ArrayList<>();
    	
    	for(Pair<Integer, Integer> coord: positions) {
    		//if any position in the potential big room is null the the big room can't be constructed
    		Room r = safeGet(rooms, coord.getKey(), coord.getValue());
    		if (r == null) {
    			return false;
    		}
    		//if any room is already a part of a big room a new big room can't be constructed 
    		if(!r.getOpenlyConnectedRooms().isEmpty()) {
    			return false;
    		}
    		
    		roomAndPoses.add(new Pair<>(r,coord));
    	}
    	
    	for(Pair<Room,Pair<Integer,Integer>> rp1: roomAndPoses) {
    		for(Pair<Room,Pair<Integer,Integer>> rp2: roomAndPoses) {
        		if(adjacent(rp1.getValue(), rp2.getValue())) {
        			rp1.getKey().getOpenlyConnectedRooms().add(rp2.getKey());
        		}
        	}
    	}
    	
    	return true;
    }
    
    /***
     * returns true if the two points are adjacent false if they are not or the same point
     * @param coord1
     * @param coord2
     * @return
     */
    private boolean adjacent(Pair<Integer, Integer> coord1, Pair<Integer, Integer> coord2) {
    	if(((coord1.getKey() == coord2.getKey() + 1 ||coord1.getKey() == coord2.getKey() - 1 ) 
    			&& coord1.getValue() == coord2.getValue()) ||
    			((coord1.getValue() == coord2.getValue() + 1 ||coord1.getValue() == coord2.getValue() - 1 ) 
				&& coord1.getKey() == coord2.getKey())) {
    		return true;
    	}
    	return false;
    }
    
    private void initRooms(long seed) {
        int i=0;
        for(Room r : orderedRoomList) {
            r.init(roomGraph.getEdgesInfo(r), null, seed+i);
            i++;
        }
    }
        
    /***
    *
    * @return the rooms in a map
    */
   public List<Room> getRooms() {
       return orderedRoomList;
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

    @Override
    public Room[][] getRoomArray() {
        return roomArray;
    }

    @Override
    public Graph<GraphNode, Pair<Direction, Double>> getTraversableGraph() {
        return traversableGraph;
    }
    
    public GraphNode[][] getTraversableArray() {
        return traversableArray;
    }
}
