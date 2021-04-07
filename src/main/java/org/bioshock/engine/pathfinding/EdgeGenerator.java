package org.bioshock.engine.pathfinding;

import org.bioshock.utils.Direction;

/***
 * A class which allows you to generate edge info 
 * given a source node a destination node and a 
 * direction from the source to the destination
 * @author Will Holbrook
 *
 * @param <T> The type of the node
 * @param <S> The type of the edge information
 */
public interface EdgeGenerator<T,S> {
	/***
	 * return edge info given a source node a destination node and a 
	 * direction
	 * @param source
	 * @param dest
	 * @param d
	 * @return The edge info 
	 */
	public S getEdgeInfo(T source, T dest, Direction d);
}
