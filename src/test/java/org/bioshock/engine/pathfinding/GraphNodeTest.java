package org.bioshock.engine.pathfinding;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

import javafx.geometry.Point2D;

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
    void setHCost() throws IllegalAccessException {
        GraphNode graphNode = new GraphNode();
        graphNode.setHCost(20);

        Field hCost;
        try{
            hCost = GraphNode.class.getDeclaredField("hCost");
        }catch(Exception e){
            fail("hCost field not found");
            return;
        }
        hCost.setAccessible(true);

        assertEquals(20, hCost.getInt(graphNode));
    }

    @Test
    void setGCost() throws IllegalAccessException {
        GraphNode graphNode = new GraphNode();
        graphNode.setGCost(20);
        Field gCost;
        try{
            gCost = GraphNode.class.getDeclaredField("gCost");
        }catch(Exception e){
            fail("gCost field not found");
            return;
        }
        gCost.setAccessible(true);

        assertEquals(20, gCost.getInt(graphNode));
    }

    @Test
    void setFCost() throws IllegalAccessException {
        GraphNode graphNode = new GraphNode();
        graphNode.setFCost(20);
        Field fCost;
        try{
            fCost = GraphNode.class.getDeclaredField("fCost");
        }catch(Exception e){
            fail("fCost field not found");
            return;
        }
        fCost.setAccessible(true);

        assertEquals(20, fCost.getInt(graphNode));
    }

    @Test
    void setLocation() throws IllegalAccessException {
        GraphNode graphNode = new GraphNode();
        graphNode.setLocation(new Point2D(1000,1000));
        Field location;
        try{
            location = GraphNode.class.getDeclaredField("location");
        }catch(Exception e){
            fail("location field not found");
            return;
        }
        location.setAccessible(true);

        assertEquals(new Point2D(1000,1000), location.get(graphNode));
    }

    @Test
    void setParent() throws IllegalAccessException {
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
        parentF.setAccessible(true);

        assertEquals(parent, parentF.get(graphNode));
    }

    @Test
    void setVisited() throws IllegalAccessException {
        GraphNode graphNode = new GraphNode();
        graphNode.setVisited(true);
        Field isVisited;
        try{
            isVisited = GraphNode.class.getDeclaredField("isVisited");
        }catch(Exception e){
            fail("isVisited field not found");
            return;
        }
        isVisited.setAccessible(true);

        assertTrue(isVisited.getBoolean(graphNode));
    }

    @Test
    void setIsObject() throws IllegalAccessException {
        GraphNode graphNode = new GraphNode();
        graphNode.setObject(true);
        Field isObject;
        try{
            isObject = GraphNode.class.getDeclaredField("isObject");
        }catch(Exception e){
            fail("isVisited field not found");
            return;
        }
        isObject.setAccessible(true);

        assertTrue(isObject.getBoolean(graphNode));
    }
}