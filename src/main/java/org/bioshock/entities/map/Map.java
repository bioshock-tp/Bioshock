package org.bioshock.entities.map;

import java.util.List;

import org.bioshock.engine.pathfinding.Graph;
import org.bioshock.utils.Direction;

import javafx.util.Pair;

public interface Map {
	public List<TexRectEntity> getWalls();
	public Graph<Room,Pair<Direction,ConnType>> getRoomGraph();
	public List<Room> getRooms();
}
