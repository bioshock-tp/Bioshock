package org.bioshock.entities.map;

import static org.bioshock.utils.Direction.*;

import org.bioshock.engine.pathfinding.EdgeGenerator;
import org.bioshock.entities.map.utils.ConnType;
import org.bioshock.utils.Direction;

import javafx.util.Pair;

public class RoomEdgeGenerator implements EdgeGenerator<Room, Pair<Direction,ConnType>>{

	@Override
	public Pair<Direction, ConnType> getEdgeInfo(Room source, Room dest, Direction d) {
	    if(source.getOpenlyConnectedRooms().contains(dest)) {
	        return new Pair<>(d, ConnType.SUB_ROOM);
	    }
		if(d == NORTH || d == EAST || d == SOUTH || d == WEST) {
			return new Pair<>(d, ConnType.ROOM_TO_ROOM);
		}
		return null;
	}

}
