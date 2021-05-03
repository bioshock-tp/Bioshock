package org.bioshock.entities.items;

import java.util.Set;

import org.bioshock.components.NetworkC;
import org.bioshock.entities.Entity;
import org.bioshock.entities.EntityManager;
import org.bioshock.entities.ImageEntity;
import org.bioshock.entities.players.Hider;
import org.bioshock.physics.Collisions;
import org.bioshock.utils.Size;

import javafx.geometry.Point3D;

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
     * @param hider The {@link Hider} to apply the effect to
     */
    protected abstract void apply(Hider hider);


    /**
     * Called when an {@link Hider} walks over this item
     * @param entity The entity that collected this item
     */
    public void collect(Hider hider) {
        if (!enabled) return;
        apply(hider);
        playCollectSound();
        destroy();
    }


    @Override
    public void collisionTick(Set<Entity> collisions) {
        collisions.forEach(collision -> {
            if (EntityManager.getPlayers().contains(collision)) {
                collect((Hider) collision);
            }
        });
    }


    /**
     * The sound effect to play when collecting this item
     */
    protected abstract void playCollectSound();


    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
