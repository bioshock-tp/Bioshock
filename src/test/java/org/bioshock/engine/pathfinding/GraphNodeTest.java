package org.bioshock.engine.pathfinding;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class GraphNodeTest {

    @Test
    void getLocation() {
        GraphNode graphNode = new GraphNode(new Point2D(50,50));
        Point2D expected = new Point2D(50,50);
        assertEquals(expected, graphNode.getLocation());
    }

    @Test
    void getHCost() {
        GraphNode graphNode = new GraphNode();
        int expected = Integer.MAX_VALUE;
        assertEquals(expected, graphNode.getHCost());
    }

    @Test
    void getGCost() {
        GraphNode graphNode = new GraphNode();
        int expected = Integer.MAX_VALUE;
        assertEquals(expected, graphNode.getGCost());
    }

    @Test
    void getFCost() {
        GraphNode graphNode = new GraphNode();
        int expected = Integer.MAX_VALUE;
        assertEquals(expected, graphNode.getFCost());
    }

    @Test
    void getParent() {
        GraphNode graphNode = new GraphNode();
        assertNull(graphNode.getParent());
    }

    @Test
    void issVisited() {
        GraphNode graphNode = new GraphNode();
        assertFalse(graphNode.isVisited());
    }

    @Test
    void isObject() {
        GraphNode graphNode = new GraphNode();
        assertFalse(graphNode.isObject());
    }

    @Test
    void setHCost() {
        GraphNode graphNode = new GraphNode();
        graphNode.setHCost(20);
        assertEquals(20, graphNode.getHCost());
    }

    @Test
    void setGCost() {
        GraphNode graphNode = new GraphNode();
        graphNode.setGCost(20);
        assertEquals(20, graphNode.getGCost());
    }

    @Test
    void setFCost() {
        GraphNode graphNode = new GraphNode();
        graphNode.setFCost(20);
        assertEquals(20, graphNode.getFCost());
    }

    @Test
    void setLocation() {
        GraphNode graphNode = new GraphNode();
        graphNode.setLocation(new Point2D(1000,1000));
        assertEquals(new Point2D(1000,1000), graphNode.getLocation());
    }

    @Test
    void setParent() {
        GraphNode graphNode = new GraphNode();
        GraphNode parent = new GraphNode(new Point2D(100,100));
        graphNode.setParent(parent);
        Field parentF;
        try{
            parentF = GraphNode.class.getDeclaredField("parent");
        }catch(Exception e){
            fail("parent field not found");
            return;
        }

        assertEquals(parent, graphNode.getParent());
    }

    @Test
    void setVisited() {
        GraphNode graphNode = new GraphNode();
        graphNode.setVisited(true);
        assertTrue(graphNode.isVisited());
    }

    @Test
    void setIsObject() {
        GraphNode graphNode = new GraphNode();
        graphNode.setObject(true);
        assertTrue(graphNode.isObject());
    }
}