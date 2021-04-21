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
import org.bioshock.utils.ArrayUtils;
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
    protected boolean alwaysRender = false;

    public boolean isAlwaysRender() {
        return alwaysRender;
    }

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

    /**
     *
     * Finds the closest room to the given {@code Point2D}, using the given
     * list of rooms
     * @param point the coordinates to find the closest room to
     * @param rooms the list of in-game rooms
     * @return The room that is closest to the given point
     */
    private static Room roomOf(Point2D point, List<Room> rooms) {
        return Collections.min(rooms, Comparator.comparing(room ->
            room.getRoomCenter().subtract(point).magnitude()
        ));
    }

    /**
     *
     * Finds the closest room to the given {@code Point2D}
     * @param point the coordinates to find the closest room to
     * @return The room that is closest to the given point
     */
    protected static Room roomOf(Point2D point) {
        return roomOf(point, SceneManager.getMap().getRooms());
    }

    public final void safeTick(double timeDelta) {
        if (enabled) {
            this.tick(timeDelta);
        }
    }

    public boolean intersects(Entity entity) {
        if (entity == this) return false;

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

    public Rectangle getRenderArea() {
        return new Rectangle(
            position.getX(),
            position.getY(),
            hitbox.getWidth(),
            hitbox.getHeight());
    }

    public Point getPosition() {
        return position;
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
     *
     * Finds the current room that an entity is in
     *
     * @param entity the entity to find current room of
     * @return the current room of the entity
     */
    public Room findCurrentRoom() {
        return findCurrentRoom(this.getPosition());
    }


    /**
     *
     * Finds the current room that a position is in
     *
     * @param pos the point to find current room of
     * @return the current room of the point
     */
    public static Room findCurrentRoom(Point2D pos) {
        Room[][] current = SceneManager.getMap().getRoomArray();
        Room temp = SceneManager.getMap().getRooms().get(0);
        double tRoomWidth = temp.getTotalSize().getWidth();
        double tRoomHeight = temp.getTotalSize().getHeight();

        int i = (int) Math.floor(pos.getY() / tRoomHeight);
        int j = (int) Math.floor(pos.getX() / tRoomWidth);

        return current[i][j];
    }


    /**
     * @return the current room and the 2 rooms that you are closest too and then the room those 2 are connected too
     *
     * OXXO
     * OXYO
     * OOOO
     *
     * so if you were in the top left of room Y it would return all rooms X and Y
     *
     * OOOO
     * OOYX
     * OOXX
     *
     * if you were in the bottom right of room Y
     *
     * OXYO
     * OOOO
     * OOOO
     * if you were in the top left of room y
     * (in this case only two rooms would be in the list)
     */
    public List<Room> find4ClosestRooms() {
        return find4ClosestRooms(this.getPosition());
    }


    /**
     *
     * @param pos the position you want the 4 rooms to be close too
     * @return the current room and the 2 rooms that you are closest too and then the room those 2 are connected too
     *
     * OXXO
     * OXYO
     * OOOO
     *
     * so if you were in the top left of room Y it would return all rooms X and Y
     *
     * OOOO
     * OOYX
     * OOXX
     *
     * if you were in the bottom right of room Y
     *
     * OXYO
     * OOOO
     * OOOO
     * if you were in the top left of room y
     * (in this case only two rooms would be in the list)
     */
    public static List<Room> find4ClosestRooms(Point2D pos){
        Room[][] current = SceneManager.getMap().getRoomArray();
        Room temp = SceneManager.getMap().getRooms().get(0);
        double tRoomWidth = temp.getTotalSize().getWidth();
        double tRoomHeight = temp.getTotalSize().getHeight();

        int i = (int) Math.round(pos.getY() / tRoomHeight);
        int j = (int) Math.round(pos.getX() / tRoomWidth);
        List<Pair<Integer, Integer>> coords = new ArrayList<>();
        coords.add(new Pair<>(i, j));
        coords.add(new Pair<>(i - 1, j));
        coords.add(new Pair<>(i, j - 1));
        coords.add(new Pair<>(i - 1, j - 1));

        List<Room> rooms = new ArrayList<>();
        for (Pair<Integer, Integer> coord : coords) {
            if (ArrayUtils.safeGet(current, coord.getKey(), coord.getValue()) != null) {
                rooms.add(ArrayUtils.safeGet(current, coord.getKey(), coord.getValue()));
            }
        }
        return rooms;
    }


    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
