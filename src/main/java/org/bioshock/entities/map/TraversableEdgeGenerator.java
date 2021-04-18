package org.bioshock.entities.map;

import static org.bioshock.utils.Direction.EAST;
import static org.bioshock.utils.Direction.NORTH;
import static org.bioshock.utils.Direction.SOUTH;
import static org.bioshock.utils.Direction.WEST;

import org.bioshock.engine.pathfinding.EdgeGenerator;
import org.bioshock.engine.pathfinding.GraphNode;
import org.bioshock.utils.Direction;

import javafx.util.Pair;

public class TraversableEdgeGenerator implements EdgeGenerator<GraphNode, Pair<Direction, Double>>{

	@Override
	public Pair<Direction, Double> getEdgeInfo(GraphNode source, GraphNode dest, Direction d) {
		if (d == NORTH || d == EAST || d == SOUTH || d == WEST) {
			Double distance = dest.getLocation().subtract(source.getLocation()).magnitude();
			return new Pair<>(d, distance);
		}
		return null;
	}
}
