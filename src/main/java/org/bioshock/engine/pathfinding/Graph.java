package org.bioshock.engine.pathfinding;

import java.util.*;

import javafx.util.Pair;

/***
 * A class which represents a graph with edges 
 * pointing to a different generic than the node
 * @author Kian Wells - Will Holbrook
 *
 * @param <T> The type of the nodes in the graph
 * @param <S> The type of the edges in a graph
 */
public class Graph<T,S> {

    /***
     * The map that stores the graph
     */
    private Map< T, List<Pair<T,S>> > nodeMap = new HashMap<>();
    
    /***
     * Adds a node to the map with no edges exiting it
     * @param nodeToAdd The node to be added
     */
    public void addNode(T nodeToAdd){
        nodeMap.put(nodeToAdd, new ArrayList<>());
    }
    
    /***
     * Adds an one directional edge between the two nodes
     * @param startNode The node the edge leaves from
     * @param endNode The node the edge enters into
     */
    public void addEdge(T startNode, Pair<T,S> endNode, boolean bidirectional){
        if(!nodeMap.containsKey(startNode)){
            addNode(startNode);
        }
        
        nodeMap.get(startNode).add(endNode);        
        if(bidirectional) {
            if(!nodeMap.containsKey(endNode.getKey())) {
                addNode(endNode.getKey());
            }
            nodeMap.get(endNode.getKey()).add(new Pair<T,S>(startNode, endNode.getValue()));
        }
    }
    
    /***
     * 
     * @param node
     * @return a list of all nodes connected to the given node
     */
    public List<T> getConnectedNodes(T node){        
        List<T> nodes = new ArrayList<>();
        nodeMap.get(node).forEach(pair -> nodes.add(pair.getKey()));
        return nodes;
    }
    
    /***
     * 
     * @param node
     * @return a list of all edges leaving the node
     */
    public List<Pair<T,S>> getEdges(T node){
        return nodeMap.get(node);
    }

    /***
     * 
     * @param node the node to count the number of edges
     * @return the number of edges leaving the given node
     */
    public int countEdges(T node){
        return nodeMap.get(node).size();
    }

    /***
     * 
     * @return all the nodes that make up the graph
     */
    public List<T> getNodes(){
        return new ArrayList<>(nodeMap.keySet());
    }

}
