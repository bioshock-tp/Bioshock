package org.bioshock.entities.items;

import org.bioshock.components.NetworkC;
import org.bioshock.entities.Entity;
import org.bioshock.entities.ImageEntity;
import org.bioshock.utils.Size;

import javafx.geometry.Point3D;

public abstract class Item extends ImageEntity {

    /**
     *
     * @param p
     * @param s Desired size of {@code Entity}. If null, will be inferred
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

    protected abstract void apply(Entity entity);

    public void collect(Entity entity) {
        if (!enabled) return;
        apply(entity);
        destroy();
    }
}
