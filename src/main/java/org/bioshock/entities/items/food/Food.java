package org.bioshock.entities.items.food;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bioshock.audio.AudioManager;
import org.bioshock.components.NetworkC;
import org.bioshock.engine.pathfinding.GraphNode;
import org.bioshock.entities.items.Item;
import org.bioshock.entities.map.Room;
import org.bioshock.entities.players.Hider;
import org.bioshock.main.App;
import org.bioshock.scenes.SceneManager;
import org.bioshock.utils.Size;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

public abstract class Food extends Item {
    private static final int Z = 10;
    private static final int SIZE = 50;

    private static Set<Room> roomsWithFood = new HashSet<>();


    /**
     * Creates a room in a random room in a random location
     * @param path Path to this entities image file
     */
    protected Food(String path, long seed) {
        super(
            spawn(seed),
            new Size(SIZE, SIZE),
            new NetworkC(true),
            path
        );

        initCollision(this);
    }


    /**
     * Gets random location in a random {@code Room}
     * @return A random {@code Point3D} in a random {@code Room}
     */
    private static Point3D spawn(long seed) {
        Random random = new Random(seed);

        List<Room> rooms = SceneManager.getMap().getRooms();

        Room room = null;
        do {
            if (roomsWithFood.size() == rooms.size()) {
                App.logger.error("Too many food items being created");
            }

            int roomIndex = random.nextInt(rooms.size());

            room = rooms.get(roomIndex);
        } while (roomsWithFood.contains(room));

        GraphNode[][] traversable = room.getTraversableArray();
        int i;
        int j;

        do {
            i = random.nextInt(traversable.length);
            j = random.nextInt(traversable[0].length);
        }
        while (traversable[i][j] == null);

        roomsWithFood.add(room);

        Point2D foodCentre = traversable[i][j].getLocation();
        double x = foodCentre.getX() - SIZE / 2f;
        double y = foodCentre.getY() - SIZE / 2f;

        return new Point3D(x, y, Z);
    }


    @Override
    protected void apply(Hider hider) {
        SceneManager.getMainGame().collectFood(hider);
    }


    @Override
    protected void playCollectSound() {
        AudioManager.playPlinkSfx();
    }

    protected void tick(double timeDelta) {}
}
