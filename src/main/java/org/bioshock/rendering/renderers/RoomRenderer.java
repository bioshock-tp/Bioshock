package org.bioshock.rendering.renderers;

import static org.bioshock.rendering.RenderManager.getRenHeight;
import static org.bioshock.rendering.RenderManager.getRenWidth;
import static org.bioshock.rendering.RenderManager.getRenX;
import static org.bioshock.rendering.RenderManager.getRenY;
import static org.bioshock.utils.GlobalConstants.UNIT_HEIGHT;
import static org.bioshock.utils.GlobalConstants.UNIT_WIDTH;

import org.bioshock.entities.map.Room;
import org.bioshock.entities.map.RoomEntity;
import org.bioshock.utils.GlobalConstants;

import javafx.scene.canvas.GraphicsContext;

public class RoomRenderer implements Renderer {
    private RoomRenderer() {}

    /**
     * Method to render the given roomEntity
     * @param <E>
     * @param gc The graphics context to render the roomEntity on
     * @param roomEntity The roomEntity to render
     */
    public static <E extends RoomEntity> void render(
        GraphicsContext gc,
        E roomEntity
    ) {
        Room room = roomEntity.getRoom();
        boolean[][] floorSpace = room.getFloorSpace();

        gc.save();

        //At every floor location render the floor image
        for (int i = 0; i < floorSpace.length; i++) {
            for (int j = 0; j < floorSpace[i].length; j++) {
                if (floorSpace[i][j]) {
                    gc.drawImage(
                        GlobalConstants.FLOOR_IMAGE,
                        getRenX(room.getPosition().getX() + j * UNIT_WIDTH),
                        getRenY(room.getPosition().getY() + i * UNIT_HEIGHT),
                        getRenWidth(UNIT_WIDTH),
                        getRenHeight(UNIT_HEIGHT)
                    );
                }
            }
        }

        gc.restore();
    }
}
