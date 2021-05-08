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
    /**
     * The default Z value
     */
    protected static final int Z = 10;

    /**
     * The default width/height
     */
    protected static final int DEFAULT_SIZE = 50;

    /**
     * Rooms that contain loot
     */
    private static Set<Room> roomsWithLoot = new HashSet<>();


    /**
     * @param position Position of this {@link Entity}
     * @param size Desired size of {@link Entity}. If null, will be inferred
     * from size of {@link #image}
     * @param networkComponent This {@link Entity Entity's} {@link #networkC}
     * @param path Path to the image for this {@link Entity}
     */
    protected Item(
        Point3D position,
        Size size,
        NetworkC networkComponent,
        String path
    ) {
        super(position, size, networkComponent, path);

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
    protected void collect(Hider hider) {
        apply(hider);
        playCollectSound();
        destroy();
    }


    @Override
    protected void tick(double timeDelta) {
        /* Items do not change */
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
            if (roomsWithLoot.size() == rooms.size()) {
                App.logger.error("Too many loot items being created");
            }

            int roomIndex = random.nextInt(rooms.size());

            room = rooms.get(roomIndex);
        } while (roomsWithLoot.contains(room));

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

        roomsWithLoot.add(room);

        Point2D lootCentre = traversableArray[i][j].getLocation();
        double x = lootCentre.getX() - DEFAULT_SIZE / 2f;
        double y = lootCentre.getY() - DEFAULT_SIZE / 2f;

        return new Point3D(x, y, Z);
    }


    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
