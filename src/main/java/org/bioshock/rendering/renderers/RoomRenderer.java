package org.bioshock.rendering.renderers;

import static org.bioshock.rendering.RenderManager.getRenHeight;
import static org.bioshock.rendering.RenderManager.getRenWidth;
import static org.bioshock.rendering.RenderManager.getRenX;
import static org.bioshock.rendering.RenderManager.getRenY;
import static org.bioshock.utils.Direction.*;
import static org.bioshock.entities.map.utils.ConnType.*;
import static org.bioshock.utils.GlobalConstants.*;

import java.util.HashMap;

import org.bioshock.entities.map.Room;
import org.bioshock.entities.map.RoomEntity;
import org.bioshock.entities.map.utils.ConnType;
import org.bioshock.utils.Direction;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RoomRenderer implements Renderer {
    public static <E extends RoomEntity> void render(
            GraphicsContext gc,
            E roomEntity
    ) {
        Room room = roomEntity.getRoom();
        HashMap<Direction,ConnType> connections = room.getConnections();
        double coriLen = room.getCoriSize().getHeight(); //in terms of units
        double coriWidth = room.getCoriSize().getWidth(); //in terms of units
        double roomWidth = room.getRoomSize().getWidth();
        double roomHeight = room.getRoomSize().getHeight();
        double wallWidth = room.getWallWidth();                
        
        gc.save();
        gc.setFill(Color.RED);
        if(connections.get(NORTH) == SUB_ROOM) {
            gc.fillRect(
                    getRenX(room.getPos().getX()+coriLen*UNIT_WIDTH), 
                    getRenY(room.getPos().getY()), 
                    getRenWidth(roomWidth), 
                    getRenHeight(coriLen*UNIT_HEIGHT));
        }
        else if(connections.get(NORTH) == ROOM_TO_ROOM) {
            gc.fillRect(
                    getRenX(room.getPos().getX()
                            +coriLen*UNIT_WIDTH
                            +(roomWidth-coriWidth*UNIT_WIDTH)/2), 
                    getRenY(room.getPos().getY()), 
                    getRenWidth(coriWidth*UNIT_WIDTH), 
                    getRenHeight(coriLen*UNIT_HEIGHT));
        }
        

        gc.setFill(Color.BLUE);
        if(connections.get(SOUTH) == SUB_ROOM) {
            gc.fillRect(
                    getRenX(room.getPos().getX()+coriLen*UNIT_WIDTH), 
                    getRenY(room.getPos().getY()+roomHeight+coriLen*UNIT_HEIGHT), 
                    getRenWidth(roomWidth), 
                    getRenHeight(coriLen*UNIT_HEIGHT));
        }
        else if(connections.get(SOUTH) == ROOM_TO_ROOM) {
            gc.fillRect(
                    getRenX(room.getPos().getX()
                            +coriLen*UNIT_WIDTH
                            +(roomWidth-coriWidth*UNIT_WIDTH)/2), 
                    getRenY(room.getPos().getY()+roomHeight+coriLen*UNIT_HEIGHT), 
                    getRenWidth(coriWidth*UNIT_WIDTH), 
                    getRenHeight(coriLen*UNIT_HEIGHT));
        }
        

        gc.setFill(Color.YELLOW);
        if(connections.get(WEST) == SUB_ROOM) {
            gc.fillRect(
                    getRenX(room.getPos().getX()), 
                    getRenY(room.getPos().getY()+coriLen*UNIT_HEIGHT), 
                    getRenWidth(coriLen*UNIT_WIDTH), 
                    getRenHeight(roomHeight));
        }
        else if(connections.get(WEST) == ROOM_TO_ROOM) {
            gc.fillRect(
                    getRenX(room.getPos().getX()), 
                    getRenY(room.getPos().getY()
                            +coriLen*UNIT_HEIGHT
                            +(roomHeight-coriWidth*UNIT_HEIGHT)/2), 
                    getRenWidth(coriLen*UNIT_WIDTH), 
                    getRenHeight(coriWidth*UNIT_HEIGHT));
        }
        

        gc.setFill(Color.GREEN);
        if(connections.get(EAST) == SUB_ROOM) {
            gc.fillRect(
                    getRenX(room.getPos().getX()+roomWidth+coriLen*UNIT_WIDTH), 
                    getRenY(room.getPos().getY()+coriLen*UNIT_HEIGHT), 
                    getRenWidth(coriLen*UNIT_WIDTH), 
                    getRenHeight(roomHeight));
        }
        else if(connections.get(EAST) == ROOM_TO_ROOM) {
            gc.fillRect(
                    getRenX(room.getPos().getX()+roomWidth+coriLen*UNIT_WIDTH), 
                    getRenY(room.getPos().getY()
                            +coriLen*UNIT_HEIGHT
                            +(roomHeight-coriWidth*UNIT_HEIGHT)/2), 
                    getRenWidth(coriLen*UNIT_WIDTH), 
                    getRenHeight(coriWidth*UNIT_HEIGHT));
        }  
        
        gc.setFill(Color.DARKCYAN);
        if(connections.get(NORTH) == SUB_ROOM &&
                connections.get(WEST) == SUB_ROOM) {
            gc.fillRect(
                    getRenX(room.getPos().getX()), 
                    getRenY(room.getPos().getY()), 
                    getRenWidth(coriLen*UNIT_WIDTH), 
                    getRenHeight(coriLen*UNIT_HEIGHT));
        }
        
        if(connections.get(NORTH) == SUB_ROOM &&
                connections.get(EAST) == SUB_ROOM) {
            gc.fillRect(
                    getRenX(room.getPos().getX()+roomWidth+coriLen*UNIT_WIDTH), 
                    getRenY(room.getPos().getY()), 
                    getRenWidth(coriLen*UNIT_WIDTH), 
                    getRenHeight(coriLen*UNIT_HEIGHT));
        }
        
        if(connections.get(SOUTH) == SUB_ROOM &&
                connections.get(WEST) == SUB_ROOM) {
            gc.fillRect(
                    getRenX(room.getPos().getX()), 
                    getRenY(room.getPos().getY()+roomHeight+coriLen*UNIT_HEIGHT), 
                    getRenWidth(coriLen*UNIT_WIDTH), 
                    getRenHeight(coriLen*UNIT_HEIGHT));
        }
        
        if(connections.get(SOUTH) == SUB_ROOM &&
                connections.get(EAST) == SUB_ROOM) {
            gc.fillRect(
                    getRenX(room.getPos().getX()+roomWidth+coriLen*UNIT_WIDTH), 
                    getRenY(room.getPos().getY()+roomHeight+coriLen*UNIT_HEIGHT), 
                    getRenWidth(coriLen*UNIT_WIDTH), 
                    getRenHeight(coriLen*UNIT_HEIGHT));
        }
        
        gc.setFill(Color.BISQUE);
        gc.fillRect(
                getRenX(room.getPos().getX()+coriLen*UNIT_WIDTH), 
                getRenY(room.getPos().getY()+coriLen*UNIT_HEIGHT), 
                getRenWidth(roomWidth), 
                getRenHeight(roomHeight));
        
        gc.restore();
    }
}