package org.bioshock.entities.items;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bioshock.components.NetworkC;
import org.bioshock.engine.pathfinding.GraphNode;
import org.bioshock.entities.Entity;
import org.bioshock.entities.EntityManager;
import org.bioshock.entities.ImageEntity;
import org.bioshock.entities.map.Room;
import org.bioshock.entities.players.Hider;
import org.bioshock.main.App;
import org.bioshock.physics.Collisions;
import org.bioshock.scenes.SceneManager;
import org.bioshock.utils.Size;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

public abstract class Item extends ImageEntity implements Collisions {
    protected static final int Z = 10;
    protected static final int DEFAULT_SIZE = 50;

    private static Set<Room> roomsWithFood = new HashSet<>();


    /**
     *
     * @param p
     * @param s Desired size of {@code Entity}. If null, will be inferred from
     * size of {@link #image}
     * @param nC
     * @param path
     */
    protected Item(
        Point3D p,
        Size s,
        NetworkC nC,
        String path
    ) {
        super(p, s, nC, path);

        initCollision(this);
    }


    /**
     * The effect of collecting this item
     * @param hider The {@link Hider} to apply the effect to
     */
    protected abstract void apply(Hider hider);


    /**
     * Called when an {@link Hider} walks over this item
     * @param hider The hider that collected this item
     */
    public void collect(Hider hider) {
        apply(hider);
        playCollectSound();
        destroy();
    }


    @Override
    public void collisionTick(Set<Entity> collisions) {
        if (!enabled) return;
        collisions.forEach(collision -> {
            if (EntityManager.getPlayers().contains(collision)) {
                collect((Hider) collision);
            }
        });
    }


    /**
     * Plays a sound when collecting this item
     */
    protected abstract void playCollectSound();


    /**
     * Gets random location in a random {@code Room}
     * @return A random {@code Point3D} in a random {@code Room}
     */
    protected static Point3D spawn(long seed) {
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

        GraphNode[][] traversableArray = room.getTraversableArray();
        Set<GraphNode> traversableNodes = room.getTraversableGraph().getNodes();

        int i;
        int j;

        do {
            i = random.nextInt(traversableArray.length);
            j = random.nextInt(traversableArray[0].length);
        }
        while (
            traversableArray[i][j] == null
            || !traversableNodes.contains(traversableArray[i][j])
        );

        roomsWithFood.add(room);

        Point2D foodCentre = traversableArray[i][j].getLocation();
        double x = foodCentre.getX() - DEFAULT_SIZE / 2f;
        double y = foodCentre.getY() - DEFAULT_SIZE / 2f;

        return new Point3D(x, y, Z);
    }


    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
