package org.bioshock.entities.map;

import java.util.Arrays;
import java.util.List;

import org.bioshock.components.NetworkC;
import org.bioshock.utils.GlobalConstants;
import org.bioshock.utils.Size;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class Sides {
    private static int padding = 1;
    private Sides() {}

    /**
     * A left side with an exit
     * @param pos the position of the top left of the side
     * @param wallWidth the width of the walls that make up the room
     * @param roomHeight the height of the central room
     * @param coriWidth the width of the corridor
     * @param coriLen the length of the corridor
     * @param c the colour of the room
     * @return the walls that make up the side and an array representing what sides are solid
     */
    public static Pair<List<TexRectEntity>,boolean[][]> lExit(
        Point3D pos,
        double wallWidth,
        double roomHeight,
        double coriWidth,
        double coriLen,
        Color c
    ) {
        boolean[][] traverseable = new boolean
                [(int) Math.round(coriLen)]
                [(int) Math.round(roomHeight)];
        
        //vertical wall
        TexRectEntity roomWall1 =
            new TexRectEntity(
                pos.add(coriLen - wallWidth, 0, 0),
                new NetworkC(false),
                new Size(wallWidth, (roomHeight - coriWidth) / 2 - wallWidth),
                c
            ); 

        //vertical wall
        TexRectEntity roomWall2 =
            new TexRectEntity(
                pos.add(
                    coriLen - wallWidth,
                    (roomHeight + coriWidth) / 2 + wallWidth, 0
                ),
                new NetworkC(false),
                new Size(wallWidth, (roomHeight - coriWidth) / 2 - wallWidth),
                c
            );

        //top wall
        TexRectEntity coriWall1 =
            new TexRectEntity(
                pos.add(
                    0,
                    (roomHeight - coriWidth) / 2 - wallWidth,
                    0
                ),
                new NetworkC(false),
                new Size(coriLen - wallWidth + padding, wallWidth),
                c
            );

        //bottom wall
        TexRectEntity coriWall2 =
            new TexRectEntity(
                pos.add(0, (roomHeight + coriWidth) / 2, 0),
                new NetworkC(false),
                new Size(coriLen - wallWidth + padding, wallWidth),
                c
            );

        //corner connecting top and left
        TexRectEntity cor1 =
            new TexRectEntity(
                pos.add(
                    coriLen - wallWidth,
                    (roomHeight - coriWidth) / 2 - wallWidth,
                    0
                ),
                new NetworkC(false),
                new Size(wallWidth, wallWidth),
                c
            );

        //corner connecting bottom and left
        TexRectEntity cor2 =
            new TexRectEntity(
                pos.add(
                    coriLen - wallWidth,
                    (roomHeight + coriWidth) / 2,
                    0
                ),
                new NetworkC(false),
                new Size(wallWidth, wallWidth),
                c
            );
        
        
        return Arrays.asList(
            roomWall1, roomWall2, coriWall1, coriWall2, cor1, cor2
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
     * @return the walls that make up the side and an array representing what sides are solid
     */
    public static Pair<List<TexRectEntity>,boolean[][]> rExit(
        Point3D pos,
        double wallWidth,
        double roomHeight,
        double coriWidth,
        double coriLen,
        Color c
    ) {

        //vertical wall
        TexRectEntity roomWall1 =
            new TexRectEntity(
                pos,
                new NetworkC(false),
                new Size(wallWidth, (roomHeight - coriWidth) / 2 - wallWidth),
                c
            );

        //vertical wall
        TexRectEntity roomWall2 =
            new TexRectEntity(
                pos.add(
                    0,
                    (roomHeight + coriWidth) / 2 + wallWidth,
                    0
                ),
                new NetworkC(false),
                new Size(wallWidth, (roomHeight - coriWidth) / 2 - wallWidth),
                c
            );

        //top wall
        TexRectEntity coriWall1 =
            new TexRectEntity(
                pos.add(
                    wallWidth,
                    (roomHeight - coriWidth) / 2 - wallWidth,
                    0
                ),
                new NetworkC(false),
                new Size(coriLen - wallWidth + padding, wallWidth),
                c
            );

        //bottom wall
        TexRectEntity coriWall2 =
            new TexRectEntity(
                pos.add(wallWidth, (roomHeight + coriWidth) / 2, 0),
                new NetworkC(false),
                new Size(coriLen - wallWidth + padding, wallWidth),
                c
            );

        //corner connecting top and right
        TexRectEntity cor1 =
            new TexRectEntity(
                pos.add(
                    0,
                    (roomHeight - coriWidth) / 2 - wallWidth,
                    0
            ),
                new NetworkC(false),
                new Size(wallWidth, wallWidth),
                c
            );

        //corner connecting bottom and right
        TexRectEntity cor2 =
            new TexRectEntity(
                pos.add(0, (roomHeight + coriWidth) / 2, 0),
                new NetworkC(false),
                new Size(wallWidth, wallWidth),
                c
            );

        return Arrays.asList(
            roomWall1, roomWall2, coriWall1, coriWall2, cor1, cor2
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
     * @return the walls that make up the side and an array representing what sides are solid
     */
    public static Pair<List<TexRectEntity>,boolean[][]> tExit(
        Point3D pos,
        double wallWidth,
        double roomWidth,
        double coriWidth,
        double coriLen,
        Color c
    ) {

        //top wall
        TexRectEntity roomWall1 =
            new TexRectEntity(
                pos.add(0, coriLen - wallWidth, 0),
                new NetworkC(false),
                new Size((roomWidth - coriWidth) / 2 - wallWidth, wallWidth),
                c
            );

        //top wall
        TexRectEntity roomWall2 =
            new TexRectEntity(
                pos.add(
                    (roomWidth + coriWidth) / 2 + wallWidth,
                    coriLen - wallWidth,
                    0
                ),
                new NetworkC(false),
                new Size((roomWidth - coriWidth) / 2 - wallWidth, wallWidth),
                c
            );

        //vertical wall
        TexRectEntity coriWall1 =
            new TexRectEntity(
                pos.add(
                    (roomWidth - coriWidth) / 2 - wallWidth,
                    0,
                    0
                ),
                new NetworkC(false),
                new Size(wallWidth, coriLen - wallWidth + padding),
                c
            );

        //vertical wall
        TexRectEntity coriWall2 =
            new TexRectEntity(
                pos.add((roomWidth + coriWidth) / 2, 0, 0),
                new NetworkC(false),
                new Size(wallWidth, coriLen - wallWidth + padding),
                c
            );

        //corner connecting top and left
        TexRectEntity cor1 =
            new TexRectEntity(
                pos.add(
                    (roomWidth - coriWidth) / 2 - wallWidth,
                    coriLen - wallWidth,
                    0
                ),
                new NetworkC(false),
                new Size(wallWidth, wallWidth),
                c
            );

        //corner connecting top and right
        TexRectEntity cor2 =
            new TexRectEntity(
                pos.add(
                    (roomWidth + coriWidth) / 2,
                    coriLen - wallWidth,
                    0
                ),
                new NetworkC(false),
                new Size(wallWidth, wallWidth),
                c
            );

        return Arrays.asList(
            roomWall1, roomWall2, coriWall1, coriWall2, cor1, cor2
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
     * @return the walls that make up the side and an array representing what sides are solid
     */
    public static Pair<List<TexRectEntity>,boolean[][]> bExit(
        Point3D pos,
        double wallWidth,
        double roomWidth,
        double coriWidth,
        double coriLen,
        Color c
    ) {

        //bottom wall
        TexRectEntity roomWall1 =
            new TexRectEntity(
                pos,
                new NetworkC(false),
                new Size((roomWidth - coriWidth) / 2 - wallWidth, wallWidth),
                c
            );

        //bottom wall
        TexRectEntity roomWall2 =
            new TexRectEntity(
                pos.add(
                    (roomWidth + coriWidth) / 2 + wallWidth,
                    0,
                    0
                ),
                new NetworkC(false),
                new Size((roomWidth - coriWidth) / 2 - wallWidth, wallWidth),
                c
            );

        //vertical wall
        TexRectEntity coriWall1 =
            new TexRectEntity(
                pos.add(
                    (roomWidth - coriWidth) / 2 - wallWidth,
                    wallWidth,
                    0
                ),
                new NetworkC(false),
                new Size(wallWidth, coriLen - wallWidth + padding),
                c
            );

        //vertical wall
        TexRectEntity coriWall2 =
            new TexRectEntity(
                pos.add((roomWidth + coriWidth) / 2, wallWidth, 0),
                new NetworkC(false),
                new Size(wallWidth, coriLen - wallWidth + padding),
                c
            );

        //corner connecting bottom and left
        TexRectEntity cor1 =
            new TexRectEntity(
                pos.add(
                    (roomWidth - coriWidth) / 2 - wallWidth,
                    0,
                    0
                ),
                new NetworkC(false),
                new Size(wallWidth, wallWidth),
                c
            );

        //corner connecting bottom and right
        TexRectEntity cor2 =
            new TexRectEntity(
                pos.add((roomWidth + coriWidth) / 2, 0, 0),
                new NetworkC(false),
                new Size(wallWidth, wallWidth),
                c
            );

        return Arrays.asList(
            roomWall1, roomWall2, coriWall1, coriWall2, cor1, cor2
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
     * @return the walls that make up the side and an array representing what sides are solid
     */
    public static Pair<List<TexRectEntity>,boolean[][]> lNoExit(
        Point3D pos,
        double wallWidth,
        double roomHeight,
        double coriWidth,
        double coriLen,
        Color c
    ) {

        //vertical wall
        TexRectEntity wall =
            new TexRectEntity(
                pos.add(coriLen - wallWidth, 0, 0),
                new NetworkC(false),
                new Size(wallWidth, roomHeight),
                c
            );

        return Arrays.asList(wall);
    }

    /**
     * A left side with no exit
     * @param pos the position of the top left of the side
     * @param wallWidth the width of the walls that make up the room
     * @param roomHeight the height of the central room
     * @param coriWidth the width of the corridor
     * @param coriLen the length of the corridor
     * @param c the colour of the room
     * @return the walls that make up the side and an array representing what sides are solid
     */
    public static Pair<List<TexRectEntity>,boolean[][]> rNoExit(
        Point3D pos,
        double wallWidth,
        double roomHeight,
        double coriWidth,
        double coriLen,
        Color c
    ) {

        //vertical wall
        TexRectEntity wall =
            new TexRectEntity(
                pos,
                new NetworkC(false),
                new Size(wallWidth, roomHeight),
                c
            );

        return Arrays.asList(wall);
    }

    /**
     * A top side with no exit
     * @param pos the position of the top left of the side
     * @param wallWidth the width of the walls that make up the room
     * @param roomWidth the width of the central room
     * @param coriWidth the width of the corridor
     * @param coriLen the length of the corridor
     * @param c the colour of the room
     * @return the walls that make up the side and an array representing what sides are solid
     */
    public static Pair<List<TexRectEntity>,boolean[][]> tNoExit(
        Point3D pos,
        double wallWidth,
        double roomWidth,
        double coriWidth,
        double coriLen,
        Color c
    ) {

        //top wall
        TexRectEntity wall =
            new TexRectEntity(
                pos.add(0, coriLen - wallWidth, 0),
                new NetworkC(false),
                new Size(roomWidth, wallWidth),
                c
            );

        return Arrays.asList(wall);
    }

    /**
     * A bottom side with no exit
     * @param pos the position of the top left of the side
     * @param wallWidth the width of the walls that make up the room
     * @param roomWidth the width of the central room
     * @param coriWidth the width of the corridor
     * @param coriLen the length of the corridor
     * @param c the colour of the room
     * @return the walls that make up the side and an array representing what sides are solid
     */
    public static Pair<List<TexRectEntity>,boolean[][]> bNoExit(
        Point3D pos,
        double wallWidth,
        double roomWidth,
        double coriWidth,
        double coriLen,
        Color c
    ) {

        //bottom wall
        TexRectEntity wall =
            new TexRectEntity(
                pos,
                new NetworkC(false),
                new Size(roomWidth, wallWidth),
                c
            );

        return Arrays.asList(wall);
    }
}
