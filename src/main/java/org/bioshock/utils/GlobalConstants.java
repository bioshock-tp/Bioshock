package org.bioshock.utils;

import javafx.scene.image.Image;
import org.bioshock.entities.map.utils.RoomType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.bioshock.entities.map.utils.RoomType.NO_ROOM;
import static org.bioshock.entities.map.utils.RoomType.SINGLE_ROOM;

/**
 *
 * A class to store constants shared across the App
 *
 */
public final class GlobalConstants {
    /**
     * The width of the player on the sprite sheet
     */
    public static final double PLAYER_WIDTH = 36;
    /**
     * The height of the player on the sprite sheet
     */
    public static final double PLAYER_HEIGHT = 42;
    /**
     * The scale at which to render the player
     */
    public static final double PLAYER_SCALE = 1.6;
    /**
     * The playback speed of the player walking animation
     */
    public static final double PLAYER_ANIMATION_SPEED = 0.15;

    //Used to make hand typing arrays faster but is less readable
    private static final RoomType A = NO_ROOM;
    private static final RoomType B = SINGLE_ROOM;

    /**
     * A three by three map of rooms
     */
    public static final RoomType[][] THREE_SQUARED_MAP = {
        {NO_ROOM, SINGLE_ROOM, SINGLE_ROOM, SINGLE_ROOM},
		{NO_ROOM, SINGLE_ROOM, SINGLE_ROOM, SINGLE_ROOM},
		{NO_ROOM, SINGLE_ROOM, SINGLE_ROOM, SINGLE_ROOM}
    };

    public static final int UNIT_WIDTH = 59;
    public static final int UNIT_HEIGHT = 66;

    /**
     * A basic test map
     */
    public static final RoomType[][] TEST_MAP = {
        {B,B,B,B,A,B},
        {B,A,A,B,B,B},
        {B,A,B,B,A,B},
        {B,B,A,B,B,A},
        {B,A,B,B,A,B}
    };

    /**
     * A simple map for debugging purposes
     */
    public static final RoomType[][] SIMPLE_MAP = {
        {B,A},
        {B,B}
    };

    /**
     * A map made up of one room
     */
    public static final RoomType[][] SINGELTON_MAP = {{B}};

    /**
     * The relative path for a floor tile
     */
    private static final String FLOOR_PATH = GlobalConstants.class.getResource(
        "/org/bioshock/images/floors/tile_71.png"
    ).getPath();
    /**
     * The loaded floor image
     */
    public static final Image FLOOR_IMAGE = new Image(
        new File(FLOOR_PATH).toURI().toString(),
        UNIT_WIDTH,
        UNIT_HEIGHT,
        false,
        true
    );

    /*
     * The following is a set of image paths and then their loaded image
     */
    private static final String BOT_HORI_WALL_PATH =
        GlobalConstants.class.getResource(
            "/org/bioshock/images/walls/botHori.png"
        ).getPath();

    public static final Image BOT_HORI_WALL_IMAGE = new Image(
        new File(BOT_HORI_WALL_PATH).toURI().toString(),
        UNIT_WIDTH,
        UNIT_HEIGHT,
        false,
        true
    );

    private static final String BOT_LEFT_CORNER_WALL_PATH =
        GlobalConstants.class.getResource(
            "/org/bioshock/images/walls/botLeftCorner.png"
        ).getPath();

    public static final Image BOT_LEFT_CORNER_WALL_IMAGE = new Image(
        new File(BOT_LEFT_CORNER_WALL_PATH).toURI().toString(),
        UNIT_WIDTH,
        UNIT_HEIGHT,
        false,
        true);

    private static final String BOT_RIGHT_CORNER_WALL_PATH =
        GlobalConstants.class.getResource(
            "/org/bioshock/images/walls/botRightCorner.png"
        ).getPath();
    public static final Image BOT_RIGHT_CORNER_WALL_IMAGE = new Image(
        new File(BOT_RIGHT_CORNER_WALL_PATH).toURI().toString(),
        UNIT_WIDTH,
        UNIT_HEIGHT,
        false,
        true
    );

    private static final String TOP_HORI_WALL_PATH =
        GlobalConstants.class.getResource(
            "/org/bioshock/images/walls/topHori.png"
        ).getPath();
    public static final Image TOP_HORI_WALL_IMAGE = new Image(
        new File(TOP_HORI_WALL_PATH).toURI().toString(),
        UNIT_WIDTH,
        UNIT_HEIGHT,
        false,
        true
    );

    private static final String TOP_LEFT_CORNER_WALL_PATH =
        GlobalConstants.class.getResource(
            "/org/bioshock/images/walls/topLeftCorner.png"
        ).getPath();
    public static final Image TOP_LEFT_CORNER_WALL_IMAGE = new Image(
        new File(TOP_LEFT_CORNER_WALL_PATH).toURI().toString(),
        UNIT_WIDTH,
        UNIT_HEIGHT,
        false,
        true
    );

    private static final String TOP_RIGHT_CORNER_WALL_PATH =
        GlobalConstants.class.getResource(
            "/org/bioshock/images/walls/topRightCorner.png"
        ).getPath();
    public static final Image TOP_RIGHT_CORNER_WALL_IMAGE = new Image(
        new File(TOP_RIGHT_CORNER_WALL_PATH).toURI().toString(),
        UNIT_WIDTH,
        UNIT_HEIGHT,
        false,
        true
    );

    private static final String VERT_WALL_PATH =
        GlobalConstants.class.getResource(
            "/org/bioshock/images/walls/vert.png"
        ).getPath();
    public static final Image VERT_WALL_IMAGE = new Image(
        new File(VERT_WALL_PATH).toURI().toString(),
        UNIT_WIDTH,
        UNIT_HEIGHT,
        false,
        true
    );

    private static final String OBJECT_DIRECTORY_PATH =
        GlobalConstants.class.getResource(
            "/org/bioshock/images/inRoomObjects"
        ).getPath();
    private static final File OBJECT_DIRECTORY_FILE = new File(
        OBJECT_DIRECTORY_PATH
    );

    /**
     * A list of all possible images for objects in a room
     */
    public static final List<Image> IN_ROOM_OBJECTS = new ArrayList<>(
        OBJECT_DIRECTORY_FILE.list().length
    );
    static {
        for (String fileName : OBJECT_DIRECTORY_FILE.list()) {
            String pathName = GlobalConstants.class.getResource(
                "/org/bioshock/images/inRoomObjects/" + fileName
            ).getPath();

            IN_ROOM_OBJECTS.add(new Image(
                new File(pathName).toURI().toString(),
                UNIT_WIDTH,
                UNIT_HEIGHT,
                false,
                true
            ));
        }
    }


    /**
     * Path to the stylesheet
     */
    public static final String STYLESHEET_PATH =
        GlobalConstants.class.getResource(
            "/org/bioshock/gui/style.css"
        ).toExternalForm();


    /**
     * Private as this is meant to be used as a static class
     */
    private GlobalConstants() {}
}