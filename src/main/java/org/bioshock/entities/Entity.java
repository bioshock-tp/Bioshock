package org.bioshock.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bioshock.components.NetworkC;
import org.bioshock.entities.map.Room;
import org.bioshock.rendering.renderers.Renderer;
import org.bioshock.rendering.renderers.components.RendererC;
import org.bioshock.scenes.SceneManager;
import org.bioshock.utils.ArrayUtils;
import org.bioshock.utils.Point;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Pair;

/**
 * A class representing a generic entity in the game
 */
public abstract class Entity {
	/**
	 * The 2d position of the entity
	 */
    protected Point position;

    /**
     * The entities hitbox
     */
    protected Rectangle hitbox;

    /**
     * The unique ID of the entity
     */
    protected String uuid = UUID.randomUUID().toString();

    /**
     * The entities network component
     */
    protected NetworkC networkC;

    /**
     * The entities render component
     */
    protected RendererC rendererC;

    /**
     * Boolean to represent whether to always render the current
     * entity or not no matter it's renderArea
     */
    protected boolean alwaysRender = false;

    /**
     * Boolean to represent if the current entity is enabled or not
     */
    protected boolean enabled = true;

    /**
     * The renderer that the entity is rendered with
     */
    protected Class<? extends Renderer> renderer;


    /**
     * @param position the position of the entity
     * @param hitbox the hitbox of the entity
     * @param networkComponent the network component of the entity
     * @param renderComponent the renderComponent of the entity
     */
    protected Entity(
        Point3D position,
        Rectangle hitbox,
        NetworkC networkComponent,
        RendererC renderComponent
    ) {
    	//set the current position in 2d
        this.position = new Point(position.getX(), position.getY());

        this.hitbox = hitbox;
        this.hitbox.setFill(Color.TRANSPARENT);

        setPosition(position);

        this.networkC = networkComponent;
        this.rendererC = renderComponent;

        this.rendererC.setZ(position.getZ());
    }


    /**
     * The method to update the current entity
     * @param timeDelta the amount of time to update the entity in seconds
     */
    protected abstract void tick(double timeDelta);


    /**
     * Method that calls tick on the entity only if the entity is enabled
     * @param timeDelta
     */
    public final void safeTick(double timeDelta) {
        if (enabled) {
            this.tick(timeDelta);
        }
    }


    /**
     * Checks if two entities intersect visually
     * @param entity The {@link Entity} to check the intersection with
     * @return True if the {@link Entity Entity's} hitboxes intersect,
     * false if provided {@code entity} is {@code this}
     */
    public boolean intersects(Entity entity) {
        return entity != this && intersects(entity.getHitbox());
    }


    /**
     * Checks if {@code this} intersects with a shape visually
     * @param shape The {@link Shape} to check the intersection with
     * @return True if this {@link Entity Entity's} hitbox intersects with the
     * {@link Shape}
     */
    public boolean intersects(Shape shape) {
        Shape intersects = Shape.intersect(
            this.getHitbox(),
            shape
        );

        return (intersects.getBoundsInLocal().getWidth() != -1);
    }


    /**
     * @return True if this {@link Entity} is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }


    /**
     * @param newX the new X coordinate of this {@link Entity}
     */
    public void setX(double newX) {
        setPosition(newX, getY());
    }


    /**
     * @param newY the new Y coordinate this {@link Entity}
     */
    public void setY(double newY) {
        setPosition(getX(), newY);
    }


    /**
     * Sets the current position of the entity
     * @param x the new x coordinate
     * @param y the new y coordinate
     */
    public void setPosition(double x, double y) {
        x = (int) x;
        y = (int) y;

        position.setX(x);
        position.setY(y);

        hitbox.setTranslateX(x + hitbox.getWidth() / 2);
        hitbox.setTranslateY(y + hitbox.getHeight() / 2);
    }


    /**
     * Sets the current position of the entity
     * @param point the new position of this entity
     */
    public void setPosition(Point point) {
        setPosition(point.getX(), point.getY());
    }


    /**
     * Sets the current position of the entity
     * @param point the new position of this entity
     */
    public void setPosition(Point3D point) {
        setPosition(point.getX(), point.getY());
    }


    /**
     * @param enabled True if this {@link Entity} should be {@link #enabled}
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    /**
     * @param renderC the new {@link #rendererC} for this {@link Entity}
     */
    public void setRenderC(RendererC renderC) {
        this.rendererC = renderC;
    }


    /**
     * @param networkC the new {@link #networkC} for this {@link Entity}
     */
    public void setNetworkC(NetworkC networkC) {
        this.networkC = networkC;
    }


    /**
     * @param newID the new Unique ID of this entity
     */
    public void setID(String newID) {
        uuid = newID;
    }


    /**
     * @return the Unique ID of this entity
     */
    public String getID() {
        return uuid;
    }


    /**
     * @return the area an entity would effect when rendered
     *
     * this is used to speed up rendering by not attempting
     * to render things that are entirely off screen
     */
    public Rectangle getRenderArea() {
        return new Rectangle(
            position.getX(),
            position.getY(),
            hitbox.getWidth(),
            hitbox.getHeight()
        );
    }


    /**
     * @return the current position of this {@link Entity}
     */
    public Point getPosition() {
        return position;
    }


    /**
     * @return the current X coordinate
     */
    public double getX() {
        return position.getX();
    }


    /**
     * @return the current Y coordinate
     */
    public double getY() {
        return position.getY();
    }


    /**
     * @return the current Z coordinate
     */
    public double getZ() {
        return rendererC.getZ();
    }


    /**
     * @return this {@link Entity} hitbox
     */
    public Rectangle getHitbox() {
        return hitbox;
    }


    /**
     * @return The width of {@link Entity}
     */
    public double getWidth() {
        return hitbox.getWidth();
    }


    /**
     * @return The height of {@link Entity}
     */
    public double getHeight() {
        return hitbox.getHeight();
    }


    /**
     * @return The centre of this {@link Entity}
     * i.e. new Point(getX() + getWidth() / 2, getY() + getHeight() / 2)
     */
    public Point getCentre() {
        return new Point(getX() + getWidth() / 2, getY() + getHeight() / 2);
    }


    /**
     * @return this {@link Entity} {@link #renderer} class
     */
    public Class<? extends Renderer> getRenderer() {
        return renderer;
    }


    /**
     * @return this {@link Entity} {@link #rendererC}
     */
    public RendererC getRendererC() {
        return rendererC;
    }


    /**
     * @return this {@link Entity} {@link #networkC}
     */
    public NetworkC getNetworkC() {
        return networkC;
    }


    /**
     * @return True if this {@link Entity} should always render
     */
    public boolean alwaysRender() {
        return alwaysRender;
    }


    /**
     *
     * Finds the current room that this {@link Entity} is in
     * @return the current room of this {@link Entity}
     */
    public Room findCurrentRoom() {
        return findCurrentRoom(this.getPosition());
    }


    /**
     * Finds the current room that a position is in
     * @param position the point to find current room of
     * @return the current room of the point
     */
    public static Room findCurrentRoom(Point2D position) {
        Room[][] current = SceneManager.getMap().getRoomArray();
        Room temp = SceneManager.getMap().getRooms().get(0);
        double tRoomWidth = temp.getTotalSize().getWidth();
        double tRoomHeight = temp.getTotalSize().getHeight();

        int i = (int) Math.floor(position.getY() / tRoomHeight);
        int j = (int) Math.floor(position.getX() / tRoomWidth);

        return current[i][j];
    }


    /**
     * @return The current room this {@link Entity} is in and the four rooms
     * adjacent to it (if applicable)
     */
    public List<Room> find4ClosestRooms() {
        return find4ClosestRooms(this.getPosition());
    }


    /**
     * @param position The position to find the room and adjacent rooms of
     * @return The current room of the given position and the four rooms
     * adjacent to it (if applicable)
     */
    public static List<Room> find4ClosestRooms(Point2D position) {
        Room[][] current = SceneManager.getMap().getRoomArray();
        Room temp = SceneManager.getMap().getRooms().get(0);
        double tRoomWidth = temp.getTotalSize().getWidth();
        double tRoomHeight = temp.getTotalSize().getHeight();

        //Get the indices of the 4 closest rooms and add them to coords
        int i = (int) Math.round(position.getY() / tRoomHeight);
        int j = (int) Math.round(position.getX() / tRoomWidth);
        List<Pair<Integer, Integer>> coords = new ArrayList<>();
        coords.add(new Pair<>(i, j));
        coords.add(new Pair<>(i - 1, j));
        coords.add(new Pair<>(i, j - 1));
        coords.add(new Pair<>(i - 1, j - 1));

        /*
         * Attempt to get the room at every position in coords and if it exists
         * and is not null add it to the rooms array
         */
        List<Room> rooms = new ArrayList<>();
        for (Pair<Integer, Integer> coord : coords) {
            Room room;
            if (
                (room = ArrayUtils.safeGet(
                    current,
                    coord.getKey(),
                    coord.getValue()
                )) != null
            ) {
                rooms.add(room);
            }
        }
        return rooms;
    }


    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
