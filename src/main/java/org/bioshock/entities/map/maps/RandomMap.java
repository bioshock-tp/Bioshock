package org.bioshock.entities.map.maps;

import java.util.List;
import java.util.Random;

import org.bioshock.engine.pathfinding.Graph;
import org.bioshock.entities.map.Room;
import org.bioshock.entities.map.TexRectEntity;
import org.bioshock.entities.map.utils.ConnType;
import org.bioshock.entities.map.utils.RoomType;
import org.bioshock.utils.Direction;
import org.bioshock.utils.Size;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class RandomMap implements Map{
    /***
     * the map created in the randomMap
     */
    GenericMap map;
    private double roomProb = 0.8;
    
    /***
     * Generate a new generic map
     * @param newPos the top left position of the map (even if there is no room in the top left position)
     * @param wallWidth the width of the walls that make up the map
     * @param newRoomSize the size of the internal room
     * @param coriSize the size of all corridors
     * @param c the colour of the map
     * @param maxMapSize the size of the array that the random array will be generated in
     * @param roomProb the probability of a room occurring at a given position
     * if null the default probability will be used
     * @param seed the seed to be used for random generation if seed is 
     * null a non-seeded random generator will be used
     */
    public RandomMap(
        Point3D newPos,
        double wallWidth,
        Size newRoomSize,
        Size coriSize,
        Color c,
        Size maxMapSize,
        Double roomProbObj,
        Long seed) {
        
        int maxMapWidth = (int) maxMapSize.getHeight();
        int maxMapHeight = (int) maxMapSize.getWidth();
        
        //if the room Prob is not null use the given value
        if(roomProbObj != null) {
            roomProb = roomProbObj;
        }
        
        RoomType[][] roomTypes = 
                new RoomType[maxMapHeight][maxMapWidth];
         
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
        for(int i=0;i<maxMapHeight;i++) {
            for(int j=0;j<maxMapWidth;j++) {
                if(rand.nextDouble()<roomProb) {
                    roomTypes[i][j] = RoomType.SINGLE_ROOM;
                }
                else {
                    roomTypes[i][j] = RoomType.NO_ROOM;
                }
            }
        }
        
        map = new GenericMap(newPos, wallWidth, newRoomSize, coriSize, c, roomTypes);
    }
 
    @Override
    public List<TexRectEntity> getWalls() {
        // TODO Auto-generated method stub
        return map.getWalls();
    }

    @Override
    public Graph<Room, Pair<Direction, ConnType>> getRoomGraph() {
        // TODO Auto-generated method stub
        return map.getRoomGraph();
    }

    @Override
    public List<Room> getRooms() {
        // TODO Auto-generated method stub
        return map.getRooms();
    }

}
