package org.bioshock.rendering.renderers;

import static org.bioshock.entities.map.utils.ConnType.ROOM_TO_ROOM;
import static org.bioshock.entities.map.utils.ConnType.SUB_ROOM;
import static org.bioshock.rendering.RenderManager.getRenHeight;
import static org.bioshock.rendering.RenderManager.getRenWidth;
import static org.bioshock.rendering.RenderManager.getRenX;
import static org.bioshock.rendering.RenderManager.getRenY;
import static org.bioshock.utils.Direction.EAST;
import static org.bioshock.utils.Direction.NORTH;
import static org.bioshock.utils.Direction.SOUTH;
import static org.bioshock.utils.Direction.WEST;
import static org.bioshock.utils.GlobalConstants.UNIT_HEIGHT;
import static org.bioshock.utils.GlobalConstants.UNIT_WIDTH;

import java.util.Map;

import org.bioshock.entities.map.Room;
import org.bioshock.entities.map.RoomEntity;
import org.bioshock.entities.map.utils.ConnType;
import org.bioshock.utils.Direction;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RoomRenderer implements Renderer {
    private RoomRenderer() {}

    public static <E extends RoomEntity> void render(
        GraphicsContext gc,
        E roomEntity
    ) {
        Room room = roomEntity.getRoom();
        Map<Direction, ConnType> connections = room.getConnections();
        double coriLen = room.getCoriSize().getHeight(); //in terms of units
        double coriWidth = room.getCoriSize().getWidth(); //in terms of units
        double roomWidth = room.getRoomSize().getWidth();
        double roomHeight = room.getRoomSize().getHeight();

        gc.save();
        gc.setFill(Color.RED);
        if (connections.get(NORTH) == SUB_ROOM) {
            gc.fillRect(
                getRenX(room.getPosition().getX() + coriLen * UNIT_WIDTH),
                getRenY(room.getPosition().getY()),
                getRenWidth(roomWidth),
                getRenHeight(coriLen * UNIT_HEIGHT)
            );
        }
        else if (connections.get(NORTH) == ROOM_TO_ROOM) {
            gc.fillRect(
                getRenX(
                    room.getPosition().getX()
                    + coriLen * UNIT_WIDTH
                    + (roomWidth - coriWidth * UNIT_WIDTH) / 2
                ),
                getRenY(room.getPosition().getY()),
                getRenWidth(coriWidth * UNIT_WIDTH),
                getRenHeight(coriLen * UNIT_HEIGHT)
            );
        }

        gc.setFill(Color.BLUE);
        if (connections.get(SOUTH) == SUB_ROOM) {
            gc.fillRect(
                getRenX(room.getPosition().getX() + coriLen * UNIT_WIDTH),
                getRenY(
                    room.getPosition().getY()
                    + roomHeight + coriLen * UNIT_HEIGHT
                ),
                getRenWidth(roomWidth),
                getRenHeight(coriLen * UNIT_HEIGHT)
            );
        }
        else if (connections.get(SOUTH) == ROOM_TO_ROOM) {
            gc.fillRect(
                getRenX(
                    room.getPosition().getX()
                    +coriLen * UNIT_WIDTH
                    +(roomWidth - coriWidth * UNIT_WIDTH) / 2
                ),
                getRenY(
                    room.getPosition().getY()
                    + roomHeight + coriLen * UNIT_HEIGHT
                ),
                getRenWidth(coriWidth * UNIT_WIDTH),
                getRenHeight(coriLen * UNIT_HEIGHT)
            );
        }


        gc.setFill(Color.YELLOW);
        if (connections.get(WEST) == SUB_ROOM) {
            gc.fillRect(
                getRenX(room.getPosition().getX()),
                getRenY(room.getPosition().getY() + coriLen * UNIT_HEIGHT),
                getRenWidth(coriLen * UNIT_WIDTH),
                getRenHeight(roomHeight)
            );
        }
        else if (connections.get(WEST) == ROOM_TO_ROOM) {
            gc.fillRect(
                getRenX(room.getPosition().getX()),
                getRenY(
                    room.getPosition().getY()
                    +coriLen * UNIT_HEIGHT
                    +(roomHeight - coriWidth * UNIT_HEIGHT) / 2
                ),
                getRenWidth(coriLen * UNIT_WIDTH),
                getRenHeight(coriWidth * UNIT_HEIGHT)
            );
        }


        gc.setFill(Color.GREEN);
        if (connections.get(EAST) == SUB_ROOM) {
            gc.fillRect(
                getRenX(
                    room.getPosition().getX()
                    + roomWidth + coriLen * UNIT_WIDTH
                ),
                getRenY(room.getPosition().getY() + coriLen * UNIT_HEIGHT),
                getRenWidth(coriLen * UNIT_WIDTH),
                getRenHeight(roomHeight)
            );
        }
        else if (connections.get(EAST) == ROOM_TO_ROOM) {
            gc.fillRect(
                getRenX(
                    room.getPosition().getX()
                    + roomWidth + coriLen * UNIT_WIDTH
                ),
                getRenY(
                    room.getPosition().getY()
                    + coriLen * UNIT_HEIGHT
                    + (roomHeight - coriWidth * UNIT_HEIGHT) / 2
                ),
                getRenWidth(coriLen * UNIT_WIDTH),
                getRenHeight(coriWidth * UNIT_HEIGHT)
            );
        }

        gc.setFill(Color.DARKCYAN);
        if (
            connections.get(NORTH) == SUB_ROOM
            && connections.get(WEST) == SUB_ROOM
        ) {
            gc.fillRect(
                getRenX(room.getPosition().getX()),
                getRenY(room.getPosition().getY()),
                getRenWidth(coriLen * UNIT_WIDTH),
                getRenHeight(coriLen * UNIT_HEIGHT)
            );
        }

        if (
            connections.get(NORTH) == SUB_ROOM
            && connections.get(EAST) == SUB_ROOM
        ) {
            gc.fillRect(
                getRenX(
                    room.getPosition().getX()
                    + roomWidth + coriLen * UNIT_WIDTH
                ),
                getRenY(room.getPosition().getY()),
                getRenWidth(coriLen * UNIT_WIDTH),
                getRenHeight(coriLen * UNIT_HEIGHT)
            );
        }

        if (
            connections.get(SOUTH) == SUB_ROOM
            && connections.get(WEST) == SUB_ROOM
        ) {
            gc.fillRect(
                getRenX(room.getPosition().getX()),
                getRenY(
                    room.getPosition().getY()
                    + roomHeight + coriLen * UNIT_HEIGHT
                ),
                getRenWidth(coriLen * UNIT_WIDTH),
                getRenHeight(coriLen * UNIT_HEIGHT)
            );
        }

        if (connections.get(SOUTH) == SUB_ROOM &&
                connections.get(EAST) == SUB_ROOM) {
            gc.fillRect(
                getRenX(
                    room.getPosition().getX()
                    + roomWidth + coriLen * UNIT_WIDTH
                ),
                getRenY(
                    room.getPosition().getY()
                    + roomHeight + coriLen * UNIT_HEIGHT
                ),
                getRenWidth(coriLen * UNIT_WIDTH),
                getRenHeight(coriLen * UNIT_HEIGHT)
            );
        }

        gc.setFill(Color.BISQUE);
        gc.fillRect(
            getRenX(room.getPosition().getX() + coriLen * UNIT_WIDTH),
            getRenY(room.getPosition().getY() + coriLen * UNIT_HEIGHT),
            getRenWidth(roomWidth),
            getRenHeight(roomHeight)
        );

        gc.restore();
    }
}
