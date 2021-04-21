package org.bioshock.engine.pathfinding;

import javafx.geometry.Point2D;

/**
 *
 * A class to hold all the data needed for A* algorithm
 * All objects that will need to be used for pathfinding should extend this class
 * (e.g. Room)
 *
 * @author Kian Wells
 */
public class GraphNode{

    private int hCost;
    private int gCost;
    private int fCost;
    private Point2D location;
    private GraphNode parent;
    private boolean isVisited;
    private boolean isObject;

    public GraphNode(){
        this.location = null;
        this.hCost = Integer.MAX_VALUE;
        this.fCost = Integer.MAX_VALUE;
        this.gCost = Integer.MAX_VALUE;
        this.parent = null;
        this.isObject = false;
        this.isVisited = false;
    }
    
    public GraphNode(Point2D location) {
    	this();
    	this.location = location; 
    }
    
    public Point2D getLocation() {
        return location;
    }

    public int getHCost() {
        return hCost;
    }

    public int getGCost() {
        return gCost;
    }

    public int getFCost() {
        return fCost;
    }

    public GraphNode getParent() {
        return parent;
    }

    public boolean getIsVisited() {
        return isVisited;
    }

    public boolean getIsObject() {
        return isObject;
    }

    public void setHCost(int hCost) {
        this.hCost = hCost;
    }

    public void setGCost(int gCost) {
        this.gCost = gCost;
    }

    public void setFCost(int fCost) {
        this.fCost = fCost;
    }

    public void setLocation(Point2D location) {
        this.location = location;
    }

    public void setParent(GraphNode parent) {
        this.parent = parent;
    }

    public void setIsVisited(boolean visited) {
        isVisited = visited;
    }

    public void setIsObject(boolean object) {
        isObject = object;
    }
}
