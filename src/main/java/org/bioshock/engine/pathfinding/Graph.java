package org.bioshock.engine.pathfinding;

import java.util.*;

public class Graph<T> {

    private Map< T, List<T> > nodeMap = new HashMap<>();

    public void addNode(T nodeToAdd){
        nodeMap.put(nodeToAdd, new ArrayList<>());
    }

    public void addEdge(T startNode, T endNode){
        if(!nodeMap.containsKey(startNode)){
            addNode(startNode);
        }
        if(!nodeMap.containsKey(endNode)){
            addNode(endNode);
        }
        nodeMap.get(startNode).add(endNode);
        nodeMap.get(endNode).add(startNode);
    }

    public List<T> getConnections(T node){
        return nodeMap.get(node);
    }

    public int countEdges(T node){
        return nodeMap.get(node).size();
    }

    public List<T> getNodes(){
        return new ArrayList<>(nodeMap.keySet());
    }

}
