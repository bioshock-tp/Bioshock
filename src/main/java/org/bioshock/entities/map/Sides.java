package org.bioshock.entities.map;

import static org.bioshock.utils.GlobalConstants.UNIT_HEIGHT;
import static org.bioshock.utils.GlobalConstants.UNIT_WIDTH;

import java.util.Arrays;
import java.util.List;

import org.bioshock.components.NetworkC;
import org.bioshock.utils.ArrayUtils;
import org.bioshock.utils.Size;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class Sides {
    private static int padding = 5;
    private Sides() {}

    /***
     * generates a new side at the given position and returns an array representing that side
     * @param pos
     * @param size the size of the side (in terms of units)
     * @param c the colour of the side
     * @return
     */
    public static Pair<TexRectEntity, boolean[][]> side(
        Point3D pos,
        Size size,
        Color c
    ) {
        boolean[][] traversable = new boolean
            [(int) Math.round(size.getHeight())]
            [(int) Math.round(size.getWidth())];

        TexRectEntity side = new TexRectEntity(
            pos,
            new NetworkC(false),
            new Size(
                size.getWidth() * UNIT_WIDTH,
                size.getHeight() * UNIT_HEIGHT
            ),
            c
        );

        return new Pair<>(side, traversable);
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
    public static Pair<List<TexRectEntity>,boolean[][]> lExit(
        Point3D pos,
        double wallWidth,
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

        //vertical wall
        TexRectEntity roomWall1 = new TexRectEntity(
            pos.add((coriLen - wallWidth) * UNIT_WIDTH, 0, 0),
            new NetworkC(false),
            new Size(
                wallWidth * UNIT_WIDTH,
                ((roomHeight - coriWidth) / 2 - wallWidth) * UNIT_HEIGHT
            ),
            c
        );

        //vertical wall
        TexRectEntity roomWall2 = new TexRectEntity(
            pos.add(
                (coriLen - wallWidth) * UNIT_WIDTH,
                ((roomHeight + coriWidth) / 2 + wallWidth) * UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(
                wallWidth * UNIT_WIDTH,
                ((roomHeight - coriWidth) / 2 - wallWidth) * UNIT_HEIGHT
            ),
            c
        );

        //top wall
        TexRectEntity coriWall1 = new TexRectEntity(
            pos.add(
                0,
                ((roomHeight - coriWidth) / 2 - wallWidth) * UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(
                (coriLen - wallWidth) * UNIT_WIDTH + padding,
                wallWidth * UNIT_HEIGHT
            ),
            c
        );

        //bottom wall
        TexRectEntity coriWall2 = new TexRectEntity(
            pos.add(0, ((roomHeight + coriWidth) / 2) * UNIT_HEIGHT, 0),
            new NetworkC(false),
            new Size(
                (coriLen - wallWidth) * UNIT_WIDTH + padding,
                wallWidth * UNIT_HEIGHT
            ),
            c
        );

        //corner connecting top and left
        TexRectEntity cor1 = new TexRectEntity(
            pos.add(
                (coriLen - wallWidth) * UNIT_WIDTH,
                ((roomHeight - coriWidth) / 2 - wallWidth) * UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(wallWidth * UNIT_WIDTH, wallWidth * UNIT_HEIGHT),
            c
        );

        //corner connecting bottom and left
        TexRectEntity cor2 = new TexRectEntity(
            pos.add(
                (coriLen - wallWidth) * UNIT_WIDTH,
                ((roomHeight + coriWidth) / 2) * UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(wallWidth * UNIT_WIDTH, wallWidth * UNIT_HEIGHT),
            c
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
    public static Pair<List<TexRectEntity>, boolean[][]> rExit(
        Point3D pos,
        double wallWidth,
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

        //vertical wall
        TexRectEntity roomWall1 = new TexRectEntity(
            pos,
            new NetworkC(false),
            new Size(
                wallWidth * UNIT_WIDTH,
                ((roomHeight - coriWidth) / 2 - wallWidth) * UNIT_HEIGHT
            ),
            c
        );

        //vertical wall
        TexRectEntity roomWall2 = new TexRectEntity(
            pos.add(
                0,
                ((roomHeight + coriWidth) / 2 + wallWidth) * UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(
                wallWidth * UNIT_WIDTH,
                ((roomHeight - coriWidth) / 2 - wallWidth) * UNIT_HEIGHT
            ),
            c
        );

        //top wall
        TexRectEntity coriWall1 = new TexRectEntity(
            pos.add(
                wallWidth * UNIT_WIDTH,
                ((roomHeight - coriWidth) / 2 - wallWidth) * UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(
                (coriLen - wallWidth) * UNIT_WIDTH + padding,
                wallWidth * UNIT_HEIGHT
            ),
            c
        );

        //bottom wall
        TexRectEntity coriWall2 = new TexRectEntity(
            pos.add(
                wallWidth * UNIT_WIDTH,
                ((roomHeight + coriWidth) / 2) * UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(
                (coriLen - wallWidth) * UNIT_WIDTH + padding,
                wallWidth * UNIT_HEIGHT
            ),
            c
        );

        //corner connecting top and right
        TexRectEntity cor1 = new TexRectEntity(
            pos.add(
                0,
                ((roomHeight - coriWidth) / 2 - wallWidth) * UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(wallWidth * UNIT_WIDTH, wallWidth * UNIT_HEIGHT),
            c
        );

        //corner connecting bottom and right
        TexRectEntity cor2 = new TexRectEntity(
            pos.add(0, ((roomHeight + coriWidth) / 2) * UNIT_HEIGHT, 0),
            new NetworkC(false),
            new Size(wallWidth * UNIT_WIDTH, wallWidth * UNIT_HEIGHT),
            c
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
    public static Pair<List<TexRectEntity>,boolean[][]> tExit(
        Point3D pos,
        double wallWidth,
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
                true),
            0,
            0
        );

        ArrayUtils.copyInArray(
            traversable,
            ArrayUtils.fill2DArray(
                    new boolean[(int) coriLen][(int) coriWidth],
                    true),
            0,
            (int) ((roomWidth - coriWidth) / 2)
        );

        ArrayUtils.copyInArray(
            traversable,
            ArrayUtils.fill2DArray(
                new boolean
                    [(int) (coriLen - wallWidth)]
                    [(int) ((roomWidth - coriWidth) / 2 - wallWidth)],
                true),
            0,
            (int) ((roomWidth + coriWidth) / 2 + wallWidth)
        );

        //top wall
        TexRectEntity roomWall1 = new TexRectEntity(
            pos.add(0, (coriLen - wallWidth) * UNIT_HEIGHT, 0),
            new NetworkC(false),
            new Size(
                ((roomWidth - coriWidth) / 2 - wallWidth) * UNIT_WIDTH,
                wallWidth * UNIT_HEIGHT
            ),
            c
        );

        //top wall
        TexRectEntity roomWall2 = new TexRectEntity(
            pos.add(
                ((roomWidth + coriWidth) / 2 + wallWidth) * UNIT_WIDTH,
                (coriLen - wallWidth) * UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(
                ((roomWidth - coriWidth) / 2 - wallWidth) * UNIT_WIDTH,
                wallWidth * UNIT_HEIGHT
            ),
            c
        );

        //vertical wall
        TexRectEntity coriWall1 = new TexRectEntity(
            pos.add(
                ((roomWidth - coriWidth) / 2 - wallWidth) * UNIT_WIDTH,
                0,
                0
            ),
            new NetworkC(false),
            new Size(
                wallWidth * UNIT_WIDTH,
                (coriLen - wallWidth) * UNIT_HEIGHT + padding
            ),
            c
        );

        //vertical wall
        TexRectEntity coriWall2 = new TexRectEntity(
            pos.add(((roomWidth + coriWidth) / 2) * UNIT_WIDTH, 0, 0),
            new NetworkC(false),
            new Size(
                wallWidth * UNIT_WIDTH,
                (coriLen - wallWidth) * UNIT_HEIGHT + padding
            ),
            c
        );

        //corner connecting top and left
        TexRectEntity cor1 = new TexRectEntity(
            pos.add(
                ((roomWidth - coriWidth) / 2 - wallWidth) * UNIT_WIDTH,
                (coriLen - wallWidth) * UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(wallWidth * UNIT_WIDTH, wallWidth * UNIT_HEIGHT),
            c
        );

        //corner connecting top and right
        TexRectEntity cor2 = new TexRectEntity(
            pos.add(
                ((roomWidth + coriWidth) / 2) * UNIT_WIDTH,
                (coriLen - wallWidth) * UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(wallWidth * UNIT_WIDTH, wallWidth * UNIT_HEIGHT),
            c
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
    public static Pair<List<TexRectEntity>,boolean[][]> bExit(
        Point3D pos,
        double wallWidth,
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
                true),
            (int) wallWidth,
            0
        );

        ArrayUtils.copyInArray(
            traversable,
            ArrayUtils.fill2DArray(
                new boolean[(int) coriLen][(int) coriWidth],
                true),
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

        //bottom wall
        TexRectEntity roomWall1 = new TexRectEntity(
                pos,
                new NetworkC(false),
                new Size(
                    ((roomWidth - coriWidth) / 2 - wallWidth) * UNIT_WIDTH,
                    wallWidth * UNIT_HEIGHT
                ),
                c
            );

        //bottom wall
        TexRectEntity roomWall2 = new TexRectEntity(
            pos.add(
                ((roomWidth + coriWidth) / 2 + wallWidth) * UNIT_WIDTH,
                0,
                0
            ),
            new NetworkC(false),
            new Size(
                ((roomWidth - coriWidth) / 2 - wallWidth) * UNIT_WIDTH,
                wallWidth * UNIT_HEIGHT
            ),
            c
        );

        //vertical wall
        TexRectEntity coriWall1 = new TexRectEntity(
            pos.add(
                ((roomWidth - coriWidth) / 2 - wallWidth) * UNIT_WIDTH,
                wallWidth * UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(
                wallWidth * UNIT_WIDTH,
                (coriLen - wallWidth) * UNIT_HEIGHT + padding
            ),
            c
        );

        //vertical wall
        TexRectEntity coriWall2 = new TexRectEntity(
            pos.add(
                ((roomWidth + coriWidth) / 2) * UNIT_WIDTH,
                wallWidth * UNIT_HEIGHT,
                0
            ),
            new NetworkC(false),
            new Size(
                wallWidth * UNIT_WIDTH,
                (coriLen - wallWidth) * UNIT_HEIGHT + padding
            ),
            c
        );

        //corner connecting bottom and left
        TexRectEntity cor1 = new TexRectEntity(
            pos.add(
                ((roomWidth - coriWidth) / 2 - wallWidth) * UNIT_WIDTH,
                0,
                0
            ),
            new NetworkC(false),
            new Size(wallWidth * UNIT_WIDTH, wallWidth * UNIT_HEIGHT),
            c
        );

        //corner connecting bottom and right
        TexRectEntity cor2 = new TexRectEntity(
            pos.add(((roomWidth + coriWidth) / 2) * UNIT_WIDTH, 0, 0),
            new NetworkC(false),
            new Size(wallWidth * UNIT_WIDTH, wallWidth * UNIT_HEIGHT),
            c
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
    public static Pair<List<TexRectEntity>,boolean[][]> lNoExit(
        Point3D pos,
        double wallWidth,
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
        TexRectEntity wall = new TexRectEntity(
            pos.add((coriLen - wallWidth) * UNIT_WIDTH, 0, 0),
            new NetworkC(false),
            new Size(wallWidth * UNIT_WIDTH, roomHeight * UNIT_HEIGHT),
            c
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
    public static Pair<List<TexRectEntity>,boolean[][]> rNoExit(
        Point3D pos,
        double wallWidth,
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

        //vertical wall
        TexRectEntity wall = new TexRectEntity(
            pos,
            new NetworkC(false),
            new Size(wallWidth * UNIT_WIDTH, roomHeight * UNIT_HEIGHT),
            c
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
    public static Pair<List<TexRectEntity>,boolean[][]> tNoExit(
        Point3D pos,
        double wallWidth,
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

        //top wall
        TexRectEntity wall = new TexRectEntity(
            pos.add(0, (coriLen - wallWidth) * UNIT_HEIGHT, 0),
            new NetworkC(false),
            new Size(roomWidth * UNIT_WIDTH, wallWidth * UNIT_HEIGHT),
            c
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
    public static Pair<List<TexRectEntity>,boolean[][]> bNoExit(
        Point3D pos,
        double wallWidth,
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
            (int) wallWidth,
            0
        );

        //bottom wall
        TexRectEntity wall = new TexRectEntity(
            pos,
            new NetworkC(false),
            new Size(roomWidth * UNIT_WIDTH, wallWidth * UNIT_HEIGHT),
            c
        );

        return new Pair<>(
            Arrays.asList(wall),
            traversable
        );
    }
}
