package org.bioshock.components;

import javafx.geometry.Point2D;
import javafx.util.Pair;
import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.pathfinding.Graph;
import org.bioshock.engine.pathfinding.GraphNode;
import org.bioshock.entities.map.Room;
import org.bioshock.entities.map.utils.ConnType;
import org.bioshock.main.App;
import org.bioshock.scenes.SceneManager;
import org.bioshock.utils.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/***
 *
 * A component class that contains the methods needed for pathfinding
 * An entity that needs to find paths should have this component
 *
 * @author Kian Wells
 *
 * @param <T> The type of the nodes in the graph
 * @param <S> The type of the edges in a graph
 */
public class PathfindingC<T extends GraphNode,S> {
    private Graph<T, S> graph;
    private Random rand = new Random();
    private double unitWidth, unitHeight;
    private T[][] nodeArray;


    public PathfindingC(Graph<T,S> g, T[][] nodeArray, double unitWidth, double unitHeight){
        graph = g;
        this.nodeArray = nodeArray;
        this.unitHeight = unitHeight;
        this.unitWidth = unitWidth;
    }


    public void setGraph(Graph<T,S> newGraph){
        this.graph = newGraph;
    }
    public Graph<T,S> getGraph(){ return graph; }

    /**
     *
     * Makes a random path of nodes given a start node
     * Picks random destination
     * Will not visit the same node twice
     *
     * @param startNode the node to start from
     * @param endNode the node to end on (null for a random endpoint)
     * @param avoidNode a node to avoid such as the previous room (null does not avoid a room)
     * @param preferredRoom the room that is the preferred first step
     * @return the list of nodes that form the path
     */
    public List<Point2D> createRandomPath(T startNode, T endNode, T avoidNode, T preferredRoom) {
        List<Point2D> pathToFollow = new ArrayList<>();
        List<T> nodePath = new ArrayList<>();
        List<T> possibleMoves = new ArrayList<>();
        List<T> adjacents;
        T destination;
        T current;
        int r;
        int c = 0;

        current = startNode;
        destination = current;
        if(graph.getNodes().size() == 1) {
            App.logger.error("can't make a path when only one node in graph");
            pathToFollow.add(current.getLocation());
            return pathToFollow;
        }
//        App.logger.debug("Start room is {}", startRoom.getRoomCenter());
        if(endNode == null){
            while(destination.equals(current) || destination.equals(avoidNode)) {
                r = rand.nextInt(graph.getNodes().size());
                destination = graph.getNodes().get(r);
            }
        }
        else{
            destination = endNode;
        }

//        App.logger.debug(
//                "Destination room is {}",
//                destination.getRoomCenter()
//        );

        //nodePath.add(current);
        //pathToFollow.add(current.getLocation());
//        App.logger.debug("Room {} is {}", c, startRoom.getRoomCenter());
        c++;

        while (current != destination) {
            adjacents = graph.getConnectedNodes(current);
            for(T node : adjacents){
                if(!nodePath.contains(node)){
                    possibleMoves.add(node);
                }
            }

            //doesnt go back to the avoid node if it can be helped
            if(avoidNode != null
                    && possibleMoves.contains(avoidNode)
                    && possibleMoves.size() > 1){
                possibleMoves.remove(avoidNode);
            }

            if(!possibleMoves.isEmpty()) {
                if(possibleMoves.contains(preferredRoom)){
                    current = preferredRoom;
                }
                else {
                    r = rand.nextInt(possibleMoves.size());
                    current = possibleMoves.get(r);
                }
            }
            else{
                destination = current;
            }

            nodePath.add(current);
            pathToFollow.add(current.getLocation());
//            App.logger.debug("Room {} is {}", c, current.getRoomCenter());
            possibleMoves.clear();
            c++;

        }

        return pathToFollow;
    }

    public List<Point2D> createRandomPath(Point2D pos, Point2D end, T avoidRoom, T preferredRoom) {
        return createRandomPath(findNearestNode(pos), findNearestNode(end), avoidRoom, preferredRoom);
    }

    public List<Point2D> createRandomPath(Point2D pos, T avoidRoom, T preferredRoom){
        return createRandomPath(findNearestNode(pos), null, avoidRoom, preferredRoom);
    }

    public List<Point2D> createRandomPath(T start, T avoidRoom, T preferredRoom){
        return createRandomPath(start, null, avoidRoom, preferredRoom);
    }

    /**
     *
     * Will find the best path from the start to the end
     * using A* algorithm
     *
     * @param startNode the node to start from
     * @param endNode the node to finish on
     * @return the list of nodes that form the path
     */
    public List<Point2D> createBestPath(T startNode, T endNode) {
        List<Point2D> pathToFollow = new ArrayList<>();
        Graph<T, S> copyGraph = graph.makeCopy();
        List<T> neighbours;
        List<T> openList = new ArrayList<>();
        List<T> closedList = new ArrayList<>();

        T currentNode;

        if(endNode == null){
            int r = rand.nextInt(copyGraph.getNodes().size());
            do{
                endNode = copyGraph.getNodes().get(r);
            }
            while(!endNode.equals(startNode));
        }

        openList.add(startNode);

        while(openList.size() > 0){
            currentNode = findSmallestCost(openList);
            openList.remove(currentNode);
            closedList.add(currentNode);

            if(currentNode.equals(endNode)){
                pathToFollow = tracePath(startNode, endNode);
                break;
            }

            neighbours = copyGraph.getConnectedNodes(currentNode);

            for(T neighbour: neighbours){
                if(!(closedList.contains(neighbour) || neighbour.getIsObject())){
                    int newCostToNeighbour = currentNode.getGCost() + findCost(currentNode, neighbour);
                    if(newCostToNeighbour < neighbour.getGCost() || !openList.contains(neighbour)){
                        neighbour.setGCost(newCostToNeighbour);
                        neighbour.setHCost(findCost(neighbour,endNode));
                        neighbour.setFCost(neighbour.getGCost() + neighbour.getHCost());

                        neighbour.setParent(currentNode);

                        if(!openList.contains(neighbour)){
                            openList.add(neighbour);
                        }
                    }
                }
            }

        }
        //pathToFollow.remove(0);
        App.logger.debug("Path created is {}", pathToFollow);

        return pathToFollow;
    }

    public List<Point2D> createBestPath(T startNode) {
        return createBestPath(startNode, null);
    }

    public List<Point2D> createBestPath(Point2D pos) {
        return createBestPath(findNearestNode(pos), null);
    }

    public List<Point2D> createBestPath(Point2D pos, Point2D end) {
        return createBestPath(findNearestNode(pos), findNearestNode(end));
    }

    /**
     *
     * Will follow the chain of parents to create a path from the start node to the end node
     *
     * @param startNode start of the path
     * @param endNode end of the patg
     * @return a list of Point2D that contain the locations of each node in the path
     */

    private List<Point2D> tracePath(T startNode, T endNode){
        List<Point2D> path = new ArrayList<>();
        GraphNode currentNode = endNode;
        while(!currentNode.equals(startNode)){
            path.add(currentNode.getLocation());
            currentNode = currentNode.getParent();
        }
        Collections.reverse(path);

        return path;
    }


    /**
     *
     * Finds the cost between 2 nodes
     * Can be used for finding g cost if the nodes connect
     * Can be used for finding the h cost between a node and the end node
     *
     * @param currentNode the node to start from
     * @param endNode the node to find the cost to
     * @return the cost as an integer
     */
    private int findCost(T currentNode, T endNode){
        Point2D currentNodeLocation = currentNode.getLocation();
        Point2D endNodeLocation = endNode.getLocation();

        return (int)Math.round(endNodeLocation.subtract(currentNodeLocation).magnitude());
    }


    /**
     *
     * Will search through a list of nodes and find the one with the smallest f cost
     *
     * @param listToSearch the list of nodes to search through
     * @return the node with the lowest f cost
     * @throws IllegalStateException if the array is empty
     */
    private T findSmallestCost(List<T> listToSearch) throws IllegalStateException{
        try{
            T smallestNode = listToSearch.get(0);
            for(T node: listToSearch){
                if(node.getFCost() < smallestNode.getFCost()){
                    smallestNode = node;
                }
                else if(node.getFCost() == smallestNode.getFCost()){
                    if(node.getHCost() < smallestNode.getHCost()){
                        smallestNode = node;
                    }
                }
            }
            return smallestNode;
        }
        catch (IllegalStateException e){
            throw new IllegalStateException("List is empty: ", e);
        }
    }

    /**
     *
     * Will find the node that a point is closest to
     *
     * @param pos the point to find the closest node to
     * @return the closest node
     */
    public T findNearestNode(Point2D pos) {
        T[][] current = nodeArray;

        int i = (int) Math.floor(pos.getY()/unitHeight);
        int j = (int) Math.floor(pos.getX()/unitWidth);

        return current[i][j];
    }


}
