package org.bioshock.entities.map;

import static org.bioshock.utils.GlobalConstants.UNIT_HEIGHT;
import static org.bioshock.utils.GlobalConstants.UNIT_WIDTH;

import java.util.Arrays;
import java.util.List;

import org.bioshock.components.NetworkC;
import org.bioshock.utils.ArrayUtils;
import org.bioshock.utils.GlobalConstants;
import org.bioshock.utils.Size;

import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Pair;

/**
 * A utility class used to generate the sides of a room 
 * 
 *
 */
public class Sides {
    /**
     * The padding to add to the end of corridors so that they overlap so entities 
     * can't fit between the gap between rooms
     */
    private static int padding = 5;

    /**
     * Private constructor as this is meant to be used as a static class
     */
    private Sides() {}

    /***
     * generates a new side at the given position and returns an array representing that side
     * @param pos
     * @param size the size of the side (in terms of units)
     * @param c the colour of the side
     * @return
     */
    public static Pair<Wall,boolean[][]> side(
        Point3D pos,
        Size size,
        Color c,
        Image image,
        double wallWidth
    ) {
        boolean[][] traversable = new boolean
            [(int) Math.round(size.getHeight())]
            [(int) Math.round(size.getWidth())];

        Wall side = new Wall(
            pos,
            new NetworkC(false),
            new Size(
                size.getWidth() * UNIT_WIDTH,
                size.getHeight() * UNIT_HEIGHT
            ),
            c,
            image,
            wallWidth
        );

        return new Pair<Wall, boolean[][]>(side, traversable);
    }

    /**
     * A left side with an exit
     * @param pos the position of the top left of the side
     * @param wallWidth the width of the walls that make up the room
     * @param roomHeight the height of the central room
     * @param coriWidth the width of the corridor
     * @param coriLen the length of the corridor
     * @param c the colour of the room
     * @return the walls that make up the side and an array representing what
     * sides are solid
     */
    public static Pair<List<Wall>, boolean[][]> lExit(
        Point3D pos,
        int wallWidth,
        double roomHeight,
        double coriWidth,
        double coriLen,
        Color c
    ) {
        boolean[][] traversable = new boolean
            [(int) Math.round(roomHeight)]
            [(int) Math.round(coriLen)];

        ArrayUtils.copyInArray(
            traversable,
            ArrayUtils.fill2DArray(
                new boolean
                    [(int) ((roomHeight - coriWidth) / 2 - wallWidth)]
                    [(int) (coriLen - wallWidth)],
                true
            ),
            0,
            0
        );

        ArrayUtils.copyInArray(
            traversable,
            ArrayUtils.fill2DArray(
                new boolean[(int) coriWidth][(int) coriLen],
                true),
            (int) ((roomHeight - coriWidth) / 2),
            0
        );

        ArrayUtils.copyInArray(
            traversable,
            ArrayUtils.fill2DArray(
                new boolean
                    [(int) ((roomHeight - coriWidth) / 2 - wallWidth)]
                    [(int) (coriLen - wallWidth)],
                true
            ),
            (int) ((roomHeight + coriWidth) / 2 + wallWidth),
            0
        );

        // vertical wall
        Wall roomWall1 = new Wall(
            pos.add((coriLen - wallWidth) * UNIT_WIDTH, 0, 0),
            new NetworkC(false),
            new Size(
                (double) wallWidth * UNIT_WIDTH,
                ((roomHeight - coriWidth) / 2 - wallWidth) * UNIT_HEIGHT
            ),
            c,
            GlobalConstants.VERT_WALL_IMAGE,
            wallWidth
        );

        // vertical wall
        Wall roomWall2 = new Wall(
            pos.add(
                (coriLen - wallWidth) * UNIT_WIDTH,
                ((roomHeight + coriWidth) / 2 + wallWidth) * UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(
                (double) wallWidth * UNIT_WIDTH,
                ((roomHeight - coriWidth) / 2 - wallWidth) * UNIT_HEIGHT
            ),
            c,
            GlobalConstants.VERT_WALL_IMAGE,
            wallWidth
        );

        // top wall
        Wall coriWall1 = new Wall(
            pos.add(
                0,
                ((roomHeight - coriWidth) / 2 - wallWidth) * UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(
                (coriLen - wallWidth) * UNIT_WIDTH + padding,
                (double) wallWidth * UNIT_HEIGHT
            ),
            c,
            GlobalConstants.TOP_HORI_WALL_IMAGE,
            wallWidth
        );

        // bottom wall
        Wall coriWall2 = new Wall(
            pos.add(0, ((roomHeight + coriWidth) / 2) * UNIT_HEIGHT, 0),
            new NetworkC(false),
            new Size(
                (coriLen - wallWidth) * UNIT_WIDTH + padding,
                (double) wallWidth * UNIT_HEIGHT
            ),
            c,
            GlobalConstants.BOT_HORI_WALL_IMAGE,
            wallWidth
        );

        // corner connecting top and left
        Wall cor1 = new Wall(
            pos.add(
                (coriLen - wallWidth) * UNIT_WIDTH,
                ((roomHeight - coriWidth) / 2 - wallWidth) * UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(
                (double) wallWidth * UNIT_WIDTH,
                (double) wallWidth * UNIT_HEIGHT
            ),
            c,
            GlobalConstants.TOP_LEFT_CORNER_WALL_IMAGE,
            wallWidth
        );

        //corner connecting bottom and left
        Wall cor2 = new Wall(
            pos.add(
                (coriLen - wallWidth) * UNIT_WIDTH,
                ((roomHeight + coriWidth) / 2) * UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(
                (double) wallWidth * UNIT_WIDTH,
                (double) wallWidth * UNIT_HEIGHT
            ),
            c,
            GlobalConstants.BOT_LEFT_CORNER_WALL_IMAGE,
            wallWidth
        );

        return new Pair<>(
            Arrays.asList(
                roomWall1, roomWall2, coriWall1, coriWall2, cor1, cor2
            ),
            traversable
        );
    }

    /**
     * A left side with an exit
     * @param pos the position of the top left of the side
     * @param wallWidth the width of the walls that make up the room
     * @param roomHeight the height of the central room
     * @param coriWidth the width of the corridor
     * @param coriLen the length of the corridor
     * @param c the colour of the room
     * @return the walls that make up the side and an array representing which
     * sides are solid
     */
    public static Pair<List<Wall>,boolean[][]> rExit(
        Point3D pos,
        int wallWidth,
        double roomHeight,
        double coriWidth,
        double coriLen,
        Color c
    ) {
        boolean[][] traversable = new boolean
            [(int) Math.round(roomHeight)]
            [(int) Math.round(coriLen)];

        ArrayUtils.copyInArray(
            traversable,
            ArrayUtils.fill2DArray(
                new boolean
                    [(int) ((roomHeight - coriWidth) / 2 - wallWidth)]
                    [(int) (coriLen - wallWidth)],
                true
            ),
            0,
            (int) wallWidth
        );

        ArrayUtils.copyInArray(
            traversable,
            ArrayUtils.fill2DArray(
                new boolean[(int) coriWidth][(int) coriLen],
                true
            ),
            (int) ((roomHeight - coriWidth) / 2),
            0
        );

        ArrayUtils.copyInArray(
            traversable,
            ArrayUtils.fill2DArray(
                new boolean
                    [(int) ((roomHeight - coriWidth) / 2 - wallWidth)]
                    [(int) (coriLen - wallWidth)],
                true
            ),
            (int) ((roomHeight + coriWidth) / 2 + wallWidth),
            (int) wallWidth
        );

        // vertical wall
        Wall roomWall1 = new Wall(
            pos,
            new NetworkC(false),
            new Size(
                (double) wallWidth * UNIT_WIDTH,
                ((roomHeight - coriWidth) / 2 - wallWidth) * UNIT_HEIGHT
            ),
            c,
            GlobalConstants.VERT_WALL_IMAGE,
            wallWidth
        );

        // vertical wall
        Wall roomWall2 = new Wall(
            pos.add(
                0,
                ((roomHeight + coriWidth) / 2 + wallWidth) * UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(
                (double) wallWidth * UNIT_WIDTH,
                ((roomHeight - coriWidth) / 2 - wallWidth) * UNIT_HEIGHT
            ),
            c,
            GlobalConstants.VERT_WALL_IMAGE,
            wallWidth
        );

        //top wall
        Wall coriWall1 = new Wall(
            pos.add(
                (double) wallWidth * UNIT_WIDTH,
                ((roomHeight - coriWidth) / 2 - wallWidth) * UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(
                (coriLen - wallWidth) * UNIT_WIDTH + padding,
                (double) wallWidth * UNIT_HEIGHT
            ),
            c,
            GlobalConstants.TOP_HORI_WALL_IMAGE,
            wallWidth
        );

        //bottom wall
        Wall coriWall2 = new Wall(
            pos.add(
                (double) wallWidth * UNIT_WIDTH,
                ((roomHeight + coriWidth) / 2) * UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(
                (coriLen - wallWidth) * UNIT_WIDTH + padding,
                (double) wallWidth * UNIT_HEIGHT
            ),
            c,
            GlobalConstants.BOT_HORI_WALL_IMAGE,
            wallWidth
        );

        //corner connecting top and right
        Wall cor1 = new Wall(
            pos.add(
                0,
                ((roomHeight - coriWidth) / 2 - wallWidth) * UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(
                (double) wallWidth * UNIT_WIDTH,
                (double) wallWidth * UNIT_HEIGHT
            ),
            c,
            GlobalConstants.TOP_RIGHT_CORNER_WALL_IMAGE,
            wallWidth
        );

        //corner connecting bottom and right
        Wall cor2 = new Wall(
            pos.add(0, ((roomHeight + coriWidth) / 2) * UNIT_HEIGHT, 0),
            new NetworkC(false),
            new Size(
                (double) wallWidth * UNIT_WIDTH,
                (double) wallWidth * UNIT_HEIGHT
            ),
            c,
            GlobalConstants.BOT_RIGHT_CORNER_WALL_IMAGE,
            wallWidth
        );

        return new Pair<>(
            Arrays.asList(
                roomWall1, roomWall2, coriWall1, coriWall2, cor1, cor2
            ),
            traversable
        );
    }

    /**
     * A top side with an exit
     * @param pos the position of the top left of the side
     * @param wallWidth the width of the walls that make up the room
     * @param roomWidth the width of the central room
     * @param coriWidth the width of the corridor
     * @param coriLen the length of the corridor
     * @param c the colour of the room
     * @return the walls that make up the side and an array representing what
     * sides are solid
     */
    public static Pair<List<Wall>,boolean[][]> tExit(
        Point3D pos,
        int wallWidth,
        double roomWidth,
        double coriWidth,
        double coriLen,
        Color c
    ) {
        boolean[][] traversable = new boolean
            [(int) Math.round(coriLen)]
            [(int) Math.round(roomWidth)];

        ArrayUtils.copyInArray(
            traversable,
            ArrayUtils.fill2DArray(
                new boolean
                    [(int) (coriLen - wallWidth)]
                    [(int) ((roomWidth - coriWidth) / 2 - wallWidth)],
                true
            ),
            0,
            0
        );

        ArrayUtils.copyInArray(
            traversable,
            ArrayUtils.fill2DArray(
                new boolean[(int) coriLen][(int) coriWidth],
                true
            ),
            0,
            (int) ((roomWidth - coriWidth) / 2)
        );

        ArrayUtils.copyInArray(
            traversable,
            ArrayUtils.fill2DArray(
                new boolean
                    [(int) (coriLen - wallWidth)]
                    [(int) ((roomWidth - coriWidth) / 2 - wallWidth)],
                true
            ),
            0,
            (int) ((roomWidth + coriWidth) / 2 + wallWidth)
        );

        //top wall
        Wall roomWall1 = new Wall(
            pos.add(0, (coriLen - wallWidth) * UNIT_HEIGHT, 0),
            new NetworkC(false),
            new Size(
                ((roomWidth - coriWidth) / 2 - wallWidth) * UNIT_WIDTH,
                (double) wallWidth * UNIT_HEIGHT
            ),
            c,
            GlobalConstants.TOP_HORI_WALL_IMAGE,
            wallWidth
        );

        //top wall
        Wall roomWall2 = new Wall(
            pos.add(
                ((roomWidth + coriWidth) / 2 + wallWidth) * UNIT_WIDTH,
                (coriLen - wallWidth) * UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(
                ((roomWidth - coriWidth) / 2 - wallWidth) * UNIT_WIDTH,
                (double) wallWidth * UNIT_HEIGHT
            ),
            c,
            GlobalConstants.TOP_HORI_WALL_IMAGE,
            wallWidth
        );

        //vertical wall
        Wall coriWall1 = new Wall(
            pos.add(
                ((roomWidth - coriWidth) / 2 - wallWidth) * UNIT_WIDTH,
                0,
                0
            ),
            new NetworkC(false),
            new Size(
                (double) wallWidth * UNIT_WIDTH,
                (coriLen - wallWidth) * UNIT_HEIGHT + padding
            ),
            c,
            GlobalConstants.VERT_WALL_IMAGE,
            wallWidth
        );

        //vertical wall
        Wall coriWall2 = new Wall(
            pos.add(((roomWidth + coriWidth) / 2) * UNIT_WIDTH, 0, 0),
            new NetworkC(false),
            new Size(
                (double) wallWidth * UNIT_WIDTH,
                (coriLen - wallWidth) * UNIT_HEIGHT + padding
            ),
            c,
            GlobalConstants.VERT_WALL_IMAGE,
            wallWidth
        );

        //corner connecting top and left
        Wall cor1 = new Wall(
            pos.add(
                ((roomWidth - coriWidth) / 2 - wallWidth) * UNIT_WIDTH,
                (coriLen - wallWidth) * UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(
                (double) wallWidth * UNIT_WIDTH,
                (double) wallWidth * UNIT_HEIGHT
            ),
            c,
            GlobalConstants.TOP_LEFT_CORNER_WALL_IMAGE,
            wallWidth
        );

        //corner connecting top and right
        Wall cor2 = new Wall(
            pos.add(
                ((roomWidth + coriWidth) / 2) * UNIT_WIDTH,
                (coriLen - wallWidth) * UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(
                (double) wallWidth * UNIT_WIDTH,
                (double) wallWidth * UNIT_HEIGHT
            ),
            c,
            GlobalConstants.TOP_RIGHT_CORNER_WALL_IMAGE,
            wallWidth
        );

        return new Pair<>(
            Arrays.asList(
                roomWall1, roomWall2, coriWall1, coriWall2, cor1, cor2
            ),
            traversable
        );
    }

    /**
     * A bottom side with an exit
     * @param pos the position of the top left of the side
     * @param wallWidth the width of the walls that make up the room
     * @param roomWidth the width of the central room
     * @param coriWidth the width of the corridor
     * @param coriLen the length of the corridor
     * @param c the colour of the room
     * @return the walls that make up the side and an array representing what
     * sides are solid
     */
    public static Pair<List<Wall>,boolean[][]> bExit(
        Point3D pos,
        int wallWidth,
        double roomWidth,
        double coriWidth,
        double coriLen,
        Color c
    ) {
        boolean[][] traversable = new boolean
            [(int) Math.round(coriLen)]
            [(int) Math.round(roomWidth)];

        ArrayUtils.copyInArray(
            traversable,
            ArrayUtils.fill2DArray(
                new boolean
                    [(int) (coriLen - wallWidth)]
                    [(int) ((roomWidth - coriWidth) / 2 - wallWidth)],
                true
            ),
            (int) wallWidth,
            0
        );

        ArrayUtils.copyInArray(
            traversable,
            ArrayUtils.fill2DArray(
                new boolean[(int) coriLen][(int) coriWidth],
                true
            ),
            0,
            (int)((roomWidth - coriWidth) / 2)
        );

        ArrayUtils.copyInArray(
            traversable,
            ArrayUtils.fill2DArray(
                new boolean
                    [(int) (coriLen - wallWidth)]
                    [(int) ((roomWidth - coriWidth) / 2 - wallWidth)],
                true
            ),
            (int) wallWidth,
            (int) ((roomWidth + coriWidth) / 2 + wallWidth)
        );

        // bottom wall
        Wall roomWall1 = new Wall(
            pos,
            new NetworkC(false),
            new Size(
                ((roomWidth - coriWidth) / 2 - wallWidth) * UNIT_WIDTH,
                (double) wallWidth * UNIT_HEIGHT
            ),
            c,
            GlobalConstants.BOT_HORI_WALL_IMAGE,
            wallWidth
        );

        // bottom wall
        Wall roomWall2 = new Wall(
            pos.add(
                ((roomWidth + coriWidth) / 2 + wallWidth) * UNIT_WIDTH,
                0,
                0
            ),
            new NetworkC(false),
            new Size(
                ((roomWidth - coriWidth) / 2 - wallWidth) * UNIT_WIDTH,
                (double) wallWidth * UNIT_HEIGHT
            ),
            c,
            GlobalConstants.BOT_HORI_WALL_IMAGE,
            wallWidth
        );

        // vertical wall
        Wall coriWall1 = new Wall(
            pos.add(
                ((roomWidth - coriWidth) / 2 - wallWidth) * UNIT_WIDTH,
                (double) wallWidth * UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(
                (double) wallWidth * UNIT_WIDTH,
                (coriLen - wallWidth) * UNIT_HEIGHT + padding
            ),
            c,
            GlobalConstants.VERT_WALL_IMAGE,
            wallWidth
        );

        // vertical wall
        Wall coriWall2 = new Wall(
            pos.add(
                ((roomWidth + coriWidth) / 2) * UNIT_WIDTH,
                (double) wallWidth * UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(
                (double) wallWidth * UNIT_WIDTH,
                (coriLen - wallWidth) * UNIT_HEIGHT + padding
            ),
            c,
            GlobalConstants.VERT_WALL_IMAGE,
            wallWidth
        );

        // corner connecting bottom and left
        Wall cor1 = new Wall(
            pos.add(
                ((roomWidth - coriWidth) / 2 - wallWidth) * UNIT_WIDTH,
                0,
                0
            ),
            new NetworkC(false),
            new Size(
                (double) wallWidth * UNIT_WIDTH,
                (double) wallWidth * UNIT_HEIGHT
            ),
            c,
            GlobalConstants.BOT_LEFT_CORNER_WALL_IMAGE,
            wallWidth
        );

        // corner connecting bottom and right
        Wall cor2 = new Wall(
            pos.add(((roomWidth + coriWidth) / 2) * UNIT_WIDTH, 0, 0),
            new NetworkC(false),
            new Size(
                (double) wallWidth * UNIT_WIDTH,
                (double)  wallWidth * UNIT_HEIGHT
            ),
            c,
            GlobalConstants.BOT_RIGHT_CORNER_WALL_IMAGE,
            wallWidth
        );

        return new Pair<>(
            Arrays.asList(
                roomWall1, roomWall2, coriWall1, coriWall2, cor1, cor2
            ),
            traversable
        );
    }

    /**
     * A left side with no exit
     * @param pos the position of the top left of the side
     * @param wallWidth the width of the walls that make up the room
     * @param roomHeight the height of the central room
     * @param coriWidth the width of the corridor
     * @param coriLen the length of the corridor
     * @param c the colour of the room
     * @return the walls that make up the side and an array representing what
     * sides are solid
     */
    public static Pair<List<Wall>,boolean[][]> lNoExit(
        Point3D pos,
        int wallWidth,
        double roomHeight,
        double coriWidth,
        double coriLen,
        Color c
    ) {
        boolean[][] traversable = new boolean
            [(int) Math.round(roomHeight)]
            [(int) Math.round(coriLen)];

        ArrayUtils.copyInArray(
            traversable,
            ArrayUtils.fill2DArray(
                new boolean[(int) (roomHeight)][(int) (coriLen - wallWidth)],
                true
            ),
            0,
            0
        );

        //vertical wall
        Wall wall =
            new Wall(
                pos.add((coriLen - wallWidth) * UNIT_WIDTH, 0, 0),
                new NetworkC(false),
                new Size(
                    (double) wallWidth * UNIT_WIDTH,
                    roomHeight * UNIT_HEIGHT
                ),
                c,
                GlobalConstants.VERT_WALL_IMAGE,
                wallWidth
            );

        return new Pair<>(
            Arrays.asList(wall),
            traversable
        );
    }

    /**
     * A left side with no exit
     * @param pos the position of the top left of the side
     * @param wallWidth the width of the walls that make up the room
     * @param roomHeight the height of the central room
     * @param coriWidth the width of the corridor
     * @param coriLen the length of the corridor
     * @param c the colour of the room
     * @return the walls that make up the side and an array representing what
     * sides are solid
     */
    public static Pair<List<Wall>,boolean[][]> rNoExit(
        Point3D pos,
        int wallWidth,
        double roomHeight,
        double coriWidth,
        double coriLen,
        Color c
    ) {
        boolean[][] traversable = new boolean
            [(int) Math.round(roomHeight)]
            [(int) Math.round(coriLen)];

        ArrayUtils.copyInArray(
            traversable,
            ArrayUtils.fill2DArray(
                new boolean[(int) (roomHeight)][(int) (coriLen - wallWidth)],
                true
            ),
            0,
            (int) wallWidth
        );

        // vertical wall
        Wall wall =
            new Wall(
                pos,
                new NetworkC(false),
                new Size(
                    (double) wallWidth * UNIT_WIDTH,
                    roomHeight * UNIT_HEIGHT
                ),
                c,
                GlobalConstants.VERT_WALL_IMAGE,
                wallWidth
            );

        return new Pair<>(
            Arrays.asList(wall),
            traversable
        );
    }

    /**
     * A top side with no exit
     * @param pos the position of the top left of the side
     * @param wallWidth the width of the walls that make up the room
     * @param roomWidth the width of the central room
     * @param coriWidth the width of the corridor
     * @param coriLen the length of the corridor
     * @param c the colour of the room
     * @return the walls that make up the side and an array representing what
     * sides are solid
     */
    public static Pair<List<Wall>,boolean[][]> tNoExit(
        Point3D pos,
        int wallWidth,
        double roomWidth,
        double coriWidth,
        double coriLen,
        Color c
    ) {
        boolean[][] traversable = new boolean
            [(int) Math.round(coriLen)]
            [(int) Math.round(roomWidth)];

        ArrayUtils.copyInArray(
            traversable,
            ArrayUtils.fill2DArray(
                new boolean[(int) (coriLen - wallWidth)][(int) (roomWidth)],
                true
            ),
            0,
            0
        );

        // top wall
        Wall wall = new Wall(
            pos.add(0, (coriLen - wallWidth) * UNIT_HEIGHT, 0),
            new NetworkC(false),
            new Size(roomWidth * UNIT_WIDTH, (double) wallWidth * UNIT_HEIGHT),
            c,
            GlobalConstants.TOP_HORI_WALL_IMAGE,
            wallWidth
        );

        return new Pair<>(
            Arrays.asList(wall),
            traversable
        );
    }

    /**
     * A bottom side with no exit
     * @param pos the position of the top left of the side
     * @param wallWidth the width of the walls that make up the room
     * @param roomWidth the width of the central room
     * @param coriWidth the width of the corridor
     * @param coriLen the length of the corridor
     * @param c the colour of the room
     * @return the walls that make up the side and an array representing what
     * sides are solid
     */
    public static Pair<List<Wall>,boolean[][]> bNoExit(
        Point3D pos,
        int wallWidth,
        double roomWidth,
        double coriWidth,
        double coriLen,
        Color c
    ) {
        boolean[][] traversable = new boolean
            [(int) Math.round(coriLen)]
            [(int) Math.round(roomWidth)];

        ArrayUtils.copyInArray(
            traversable,
            ArrayUtils.fill2DArray(
                new boolean[(int) (coriLen - wallWidth)][(int) (roomWidth)],
                true
            ),
            wallWidth,
            0
        );

        //bottom wall
        Wall wall = new Wall(
            pos,
            new NetworkC(false),
            new Size(
                roomWidth * UNIT_WIDTH,
                (double) wallWidth * UNIT_HEIGHT
            ),
            c,
            GlobalConstants.BOT_HORI_WALL_IMAGE,
            wallWidth
        );

        return new Pair<>(
            Arrays.asList(wall),
            traversable
        );
    }
}
