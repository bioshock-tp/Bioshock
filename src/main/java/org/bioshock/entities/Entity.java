package org.bioshock.entities;

import java.util.ArrayList;
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

/**
 * A class representing a generic entity in the game
 * 
 *
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
     * Basic constructor
     * @param p the position of the entity 
     * @param h the hitbox of the entity
     * @param netC the network component of the entity
     * @param renC the renderComponent of the entity
     */
    protected Entity(Point3D p, Rectangle h, NetworkC netC, RendererC renC) {
    	//set the current position in 2d
        position = new Point(p.getX(), p.getY());

        hitbox = h;
        hitbox.setFill(Color.TRANSPARENT);

        setPosition(position);

        networkC = netC;
        rendererC = renC;

        rendererC.setZ(p.getZ());
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
     * Checks to see if the given entities hitbox intersects the this entities hitbox
     * @param entity
     * @return whether the given entities hitbox intersects the this entities hitbox
     * 
     * false if the given entity is this
     */
    public boolean intersects(Entity entity) {
        return entity != this && intersects(entity.getHitbox());
    }

    /**
     * Checks to see if the given shape 
     * @param shape
     * @return whether the given shape intersects the this entities hitbox
     */
    public boolean intersects(Shape shape) {
        Shape intersects = Shape.intersect(
            this.getHitbox(),
            shape
        );

        return (intersects.getBoundsInLocal().getWidth() != -1);
    }

    /**
     * getter
     * @return enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * sets a new X coordinate
     * @param newX new X coordinate
     */
    public void setX(double newX) {
        setPosition(newX, getY());
    }

    /**
     * sets a new Y coordinate
     * @param newY new Y coordinate
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
     * sets enabled to the given boolean
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Sets rendererC to the given render component
     * @param renderC
     */
    public void setRenderC(RendererC renderC) {
        this.rendererC = renderC;
    }
    
    /**
     * Sets networkC to the given network component
     * @param component
     */
    public void setNetworkC(NetworkC component) {
        this.networkC = component;
    }

    /**
     * Sets the Unique ID of this entity
     * @param newID
     */
    public void setID(String newID) {
        uuid = newID;
    }

    /**
     * 
     * @return the Unique ID of this entity
     */
    public String getID() {
        return uuid;
    }

    /**
     * 
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
     * 
     * @return the current position of this entity
     */
    public Point getPosition() {
        return position;
    }

    /**
     * 
     * @return the current X coordinate
     */
    public double getX() {
        return position.getX();
    }

    /**
     * 
     * @return the current Y coordinate
     */
    public double getY() {
        return position.getY();
    }

    /**
     * 
     * @return the current Z coordinate
     */
    public double getZ() {
        return rendererC.getZ();
    }

    /**
     * 
     * @return this entities hitbox
     */
    public Rectangle getHitbox() {
        return hitbox;
    }

    /**
     * 
     * @return this entities renerer class
     */
    public Class<? extends Renderer> getRenderer() {
        return renderer;
    }

    /**
     * 
     * @return this entities render component
     */
    public RendererC getRendererC() {
        return rendererC;
    }

    /**
     * 
     * @return this entities network component
     */
    public NetworkC getNetworkC() {
        return networkC;
    }

    /**
     * 
     * @return whether to always render this entity
     */
    public boolean alwaysRender() {
        return alwaysRender;
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

        //Get the indices of the 4 closest rooms and add them to coords
        int i = (int) Math.round(pos.getY() / tRoomHeight);
        int j = (int) Math.round(pos.getX() / tRoomWidth);
        List<Pair<Integer, Integer>> coords = new ArrayList<>();
        coords.add(new Pair<>(i, j));
        coords.add(new Pair<>(i - 1, j));
        coords.add(new Pair<>(i, j - 1));
        coords.add(new Pair<>(i - 1, j - 1));

        //Attempt to get the room at every position in coords and if it exists and is not null
        //add it to the rooms array
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
