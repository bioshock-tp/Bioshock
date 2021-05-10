package org.bioshock.engine.pathfinding;

import javafx.geometry.Point2D;

/**
 *
 * A class to hold all the data needed for A* algorithm.
 * All objects that will need to be used for pathfinding should extend this
 * class (e.g. Room)
 *
 * @author Kian Wells
 */
public class GraphNode{

    /**
     * The (Euclidean distance) heuristic cost of the node (Used in A*)
     */
    private int hCost;

    /**
     * The cost from the start node along the current path taken (Used in A*)
     */
    private int gCost;

    /**
     * gCost + hCost (used in A*)
     */
    private int fCost;

    /**
     * The real world location of the node
     */
    private Point2D location = null;

    /**
     * The parent node used in A*
     */
    private GraphNode parent;

    /**
     * Used for A*, true if visited by the algorithm, false otherwise
     */
    private boolean isVisited;

    /**
     * Used to determine if the node is traversable (i.e. not a wall), ture if non traversable, false otherwise
     */
    private boolean isObject;

    /**
     * Creates a GraphNode and initialises all costs to be the max value for A* pathfinding
     */
    public GraphNode() {
        this.hCost = Integer.MAX_VALUE;
        this.fCost = Integer.MAX_VALUE;
        this.gCost = Integer.MAX_VALUE;
        this.isObject = false;
        this.isVisited = false;
    }

    /**
     * Creates a graph node with a given location
     */
    public GraphNode(Point2D location) {
        this();
        this.location = location;
    }

    public Point2D getLocation() {
        return location;
    }

    /**
     * @return the h cost of the node
     */
    public int getHCost() {
        return hCost;
    }

    /**
     * @return the g cost of the node
     */
    public int getGCost() {
        return gCost;
    }

    /**
     * @return the f cost of the node
     */
    public int getFCost() {
        return fCost;
    }

    /**
     * @return the node's parent node
     */
    public GraphNode getParent() {
        return parent;
    }

    /**
     * @return whether the node is visited
     */
    public boolean getIsVisited() {
        return isVisited;
    }

    /**
     * @return whether the node is an object
     */
    public boolean isObject() {
        return isObject;
    }

    /**
     * @param hCost h cost of the node
     */
    public void setHCost(int hCost) {
        this.hCost = hCost;
    }

    /**
     * @param gCost g cost of the node
     */
    public void setGCost(int gCost) {
        this.gCost = gCost;
    }

    /**
     * @param fCost f cost of the node
     */
    public void setFCost(int fCost) {
        this.fCost = fCost;
    }

    /**
     * @param location location of the node
     */
    public void setLocation(Point2D location) {
        this.location = location;
    }

    /**
     * @param parent parent node of the node
     */
    public void setParent(GraphNode parent) {
        this.parent = parent;
    }

    /**
     * @param visited the visited status of the node
     */
    public void setIsVisited(boolean visited) {
        isVisited = visited;
    }

    /**
     * @param object object status of the node
     */
    public void setIsObject(boolean object) {
        isObject = object;
    }
}
