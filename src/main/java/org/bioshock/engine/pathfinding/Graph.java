package org.bioshock.engine.pathfinding;

import static org.bioshock.utils.Direction.EAST;
import static org.bioshock.utils.Direction.NORTH;
import static org.bioshock.utils.Direction.NORTH_EAST;
import static org.bioshock.utils.Direction.NORTH_WEST;
import static org.bioshock.utils.Direction.SOUTH;
import static org.bioshock.utils.Direction.SOUTH_EAST;
import static org.bioshock.utils.Direction.SOUTH_WEST;
import static org.bioshock.utils.Direction.WEST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bioshock.utils.DeepCopy;
import org.bioshock.utils.Direction;

import javafx.util.Pair;

/***
 * A class which represents a graph with edges
 * pointing to a different generic than the node
 * @author Kian Wells - Will Holbrook
 *
 * @param <T> The type of the nodes in the graph
 * @param <S> The type of the edges in a graph
 */
public class Graph<T extends GraphNode, S> {
    /***
     * The map that stores the graph
     */
    private Map<T, List<Pair<T, S>>> nodeMap = new HashMap<>();


    /***
     * constructs an empty graph
     */
    public Graph() {}

    /**
     * constructs a graph based of the given node array using the
     * edge generator to generate the edges between the 8 adjacent nodes
     *
     * Note the given graph won't necessarily be connected
     * @param nodes2D The array of nodes to be put into the graph
     * @param eg The edge generator that gives the rules about what
     * edges between adjacent nodes should be
     */
    public Graph(T[][] nodes2D, EdgeGenerator<T, S> eg) {
        for (T[] nodes: nodes2D) {
            for (T node: nodes) {
            	//if the node isn't null add the node to the graph
                if (node != null) {
                	addNode(node);
                }
            }
        }

        //add all edges between all 8 adjacent nodes in the array
        for (int i = 0; i < nodes2D.length; i++) {
            for (int j = 0; j < nodes2D[0].length; j++) {
                if (nodes2D[i][j] != null) {
                    /*
                     * add an edge in the graph if there should be one in each
                     * possible direction
                     */
                    safeAddEdge(i, j, i - 1, j, 	NORTH, 		nodes2D, eg);
                    safeAddEdge(i, j, i - 1, j + 1, NORTH_EAST, nodes2D, eg);
                    safeAddEdge(i, j, i, 	 j + 1, EAST, 		nodes2D, eg);
                    safeAddEdge(i, j, i + 1, j + 1, SOUTH_EAST, nodes2D, eg);
                    safeAddEdge(i, j, i + 1, j,     SOUTH, 		nodes2D, eg);
                    safeAddEdge(i, j, i + 1, j - 1,	SOUTH_WEST, nodes2D, eg);
                    safeAddEdge(i, j, i, 	 j - 1, WEST, 		nodes2D, eg);
                    safeAddEdge(i, j, i - 1, j - 1, NORTH_WEST, nodes2D, eg);
                }
            }
        }
    }


    /**
     * Clones a graph
     * @param graph the graph to clone
     */
    public Graph(Graph<T, S> graph) {
        this.nodeMap = new HashMap<>(graph.nodeMap);
    }


    /***
     * Adds a 1 directional edge between the room at
     * (i1, j1) and (i2, j2) in the nodes array
     *
     * The edge info is dictated by the EdgeGenerator
     * interface implementation
     * @param i1
     * @param j1
     * @param i2
     * @param j2
     * @param d the direction for the edge between the nodes
     * @param nodes2D the array which stores nodes
     * @param eg an implementation of the edge generator interface
     * for the types of the graph
     */
    private void safeAddEdge(
        int i1,
        int j1,
        int i2,
        int j2,
        Direction d,
        T[][] nodes2D,
        EdgeGenerator<T, S> eg
     ) {
        // If all the points are in the array then add the edge
        if (
            0 <= i1 && i1 < nodes2D.length
            && 0 <= i2 && i2 < nodes2D.length
            && 0 <= j1 && j1 < nodes2D[0].length
            && 0 <= j2 && j2 < nodes2D[0].length
            && nodes2D[i1][j1] != null && nodes2D[i2][j2] != null
        ) {
            /*
             * If both the rooms to add an edge between are not null start
             * adding the edge
             */

            // Get the connection type between the two rooms
            S edgeInfo = eg.getEdgeInfo(nodes2D[i1][j1], nodes2D[i2][j2], d);

            // If there is no edge info don't add the edge
            if (edgeInfo == null) {
                return;
            }

            // Adds the edge to the room graph
            addEdge(
                nodes2D[i1][j1],
                new Pair<>(nodes2D[i2][j2], edgeInfo),
                false
            );
        }
    }


    /***
     * Adds a node to the map with no edges exiting it
     * @param nodeToAdd The node to be added
     */
    public void addNode(T nodeToAdd) {
        nodeMap.putIfAbsent(nodeToAdd, new ArrayList<>());
    }


    /***
     * Adds an one directional edge between the two nodes
     * @param startNode The node the edge leaves from
     * @param endNode The node the edge enters into
     */
    public void addEdge(
        T startNode,
        Pair<T, S> endNode,
        boolean bidirectional
    ) {
        if (!nodeMap.containsKey(startNode)) {
            addNode(startNode);
        }

        nodeMap.get(startNode).add(endNode);
        if (bidirectional) {
            if (!nodeMap.containsKey(endNode.getKey())) {
                addNode(endNode.getKey());
            }
            nodeMap.get(endNode.getKey()).add(new Pair<>(
                startNode,
                endNode.getValue()
            ));
        }
    }

    /***
     * @param node
     * @return a list of all nodes connected to the given node not including
     * the node itself
     */
    public List<T> getConnectedNodes(T node) {
        List<T> nodes = new ArrayList<>();
        nodeMap.get(node).forEach(pair -> nodes.add(pair.getKey()));
        return nodes;
    }

    /**
     *
     * @param node
     * @return the edge info for all edges leaving the given node
     */
    public List<S> getEdgesInfo(T node) {
        List<S> edgesInfo = new ArrayList<>();
        nodeMap.get(node).forEach(pair -> edgesInfo.add(pair.getValue()));
        return edgesInfo;
    }

    /***
     *
     * @param node
     * @return a list of all edges leaving the node
     */
    public List<Pair<T, S>> getEdges(T node) {
        return nodeMap.get(node);
    }


    /***
     *
     * @param node the node to count the number of edges
     * @return the number of edges leaving the given node
     */
    public int countEdges(T node) {
        return nodeMap.get(node).size();
    }


    /***
     *
     * @return all the nodes that make up the graph
     */
    public Set<T> getNodes() {
        return nodeMap.keySet();
    }


    /***
     * gets a new Graph which only contains nodes you can
     * traverse to from the provided node
     * @param node the node all other nodes will be connected to
     * @param dc a deep copy function on the type T that returns a
     * new object with all the same values of the given object
     *
     * if this is null it doesn't do a deep copy
     * @return a deep copy of the new subgraph graph
     */
    public Graph<T, S> getConnectedSubgraph(T node) {
        /***
         * list of nodes reachable from the given node
         */
        List<T> reachableNodes = getIndirectlyConnectedNodes(node);

        return getTrimmedGraph(reachableNodes);
    }


    /***
     *
     * @param node
     * @return a list of all nodes that are indirectly
     * connected to the given node including itself
     */
    public List<T> getIndirectlyConnectedNodes(T node) {
        if (node == null) {
            throw new NullPointerException("node is null");
        }
        if (!nodeMap.containsKey(node)) {
            throw new IllegalArgumentException(
                "Node provided is not in graph"
            );
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
        while (!frontier.isEmpty()) {
            //visit the first node in the frontier
            T currNode = frontier.get(0);
            frontier.remove(0);
            visited.add(currNode);

            //for every node directly connected to the node we are visiting add
            //to the frontier if they haven't already been visited and aren't
            //already in the frontier
            for(T dirConNode : getConnectedNodes(currNode)) {
                if (
                    !visited.contains(dirConNode)
                    && !frontier.contains(dirConNode)
                ) {
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
     *
     * if this is null it doesn't do a deep copy
     * @return a deep copy of of the new trimmed graph
     */
    public Graph<T, S> getTrimmedGraph(List<T> nodes) {
        /***
         * The trimmed graph we're going to add
         */
        Graph<T, S> trimmedGraph = new Graph<>();
        for (T node:nodes) {
            trimmedGraph.addNode(node);
        }

        for (T node:nodes) {
            // for each edge coming out of the node in the parent graph
            for (Pair<T, S> edge:nodeMap.get(node)) {
                /*
                 * if that edge connects to a node we want to have in the new
                 * graph
                 */
                if (nodes.contains(edge.getKey())) {
                    // add the edge to the new graph but with the new
                    trimmedGraph.addEdge(
                        node,
                        new Pair<>(edge.getKey(),edge.getValue()),
                        false
                    );
                }
            }
        }
        return trimmedGraph;
    }

    /**
     * Will find the first node that is connected to the given node with the
     * given connection
     * @param edge the type of connection to look for
     * @param node the node to look at the connections of
     * @return the first node with that connection
     */
    public T getNodeFromEdge(S edge, T node) {
        List<Pair<T, S>> edges = getEdges(node);

        T returnNode = null;
        for (Pair<T, S> pair : edges) {
            if (pair.getValue().equals(edge) && returnNode == null) {
                returnNode = pair.getKey();
            }
        }

        return returnNode;
    }


    /***
     *
     * @param dc an implementation of the deep copy interface for the type of
     * the node
     * @return a deep copy of the graph this is called on
     */
    public Graph<T, S> deepCopy(DeepCopy<T> dc) {
        Set<T> nodes = this.getNodes();
        /***
         * A mapping from old nodes to new nodes
         */
        HashMap<T, T> oldToNew = new HashMap<>();
        /***
         * The trimmed graph we're going to add
         */
        Graph<T, S> copy = new Graph<>();

        for (T node : nodes) {
            //make a copy of each node and add to the mapping and the new graph
            T nodeCopy;
            nodeCopy = dc.deepCopy(node);
            oldToNew.put(node, nodeCopy);
            copy.addNode(nodeCopy);
        }

        for (T node : nodes) {
            // for each edge coming out of the node in the parent graph
            for (Pair<T, S> edge:nodeMap.get(node)) {
                /*
                 * if that edge connects to a node we want to have in the new
                 * graph
                 */
                if (nodes.contains(edge.getKey())) {
                    // add the edge to the new graph but with the new
                    copy.addEdge(
                        oldToNew.get(node),
                        new Pair<>(
                            oldToNew.get(edge.getKey()),
                            edge.getValue()
                        ),
                        false
                    );
                }
            }
        }

        return copy;
    }

    /***
     *
     * @return the largest connected subgraph
     */
    public Graph<T, S> getLargestConnectedSubgraph() {
        /***
         * list of all unchecked rooms in the parentGraph
         */
        Set<T> uncheckedRooms = this.getNodes();
        Graph<T, S> currGraph = new Graph<>();

        /*
         * Find the biggest connected subgraph and set the room graph to be the
         * biggest connected subgraph
         */
        while (!uncheckedRooms.isEmpty()) {
            Graph<T, S> newGraph = getConnectedSubgraph(
                uncheckedRooms.iterator().next()
            );
            uncheckedRooms.removeAll(newGraph.getNodes());

            if (currGraph.getNodes().size() < newGraph.getNodes().size()) {
                currGraph = newGraph;
            }
        }

        return currGraph;
    }
}
