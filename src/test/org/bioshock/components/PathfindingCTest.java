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


    @BeforeEach
    void setUp() {
        unitHeight = 5;
        unitWidth = 10;
        //Create a grid of traversable nodes
        traversableNodes = new GraphNode[5][5];
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                traversableNodes[i][j] = new GraphNode(new Point2D(i*unitHeight,j*unitWidth));
            }
        }
        traversableNodes[1][1].setIsObject(true);
        traversableNodes[1][2].setIsObject(true);
        traversableNodes[1][3].setIsObject(true);
        traversableNodes[2][1].setIsObject(true);
        traversableNodes[2][2].setIsObject(true);
        traversableNodes[2][3].setIsObject(true);
        traversableNodes[0][1].setIsObject(true);
        traversableNodes[0][3].setIsObject(true);

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
        expectedPath.add(new Point2D(5,0));
        expectedPath.add(new Point2D(10,0));
        expectedPath.add(new Point2D(15,0));
        expectedPath.add(new Point2D(20,0));

        assertEquals(expectedPath, pathfinding.createBestPath(traversableNodes[0][0], traversableNodes[4][0]));

        expectedPath.clear();
        //**************************************************************//

        //test to see if the path will avoid obstacles to go from [0][0] to [0][4]
        expectedPath.add(new Point2D(5,0));
        expectedPath.add(new Point2D(10,0));
        expectedPath.add(new Point2D(15,0));
        expectedPath.add(new Point2D(15,10));
        expectedPath.add(new Point2D(15,20));
        expectedPath.add(new Point2D(15,30));
        expectedPath.add(new Point2D(15,40));
        expectedPath.add(new Point2D(10,40));
        expectedPath.add(new Point2D(5,40));
        expectedPath.add(new Point2D(0,40));

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

        expectedPath.clear();
        //*************************************************************//

        //Confirm that it cant reach the path from [0][2] to [0][4], should return empty list

        assertEquals(expectedPath, pathfinding.createBestPath(traversableNodes[0][2], traversableNodes[0][4]));

        //Confirm that it cannot travel to a node that is an obstacle
        assertEquals(expectedPath, pathfinding.createBestPath(traversableNodes[0][0], traversableNodes[1][2]));
    }
}