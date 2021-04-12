package org.bioshock.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.bioshock.components.NetworkC;
import org.bioshock.components.RendererC;
import org.bioshock.entities.map.Room;
import org.bioshock.rendering.renderers.Renderer;
import org.bioshock.scenes.SceneManager;
import org.bioshock.utils.Point;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Pair;

public abstract class Entity {
    protected Point position;
    protected Rectangle hitbox;

    protected String uuid = UUID.randomUUID().toString();
    protected double z;
    protected NetworkC networkC;
    protected RendererC rendererC;

    protected boolean enabled = true;

    protected Class<? extends Renderer> renderer;

    protected Entity(Point3D p, Rectangle h, NetworkC netC, RendererC renC) {
        position = new Point(p.getX(), p.getY());

        hitbox = h;
        hitbox.setFill(Color.TRANSPARENT);

        setPosition(position);

        networkC = netC;
        rendererC = renC;

        rendererC.setZ(p.getZ());
    }

    protected abstract void tick(double timeDelta);

    public final void safeTick(double timeDelta) {
        if (enabled) {
            this.tick(timeDelta);
        }
    }

    public boolean intersects(Entity entity) {
        Shape intersects = Shape.intersect(
            this.getHitbox(),
            entity.getHitbox()
        );

        return (intersects.getBoundsInLocal().getWidth() != -1);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setX(double newX) {
        setPosition(newX, getY());
    }

    public void setY(double newY) {
        setPosition(getX(), newY);
    }

    public void setPosition(double x, double y) {
        x = (int) x;
        y = (int) y;

        position.setX(x);
        position.setY(y);

        hitbox.setTranslateX(x + hitbox.getWidth() / 2);
        hitbox.setTranslateY(y + hitbox.getHeight() / 2);
    }

    public void setPosition(Point point) {
        setPosition(point.getX(), point.getY());
    }

    public void setPosition(Point3D point) {
        setPosition(point.getX(), point.getY());
    }

    public void setRenderC(RendererC renderC) {
        this.rendererC = renderC;
    }

    public void setNetworkC(NetworkC component) {
        this.networkC = component;
    }

    public void setID(String newID) {
        uuid = newID;
    }

    public String getID() {
        return uuid;
    }

    public Pair<Point2D, Point2D> getRenderArea() {
        return new Pair<>(
            new Point2D(hitbox.getX(), hitbox.getY()),
            new Point2D(
                hitbox.getX() + hitbox.getWidth(),
                hitbox.getY() + hitbox.getHeight()
            )
        );
    }

    public Point getPosition() {
        return position;
    }

    private Point2D getCentre() {
        double x = getX() + hitbox.getWidth();
        double y = getY() + hitbox.getHeight();
        return new Point2D(x, y);
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public double getZ() {
        return rendererC.getZ();
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public Class<? extends Renderer> getRenderer() {
        return renderer;
    }

    public RendererC getRendererC() {
        return rendererC;
    }

    public NetworkC getNetworkC() {
        return networkC;
    }


    /**
     * Finds the nearest room that this {@code Entity} is in
     * @return the room that this {@code Entity} is in
     */
    public Room getCurrentRoom() {
        List<Room> rooms = SceneManager.getMap().getRooms();

        return Collections.min(rooms, Comparator.comparing(room ->
            room.getRoomCenter().subtract(getCentre()).magnitude()
        ));
    }

    /**
     * Finds the two nearest rooms to this {@code Entity}, used for collisions
     * in-between rooms
     * @return the two nearest rooms to this {@code Entity}
     */
    public Room[] getCurrentRooms() {
        List<Room> rooms = SceneManager.getMap().getRooms();

        Room first = Collections.min(rooms, Comparator.comparing(room ->
            room.getRoomCenter().subtract(getCentre()).magnitude()
        ));

        List<Room> otherRooms = new ArrayList<>(rooms);
        otherRooms.remove(first);

        Room second = Collections.min(otherRooms, Comparator.comparing(room ->
            room.getRoomCenter().subtract(getCentre()).magnitude()
        ));

        return new Room[]{first, second};
    }


    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
