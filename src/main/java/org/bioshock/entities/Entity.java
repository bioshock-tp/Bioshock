package org.bioshock.entities;

import java.util.UUID;

import org.bioshock.components.NetworkC;
import org.bioshock.components.RendererC;
import org.bioshock.engine.core.WindowManager;
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

    public void setNetwokC(NetworkC component) {
        this.networkC = component;
    }

    public void setID(String newID) {
        uuid = newID;
    }

    public void setAnimation() {}

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

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
    
    /**
    *
    * Finds the current room that an entity is in
    *
    * @param entity the entity to find current room of
    * @return the current room of the entity
    */
   public static Room findCurrentRoom(Entity entity) {
       Room current = SceneManager.getMap().getRoomGraph().getNodes().get(0);
       Point3D temp;
       double shortest =
               WindowManager.getWindowWidth() * WindowManager.getWindowHeight();

       for (Room room : SceneManager.getMap().getRoomGraph().getNodes()) {
           temp = room.getRoomCenter().subtract(
                   new Point3D(entity.getX(), entity.getY(), room.getZ())
           );
           if (temp.magnitude() < shortest) {
               shortest = temp.magnitude();
               current = room;
           }
       }

       return current;
   }
    /**
     *
     * Finds the current room that an entity is in
     *
     * @param pos the point to find current room of
     * @return the current room of the point
     */
    public static Room findCurrentRoom(Point2D pos) {
        Room current = SceneManager.getMap().getRoomGraph().getNodes().get(0);
        Point3D temp;
        double shortest =
                WindowManager.getWindowWidth() * WindowManager.getWindowHeight();

        for (Room room : SceneManager.getMap().getRoomGraph().getNodes()) {
            temp = room.getRoomCenter().subtract(
                    new Point3D(pos.getX(), pos.getY(), room.getZ())
            );
            if (temp.magnitude() < shortest) {
                shortest = temp.magnitude();
                current = room;
            }
        }

        return current;
    }
}
