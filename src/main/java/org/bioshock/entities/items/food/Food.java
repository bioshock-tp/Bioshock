package org.bioshock.entities.items.food;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bioshock.components.NetworkC;
import org.bioshock.entities.Entity;
import org.bioshock.entities.items.Item;
import org.bioshock.entities.map.Room;
import org.bioshock.main.App;
import org.bioshock.scenes.MainGame;
import org.bioshock.scenes.SceneManager;
import org.bioshock.utils.Size;

import javafx.geometry.Point3D;

public abstract class Food extends Item {
    private static final int Z = 10;
    private static final int SIZE = 50;
    private static final Random random = new Random();

    private static Set<Room> roomsWithFood = new HashSet<>();

    /**
     * Creates a room in a random room in a random location
     * @param path Path to this entities image file
     */
    protected Food(String path) {
        super(
            spawn(),
            new Size(SIZE, SIZE),
            new NetworkC(true),
            path
        );
    }

    /**
     * Gets random location in a random room
     * @return
     */
    private static Point3D spawn() {
        List<Room> rooms = SceneManager.getMap().getRooms();

        Room room;
        do {
            int roomIndex = random.nextInt(rooms.size() - 1);

            room = rooms.get(roomIndex);

            if (roomsWithFood.size() == rooms.size()) {
                App.logger.error("Too many food items being created");
                break;
            }
        } while (roomsWithFood.contains(room));

        Size bounds = room.getRoomSize();

        int maxXOffset = (int) bounds.getWidth() / 2 - SIZE;
        int minXOffset = -maxXOffset;
        int xOffset = random.nextInt(maxXOffset - minXOffset) + minXOffset;

        int maxYOffset = (int) bounds.getHeight() / 2 - SIZE;
        int minYOffset = -maxYOffset;
        int yOffset = random.nextInt(maxYOffset - minYOffset) + minYOffset;

        Point3D centre = room.getRoomCenter();

        int x = (int) centre.getX() + xOffset;
        int y = (int) centre.getY() + yOffset;

        roomsWithFood.add(room);

        return new Point3D(x, y, Z);
    }

    @Override
    protected void apply(Entity entity) {
        ((MainGame) SceneManager.getScene()).collectFood();
    }

    @Override
    protected void tick(double timeDelta) {
        //TODO: insert collision tick
    }

}
