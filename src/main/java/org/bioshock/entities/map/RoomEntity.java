package org.bioshock.entities.map;

import org.bioshock.components.NetworkC;
import org.bioshock.entities.Entity;
import org.bioshock.rendering.renderers.RoomRenderer;
import org.bioshock.rendering.renderers.components.SimpleRendererC;

import javafx.geometry.Point3D;
import javafx.scene.shape.Rectangle;

/**
 * 
 * A wrapper entity for a Room
 * 
 * This is needed as Room already extends GraphNode so to have it extend entity 
 * it needs a wrapper class
 *
 */
public class RoomEntity extends Entity{
    /**
     * The room this RoomEntity is a wrapper for
     */
    Room room;

    /**
     * 
     * @return The room this RoomEntity is a wrapper for
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Construct a new RoomEntity with it's position being the top left of the room and a z value of 0
     * @param room The room this is going to be a wrapper for
     */
    public RoomEntity(Room room) {
        super(
            new Point3D(
                room.getPosition().getX(),
                room.getPosition().getY(),
                0
            ),
            new Rectangle(0, 0),
            new NetworkC(false),
            new SimpleRendererC()
        );

        this.room = room;
        renderer = RoomRenderer.class;
    }

    @Override
    protected void tick(double timeDelta) {
        /* This entity does not change */
    }

    @Override
    public Rectangle getRenderArea() {
        return new Rectangle(
            room.getPosition().getX(),
            room.getPosition().getY(),
            room.getTotalSize().getWidth(),
            room.getTotalSize().getHeight()
        );
    }
}
