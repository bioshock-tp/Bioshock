package org.bioshock.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bioshock.engine.pathfinding.Graph;
import org.bioshock.engine.pathfinding.GraphNode;
import org.bioshock.main.App;

import javafx.geometry.Point2D;

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
public class PathfindingC<T extends GraphNode, S> {

    /**
     * The graph that stores the nodes and connections to work with
     */
    private Graph<T, S> graph;

    private Random rand = new Random();

    /**
     * The width of each node in the graph
     */
    private double unitWidth;

    /**
     * The height of each node in the graph
     */
    private double unitHeight;

    /**
     * A 2D array containing all of the nodes in a grid structure
     * Used to find the nearest node quickly
     */
    private T[][] nodeArray;

    public PathfindingC(
        Graph<T, S> g, T[][] nodeArray,
        double unitWidth,
        double unitHeight
    ) {
        graph = g;
        this.nodeArray = nodeArray;
        this.unitHeight = unitHeight;
        this.unitWidth = unitWidth;
    }


    /**
     *
     * Makes a random path of nodes given a start node
     * Picks random destination
     * Will not visit the same node twice
     *
     * @param startNode the node to start from
     * @param endNode the node to end on (null for a random endpoint)
     * @param avoidNode a node to avoid such as the previous room (null does
     * not avoid a room)
     * @param preferredNode the room that is the preferred first step
     * @return the list of nodes that form the path
     */
    public List<Point2D> createRandomPath(
        T startNode,
        T endNode,
        T avoidNode,
        T preferredNode
    ) {
        T current = startNode;
        List<Point2D> pathToFollow = new ArrayList<>();
        if (graph.getNodes().size() == 1) {
            App.logger.error("can't make a path when only one node in graph");
            pathToFollow.add(current.getLocation());
            return pathToFollow;
        }

        T destination = current;
        if (endNode == null) {
            Iterator<T> iterator = graph.getNodes().iterator();
            while (
                iterator.hasNext()
                && (
                    (destination = iterator.next()) == current
                    || destination == avoidNode
                )
            );

            if (destination == current) App.logger.error("Not enough nodes");
        }
        else {
            destination = endNode;
        }

        List<T> nodePath = new ArrayList<>();
        List<T> possibleMoves = new ArrayList<>();
        while (current != destination) {
            List<T> adjacents = graph.getConnectedNodes(current);
            for (T node : adjacents) {
                if (!nodePath.contains(node)) {
                    possibleMoves.add(node);
                }
            }

            // Does not go back to the avoid node if it can be helped
            if (
                avoidNode != null
                && possibleMoves.contains(avoidNode)
                && possibleMoves.size() > 1
            ) {
                possibleMoves.remove(avoidNode);
            }

            if (!possibleMoves.isEmpty()) {
                if (possibleMoves.contains(preferredNode)) {
                    current = preferredNode;
                }
                else {
                    int r = rand.nextInt(possibleMoves.size());
                    current = possibleMoves.get(r);
                }
            }
            else {
                destination = current;
            }

            nodePath.add(current);
            pathToFollow.add(current.getLocation());

            possibleMoves.clear();
        }

        return pathToFollow;
    }

    /**
     * Calls the createRandomPath function with null as the end node
     * and the nearest node to pos as a start node
     * @param pos starting position
     * @param avoidRoom the room to avoid
     * @param preferredNode the next preferred node
     * @return the list of positions of the nodes in the path
     */
    public List<Point2D> createRandomPath(
        Point2D pos,
        T avoidRoom,
        T preferredNode
    ) {
        return createRandomPath(
            findNearestNode(pos),
            null,
            avoidRoom,
            preferredNode
        );
    }

    /**
     *
     * Will find the best path from the start to the end
     * using A* algorithm
     * Euclidean distance from start to end is used as a heuristic
     * @param startNode the node to start from
     * @param endNode the node to finish on
     * @return the list of nodes that form the path
     */
    public List<Point2D> createBestPath(T startNode, T endNode) {
        List<Point2D> pathToFollow = new ArrayList<>();
        Graph<T, S> copyGraph = new Graph<>(graph);
        List<T> neighbours;
        List<T> openList = new ArrayList<>();
        List<T> closedList = new ArrayList<>();

        T currentNode;

        if (endNode == null) {
            Iterator<T> iterator = copyGraph.getNodes().iterator();
            do {
                endNode = iterator.next();
            }
            while (iterator.hasNext() && !endNode.equals(startNode));

            if (endNode.equals(startNode)) App.logger.error("Not enough nodes");
        }

        openList.add(startNode);

        while (!openList.isEmpty()) {
            currentNode = findSmallestCost(openList);
            openList.remove(currentNode);
            closedList.add(currentNode);

            if (currentNode.equals(endNode)) {
                pathToFollow = tracePath(startNode, endNode);
                break;
            }

            neighbours = copyGraph.getConnectedNodes(currentNode);

            for (T neighbour: neighbours) {
                if (
                    !(closedList.contains(neighbour)
                    || neighbour.isObject())
                ) {
                    int newCostToNeighbour = currentNode.getGCost()
                        + findCost(currentNode, neighbour);
                    if (
                        newCostToNeighbour < neighbour.getGCost()
                        || !openList.contains(neighbour)
                    ) {
                        neighbour.setGCost(newCostToNeighbour);
                        neighbour.setHCost(findCost(neighbour, endNode));
                        neighbour.setFCost(
                            neighbour.getGCost() + neighbour.getHCost()
                        );

                        neighbour.setParent(currentNode);

                        if (!openList.contains(neighbour)) {
                            openList.add(neighbour);
                        }
                    }
                }
            }
        }

        return pathToFollow;
    }

    /**
     * Calls the createBestPath method but uses the nodes closest to the given positions
     * @param pos the position that should be close to the start node
     * @param end the position that should be close to the end node
     * @return the list of positions of the nodes in the best path
     */
    public List<Point2D> createBestPath(Point2D pos, Point2D end) {
        return createBestPath(findNearestNode(pos), findNearestNode(end));
    }


    /**
     *
     * Will follow the chain of parents to create a path from the start node to
     * the end node
     *
     * @param startNode start of the path
     * @param endNode end of the path
     * @return a list of Point2D that contain the locations of each node in the
     * path
     */
    private List<Point2D> tracePath(T startNode, T endNode) {
        List<Point2D> path = new ArrayList<>();
        GraphNode currentNode = endNode;
        while (!currentNode.equals(startNode)) {
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
    private int findCost(T currentNode, T endNode) {
        Point2D currentNodeLocation = currentNode.getLocation();
        Point2D endNodeLocation = endNode.getLocation();

        return (int) Math.round(
            endNodeLocation
                .subtract(currentNodeLocation)
                .magnitude()
        );
    }


    /**
     *
     * Will search through a list of nodes and find the one with the smallest f
     * cost
     *
     * @param listToSearch the list of nodes to search through
     * @return the node with the lowest f cost
     * @throws IllegalStateException if the array is empty
     */
    private T findSmallestCost(List<T> listToSearch) {
        T smallestNode = listToSearch.get(0);
        for (T node: listToSearch) {
            if (
                node.getFCost() < smallestNode.getFCost()
                || (
                    node.getFCost() == smallestNode.getFCost()
                    && node.getHCost() < smallestNode.getHCost()
                )
            ) {
                smallestNode = node;
            }
        }
        return smallestNode;
    }


    /**
     *
     * Will find the node that a point is closest to
     * Will always find the one below it
     *
     * @param pos the point to find the closest node to
     * @return the closest node
     */
    public T findNearestNode(Point2D pos) {
        T[][] current = nodeArray;

        int i = (int) Math.floor(pos.getY() / unitHeight);
        int j = (int) Math.floor(pos.getX() / unitWidth);

        return current[i][j];
    }

    public void setGraph(Graph<T, S> newGraph) {
        this.graph = newGraph;
    }

    public Graph<T, S> getGraph() { return graph; }
}
