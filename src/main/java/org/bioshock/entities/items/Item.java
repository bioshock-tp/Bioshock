package org.bioshock.entities.items;

import javafx.geometry.Point3D;
import org.bioshock.components.NetworkC;
import org.bioshock.entities.Entity;
import org.bioshock.entities.EntityManager;
import org.bioshock.entities.ImageEntity;
import org.bioshock.physics.Collisions;
import org.bioshock.utils.Size;

import java.util.Set;

public abstract class Item extends ImageEntity implements Collisions {

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
    }


    /**
     * The effect of collecting this item
     * @param entity The {@code Entity} to apply the effect to
     */
    protected abstract void apply(Entity entity);


    /**
     * Called when an {@code Entity} walks over this item
     * @param entity The entity that collected this item
     */
    public void collect(Entity entity) {
        if (!enabled) return;
        apply(entity);
        playCollectSound();
        destroy();
    }


    @Override
    public void collisionTick(Set<Entity> collisions) {
        collisions.forEach(collision -> {
            if (EntityManager.getPlayers().contains(collision)) {
                collect(collision);
            }
        });
    }


    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    /**
     * The sound effect to play when collecting this item
     */
    protected abstract void playCollectSound();
}
