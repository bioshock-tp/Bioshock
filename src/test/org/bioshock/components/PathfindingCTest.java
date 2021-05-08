package org.bioshock.components;

import javafx.geometry.Point2D;
import javafx.util.Pair;
import org.bioshock.engine.pathfinding.Graph;
import org.bioshock.engine.pathfinding.GraphNode;
import org.bioshock.entities.map.TraversableEdgeGenerator;
import org.bioshock.entities.map.utils.ConnType;
import org.bioshock.utils.Direction;
import org.bioshock.utils.GlobalConstants;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PathfindingCTest {
    private static Graph<GraphNode, Pair<Direction, Double>> graph;
    private static int unitHeight;
    private static int unitWidth;
    private static GraphNode[][] traversableNodes;
    private static PathfindingC<GraphNode, Pair<Direction, Double>> pathfinding;

    private static List<GraphNode> objectNodes;


    @BeforeEach
    void setUp() {
        unitHeight = 5;
        unitWidth = 10;
        //Create a grid of traversable nodes
        traversableNodes = new GraphNode[5][5];
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                traversableNodes[i][j] = new GraphNode(new Point2D(i*unitWidth,j*unitHeight));
            }
        }
        objectNodes = new ArrayList<>();

        traversableNodes[1][1].setIsObject(true);
        objectNodes.add(traversableNodes[1][1]);
        traversableNodes[1][2].setIsObject(true);
        objectNodes.add(traversableNodes[1][2]);
        traversableNodes[1][3].setIsObject(true);
        objectNodes.add(traversableNodes[1][3]);
        traversableNodes[2][1].setIsObject(true);
        objectNodes.add(traversableNodes[2][1]);
        traversableNodes[2][2].setIsObject(true);
        objectNodes.add(traversableNodes[2][2]);
        traversableNodes[2][3].setIsObject(true);
        objectNodes.add(traversableNodes[2][3]);
        traversableNodes[0][1].setIsObject(true);
        objectNodes.add(traversableNodes[0][1]);
        traversableNodes[0][3].setIsObject(true);
        objectNodes.add(traversableNodes[0][3]);

        /*
        +--------------+
        |  |  |  |  |  |
        +--------------+
        |X |X |X |  |  |
        +--------------+
        |  |X |X |  |  |
        +--------------+
        |X |X |X |  |  |
        +--------------+
        |  |  |  |  |  |
        +--------------+
         */

        graph = new Graph<>(traversableNodes, new TraversableEdgeGenerator());
        pathfinding = new PathfindingC<>(graph, traversableNodes, unitWidth, unitHeight);
    }

    @Test
    void createBestPath() {
        List<Point2D> expectedPath = new ArrayList<>();

        //test to check path from node [0][0] to node [4][0]
        /*
        +--------------+
        |  |  |  |  |  |
        +--------------+
        |X |X |X |  |  |
        +--------------+
        |  |X |X |  |  |
        +--------------+
        |X |X |X |  |  |
        +--------------+
        |S |->|->|->|G |
        +--------------+
         */
        expectedPath.add(new Point2D(10,0));
        expectedPath.add(new Point2D(20,0));
        expectedPath.add(new Point2D(30,0));
        expectedPath.add(new Point2D(40,0));

        assertEquals(expectedPath, pathfinding.createBestPath(traversableNodes[0][0], traversableNodes[4][0]));

        expectedPath.clear();
        //**************************************************************//

        //test to see if the path will avoid obstacles to go from [0][0] to [0][4]
        expectedPath.add(new Point2D(10,0));
        expectedPath.add(new Point2D(20,0));
        expectedPath.add(new Point2D(30,0));
        expectedPath.add(new Point2D(30,5));
        expectedPath.add(new Point2D(30,10));
        expectedPath.add(new Point2D(30,15));
        expectedPath.add(new Point2D(30,20));
        expectedPath.add(new Point2D(20,20));
        expectedPath.add(new Point2D(10,20));
        expectedPath.add(new Point2D(0,20));

        /*
        +--------------+
        |G |<-|<-|<-|  |
        +--------------+
        |X |X |X |^ |  |
        +--------------+
        |  |X |X |^ |  |
        +--------------+
        |X |X |X |^ |  |
        +--------------+
        |S |->|->|^ |  |
        +--------------+
         */

        assertEquals(expectedPath, pathfinding.createBestPath(traversableNodes[0][0], traversableNodes[0][4]));
        assertEquals(expectedPath, pathfinding.createBestPath(new Point2D(0,0), new Point2D(40,0)));

        expectedPath.clear();
        //*************************************************************//

        //Confirm that it cant reach the path from [0][2] to [0][4], should return empty list

        assertEquals(expectedPath, pathfinding.createBestPath(traversableNodes[0][2], traversableNodes[0][4]));

        //Confirm that it cannot travel to a node that is an obstacle
        assertEquals(expectedPath, pathfinding.createBestPath(traversableNodes[0][0], traversableNodes[1][2]));


        assertThrows(ArrayIndexOutOfBoundsException.class, () -> pathfinding.createBestPath(traversableNodes[0][0], traversableNodes[0][8]));
    }

    @RepeatedTest(20)
    void createRandomPath(){
        List<Point2D> path = pathfinding.createRandomPath(traversableNodes[0][0],null,null,null);

        //Check that an empty path is not made
        assertFalse(path.isEmpty());

        //Check that it doesnt contain the start node
        assertFalse(path.contains(traversableNodes[0][0].getLocation()));

        //check that it doesnt contain any of the nodes to avoid
        for(GraphNode g : objectNodes){
            assertFalse(path.contains(g.getLocation()));
        }

        path = pathfinding.createRandomPath(traversableNodes[4][4], traversableNodes[0][0], traversableNodes[3][3], traversableNodes[4][3]);
        assertTrue(path.contains(traversableNodes[4][3].getLocation()));

        //testing using point2d
        path = pathfinding.createRandomPath(new Point2D(0,0),  null, null);
        assertFalse(path.isEmpty());
        assertFalse(path.contains(new Point2D(0,0)));



        assertThrows(ArrayIndexOutOfBoundsException.class, () -> pathfinding.createRandomPath(traversableNodes[0][0], traversableNodes[0][8],null ,null));
    }

    @Test
    void findNearestNode(){
        assertEquals(traversableNodes[0][0], pathfinding.findNearestNode(new Point2D(0,0)));
        assertEquals(traversableNodes[4][4], pathfinding.findNearestNode(new Point2D(41,21)));
        assertEquals(traversableNodes[0][4], pathfinding.findNearestNode(new Point2D(42,0)));
    }

    @Test
    void setGraph(){
        Graph<GraphNode, Pair<Direction, Double>> newGraph = new Graph<>();
        pathfinding.setGraph(newGraph);
        assertEquals(newGraph, pathfinding.getGraph());
    }

    @Test
    void getGraph(){
        assertEquals(graph, pathfinding.getGraph());
    }
}