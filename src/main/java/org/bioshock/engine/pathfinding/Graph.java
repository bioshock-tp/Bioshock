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
     * @param a deep copy function on the type T that returns a 
     * new object with all the same values of the given object
     * @return a deep copy of the new subgraph graph 
     */
    public Graph<T,S> getConnectedSubgraph(T node, DeepCopy<T> dc) {
        /***
         * list of nodes reachable from the given node
         */
        List<T> reachableNodes = getIndirectlyConnectedNodes(node);
        /***
         * list of nodes which you can reach from the given node and can traverse to the given node
         * i.e. if there was a one directional edge from A->B but not from B->A B wouldn't be in the list
         */
        ArrayList<T> bidirectionalNodes = new ArrayList<>();
        
        for(T testNode: reachableNodes) {
            //if you can reach node from the testNode add to bidirectionalNodes
            if(getIndirectlyConnectedNodes(testNode).contains(node)) {
                bidirectionalNodes.add(testNode);
            }
        }
        
        return getTrimmedGraph(bidirectionalNodes, dc);
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
        
        /***
         * list to store all the nodes that have been visited in the search
         */
        ArrayList<T> visited = new ArrayList<>();
        /***
         * list to store all nodes that can be visited but haven't been yet
         */
        ArrayList<T> frontier = new ArrayList<>();
        //add the first node to initialise the search
        frontier.add(node);
        
        //this search is an implementation  of breadth first search
        //loop while there are still nodes to explore
        while(!frontier.isEmpty()) {
            //visit the first node in the frontier
            T currNode = frontier.get(0);
            frontier.remove(0);            
            visited.add(currNode);
            
            //for every node directly connected to the node we are visiting add 
            //to the frontier if they haven't already been visited and aren't 
            //already in the frontier            
            for(T dirConNode : getConnectedNodes(currNode)) {
                if(!visited.contains(dirConNode) && !frontier.contains(dirConNode)) {
                    frontier.add(dirConNode);
                }
            }
        }
        return visited;
    }
    
    /***
     * gets a graph that only contains the given nodes and only has 
     * connections to nodes in the trimmed graph
     * @param nodes the list of all nodes to contained in the trimmed graph
     * @param a deep copy function on the type T that returns a 
     * new object with all the same values of the given object
     * @return a deep copy of of the new trimmed graph
     */
    public Graph<T,S> getTrimmedGraph(List<T> nodes, DeepCopy<T> dc) {
        /***
         * A mapping from old nodes to new nodes
         */
        HashMap<T,T> oldToNew = new HashMap<>();
        /***
         * The trimmed graph we're going to add
         */
        Graph<T,S> trimmedGraph = new Graph<>();
        for(T node:nodes) {
            //make a copy of each node and add to the mapping and the new graph
            T nodeCopy = dc.deepCopy(node);
            oldToNew.put(node, nodeCopy);
            trimmedGraph.addNode(nodeCopy);
        }
        
        for(T node:nodes) {        
            //for each edge coming out of the node in the parent graph
            for(Pair<T,S> edge:nodeMap.get(node)) {
                //if that edge connects to a node we want to have in the new graph
                if(nodes.contains(edge.getKey())) {
                    //add the edge to the new graph but with the new 
                    trimmedGraph.addEdge(
                        oldToNew.get(node), 
                        new Pair<T,S>(oldToNew.get(edge.getKey()),edge.getValue()), 
                        false
                    );
                }
            }
            
        }
        return trimmedGraph;
    }    
}
