package org.bioshock.entities.map.maps;

import java.util.List;

import org.bioshock.engine.pathfinding.Graph;
import org.bioshock.engine.pathfinding.GraphNode;
import org.bioshock.entities.map.Room;
import org.bioshock.entities.map.Wall;
import org.bioshock.entities.map.utils.ConnType;
import org.bioshock.utils.Direction;

import javafx.util.Pair;

public interface Map {
    /***
    *
    * @return all the walls that make up a map
    */
	public List<Wall> getWalls();
	
	 /***
	    * @return the room graph representing the map
	    */
	public Graph<Room,Pair<Direction,ConnType>> getRoomGraph();
	/***
    *
    * @return the rooms in a map
    */
	public List<Room> getRooms();
	
	/**
	 * 
	 * @return an array of current rooms in the map where Room[0][0] is a 
	 * room at x=0 y=0 and if it is null there is no room at the given position
	 * the room at Room[0][1] should have the position x=total room width y=0
	 */
	public Room[][] getRoomArray();
	
	/**
	 * 
	 * @return a graph of all traversable positions in the map
	 */
	public Graph<GraphNode, Pair<Direction,Double>> getTraversableGraph();
	
    public GraphNode[][] getTraversableArray();
}
