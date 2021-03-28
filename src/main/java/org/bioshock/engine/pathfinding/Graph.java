package org.bioshock.engine.pathfinding;

import java.util.*;

import org.bioshock.utils.DeepCopy;

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
    
    public List<S> getEdgesInfo(T node){
        List<S> edgesInfo = new ArrayList<>();
        nodeMap.get(node).forEach(pair -> edgesInfo.add(pair.getValue()));
        return edgesInfo;
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
    
    /***
     * gets a new Graph which only contains nodes you can 
     * traverse to and from the provided node
     * @param node the node all other nodes will be connected to
     * @return a deep copy of the new subgraph graph 
     */
    public Graph<T,S> getConnectedSubgraph(T node, DeepCopy<T> d) {
        return null;
    }
    
    /***
     * 
     * @param node
     * @return a list of all nodes that are indirectly 
     * connected to the given node including itself
     */
    public List<T> getIndirectlyConnectedNodes(T node){
        if(node == null) {
            throw new RuntimeException("node is null");
        }
        if(!nodeMap.containsKey(node)) {
            throw new RuntimeException("Node provided is not in graph");
        }
        
        ArrayList<T> visited = new ArrayList<>();
        ArrayList<T> frontier = new ArrayList<>();
        frontier.add(node);
        
        while(!frontier.isEmpty()) {
            T currNode = frontier.get(0);
            frontier.remove(0);            
            visited.add(currNode);
            
            for(T dirConNode : getConnectedNodes(currNode)) {
                if(!visited.contains(dirConNode) && !frontier.contains(dirConNode)) {
                    frontier.add(dirConNode);
                }
            }
        }
        return null;
    }
    
    /***
     * gets a graph that only contains the given nodes and only has 
     * connections to nodes in the trimmed graph
     * @param nodes the list of all nodes to contained in the trimmed graph
     * @param the function to deep copy a node T 
     * @return a deep copy of of the new trimmed graph
     */
    public Graph<T,S> getTrimmedGraph(List<T> nodes, DeepCopy<T> d) {
        return null;
    }
    
}
