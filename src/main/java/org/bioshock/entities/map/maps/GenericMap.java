package org.bioshock.entities.map.maps;

import static org.bioshock.utils.GlobalConstants.UNIT_HEIGHT;
import static org.bioshock.utils.GlobalConstants.UNIT_WIDTH;

import java.util.ArrayList;
import java.util.List;

import org.bioshock.engine.pathfinding.Graph;
import org.bioshock.entities.map.Room;
import org.bioshock.entities.map.RoomEdgeGenerator;
import org.bioshock.entities.map.TexRectEntity;
import org.bioshock.entities.map.utils.ConnType;
import org.bioshock.entities.map.utils.RoomType;
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
        double tRoomWidth = (newRoomSize.getWidth() + coriSize.getHeight()*2)*UNIT_WIDTH;
        double tRoomHeight = (newRoomSize.getHeight() + coriSize.getHeight()*2)*UNIT_HEIGHT;
        
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
        
        roomGraph = new Graph<>(rooms, new RoomEdgeGenerator());

        /***
         * list of all unchecked rooms in the parentGraph
         */
        List<Room> uncheckedRooms = roomGraph.getNodes();
        Graph<Room,Pair<Direction,ConnType>> currGraph = new Graph<>();
        
        //Find the biggest connected subgraph and set the room graph to be the biggest connected subgraph
        while(!uncheckedRooms.isEmpty()) {
            Graph<Room,Pair<Direction,ConnType>> newGraph = roomGraph.getConnectedSubgraph(uncheckedRooms.get(0));            
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
