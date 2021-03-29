package org.bioshock.entities.map.maps;

import java.util.List;

import org.bioshock.engine.pathfinding.Graph;
import org.bioshock.entities.map.Room;
import org.bioshock.entities.map.TexRectEntity;
import org.bioshock.entities.map.utils.ConnType;
import org.bioshock.utils.Direction;

import javafx.util.Pair;

public interface Map {
    /***
    *
    * @return all the walls that make up a map
    */
	public List<TexRectEntity> getWalls();
	
	 /***
	    * @return the room graph representing the map
	    */
	public Graph<Room,Pair<Direction,ConnType>> getRoomGraph();
	/***
    *
    * @return the rooms in a map
    */
	public List<Room> getRooms();
}
