package org.bioshock.entities.map;

import org.bioshock.engine.pathfinding.EdgeGenerator;
import org.bioshock.engine.pathfinding.GraphNode;
import org.bioshock.utils.Direction;

import javafx.util.Pair;

public class TraversableEdgeGenerator implements EdgeGenerator<GraphNode, Pair<Direction,Double>>{

	@Override
	public Pair<Direction, Double> getEdgeInfo(GraphNode source, GraphNode dest, Direction d) {
		Double distance = dest.getLocation().subtract(source.getLocation()).magnitude();
		return new Pair<>(d, distance);
	}
}
