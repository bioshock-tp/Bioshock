package org.bioshock.entities.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bioshock.engine.pathfinding.Graph;
import org.bioshock.utils.Direction;
import org.bioshock.utils.Point;
import org.bioshock.utils.Size;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class GenericMap {
    

    //stores a graph representing the map
    private Graph<Room,Pair<Direction,ConnType>> roomGraph = new Graph<>();
    
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
        
        if (roomTypes.length == 0 || roomTypes[0].length == 0) {
            throw new RuntimeException("Map is empty");
        }
        
        rooms = new Room[roomTypes.length][roomTypes[0].length];
        
        double tRoomWidth = newRoomSize.getWidth() + coriSize.getHeight()*2;
        double tRoomHeight = newRoomSize.getHeight() + coriSize.getHeight()*2;
        int firstRoomPosX=-1;
        int firstRoomPosY=-1;
        
        //iterate through the room array in row major order
        for (int i=0;i<roomTypes.length;i++) {
            for (int j=0;j<roomTypes[0].length;j++) {
//                //Check in all four directions to make sure it is connected to at le
//                if((i-1>0 && roomTypes[i-1][j] != RoomType.NO_ROOM) 
//                        || (i+1<roomTypes.length && roomTypes[i+1][j] != RoomType.NO_ROOM)
//                        || (j-1>0 && roomTypes[i][j-1] != RoomType.NO_ROOM)
//                        || (j+1<roomTypes[0].length && roomTypes[i][j+1] != RoomType.NO_ROOM)) {
//                    
//                }
                
                //If the current position should be a room
                if (roomTypes[i][j] != RoomType.NO_ROOM) {
                    //If this is the first room in the array set it's position

                        firstRoomPosX = j;
                        firstRoomPosY = i;
                    
                        j = roomTypes[0].length;
                        i = roomTypes.length;
                    //generate a new room at the current position
//                    rooms[i][j] = new Room(newPos.add(j*tRoomWidth, i*tRoomHeight, 0), 
//                            wallWidth, newRoomSize, coriSize, c);  
                }
            }
        }
        
        ArrayList<Room> expandedRooms = new ArrayList<>();
        ArrayList<Room> frontier = new ArrayList<>();
        frontier = rooms[i][j]
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

   public Graph<Room,Pair<Direction,ConnType>> getRoomGraph(){
       return roomGraph;
   }
}
