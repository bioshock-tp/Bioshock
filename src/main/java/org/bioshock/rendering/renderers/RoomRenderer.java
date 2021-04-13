package org.bioshock.rendering.renderers;

import static org.bioshock.rendering.RenderManager.getRenHeight;
import static org.bioshock.rendering.RenderManager.getRenWidth;
import static org.bioshock.rendering.RenderManager.getRenX;
import static org.bioshock.rendering.RenderManager.getRenY;

import org.bioshock.entities.map.Room;
import org.bioshock.entities.map.RoomEntity;

import javafx.scene.canvas.GraphicsContext;

public class RoomRenderer implements Renderer {
    public static <E extends RoomEntity> void render(
            GraphicsContext gc,
            E roomEntity
    ) {
        Room room = roomEntity.getRoom();
        gc.save();
        gc.setFill(room.getColor().grayscale());
        gc.fillRect(
            getRenX(room.getPos().getX()), 
            getRenY(room.getPos().getY()), 
            getRenWidth(room.getTotalSize().getWidth()), 
            getRenHeight(room.getTotalSize().getHeight()));
        
        gc.restore();
    }
}
