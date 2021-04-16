package org.bioshock.entities.map;

import org.bioshock.components.NetworkC;
import org.bioshock.entities.Entity;
import org.bioshock.rendering.renderers.RoomRenderer;
import org.bioshock.rendering.renderers.components.SimpleRendererC;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

public class RoomEntity extends Entity{
    Room room;

    public Room getRoom() {
        return room;
    }

    public RoomEntity(Room room) {
        super(room.getPos().subtract(0,0,5), new Rectangle(0,0), new NetworkC(false), new SimpleRendererC());
        
        this.room = room;
        renderer = RoomRenderer.class;
    }

    @Override
    protected void tick(double timeDelta) {}

    @Override
    public Rectangle getRenderArea() {
        return new Rectangle(
            room.getPos().getX(),
            room.getPos().getY(), 
            room.getTotalSize().getWidth(), 
            room.getTotalSize().getHeight()
        );
    }
}