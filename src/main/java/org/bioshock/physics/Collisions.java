package org.bioshock.physics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bioshock.entities.Entity;
import org.bioshock.main.App;

/**
 * Implemented by an {@code Entity} that should have collisions detected
 */
public interface Collisions {
    /**
     * Called every game tick
     * @param delta
     */
    public static void tick(double delta) {
        updateCollisions();
        CollisionUtils.collidables.forEach(entity -> {
            Set<Entity> collisions = CollisionUtils.collisions.get(entity);

            ((Collisions) entity).collisionTick(collisions);
        });
    }


    /**
     * Updates {@link CollisionUtils#collisions} with each {@code Entity}'s new
     * {@code Set} of collisions
     */
    private static void updateCollisions() {
        CollisionUtils.collidables.forEach(entity -> {
            Set<Entity> newCollisions = CollisionUtils.collidables.stream()
                .filter(entity::intersects)
                .collect(Collectors.toCollection(HashSet::new));

            entity.find4ClosestRooms().forEach(room ->
                newCollisions.addAll(room.getWalls().stream()
                    .filter(entity::intersects)
                    .collect(Collectors.toSet())
                )
            );

            CollisionUtils.collisions.replace(entity, newCollisions);
        });
    }


    /**
     * To be called by every {@code Entity}'s constructor that should have
     * collisions detected
     * @param entity The {@code Entity} that should have collisions detected
     */
    public default void initCollision(Collisions entity) {
        if (!(entity instanceof Entity)) {
            App.logger.error("Collisions should only by implemented by Entities");
        }
        CollisionUtils.collidables.add((Entity) entity);
        CollisionUtils.collisions.putIfAbsent((Entity) entity, Set.of());
    }


    /**
     * A method defined by each {@code Entity} that handles collisions
     * @param collisions A {@code Set} of entities this {@code Entity} collided
     * with within this game tick
     */
    public void collisionTick(Set<Entity> collisions);


    /**
     * A collection of fields to be used in this interface
     */
    static class CollisionUtils {
        private CollisionUtils() {}


        /**
         * Maps each {@code Entity} to a {@code Set} of entities it has
         * collided with this game tick
         */
        private static Map<Entity, Set<Entity>> collisions = new HashMap<>();


        /**
         * A {@code Set} of each {@code Entity} to be checked for collisions
         */
        private static Set<Entity> collidables = new HashSet<>();
    }
}
