package org.bioshock.utils;

import static org.bioshock.entities.map.utils.RoomType.NO_ROOM;
import static org.bioshock.entities.map.utils.RoomType.SINGLE_ROOM;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bioshock.entities.map.utils.RoomType;

import javafx.scene.image.Image;

public final class GlobalConstants {
    public static final double PLAYER_WIDTH = 36;
    public static final double PLAYER_HEIGHT = 42;
    public static final double PLAYER_SCALE = 1.6;
    public static final double PLAYER_ANIMATION_SPEED = 0.15;
    static RoomType A = NO_ROOM, B = SINGLE_ROOM;
    public static final RoomType[][] threeByThreeMap = 
    	{{NO_ROOM, SINGLE_ROOM, SINGLE_ROOM, SINGLE_ROOM},
		{NO_ROOM, SINGLE_ROOM, SINGLE_ROOM, SINGLE_ROOM},
		{NO_ROOM, SINGLE_ROOM, SINGLE_ROOM, SINGLE_ROOM}};
    public static final int  UNIT_WIDTH = 59;
    public static final int  UNIT_HEIGHT = 66;
    
    public static final RoomType[][] testMap = 
       {{B,B,B,B,A,B},
        {B,A,A,B,B,B},
        {B,A,B,B,A,B},
        {B,B,A,B,B,A},
        {B,A,B,B,A,B}};
    
    public static final RoomType[][] simpleMap = 
       {{B,A},
        {B,B}};

    public static final RoomType[][] singletonMap = 
        {{B}};
    
    public static final String FLOOR_PATH =
        GlobalConstants.class.getResource("/org/bioshock/images/floors/tile_71.png").getPath();
    
    public static Image FLOOR_IMAGE = new Image(
        new File(FLOOR_PATH).toURI().toString(), 
        UNIT_WIDTH, 
        UNIT_HEIGHT, 
        false, 
        true);
    
    public static final String BOT_HORI_WALL_PATH =
        GlobalConstants.class.getResource("/org/bioshock/images/walls/botHori.png").getPath();
    public static Image BOT_HORI_WALL_IMAGE = new Image(
        new File(BOT_HORI_WALL_PATH).toURI().toString(), 
        UNIT_WIDTH, 
        UNIT_HEIGHT, 
        false, 
        true);
    
    public static final String BOT_LEFT_CORNER_WALL_PATH =
        GlobalConstants.class.getResource("/org/bioshock/images/walls/botLeftCorner.png").getPath();
    public static Image BOT_LEFT_CORNER_WALL_IMAGE = new Image(
        new File(BOT_LEFT_CORNER_WALL_PATH).toURI().toString(), 
        UNIT_WIDTH, 
        UNIT_HEIGHT, 
        false, 
        true);
    
    public static final String BOT_RIGHT_CORNER_WALL_PATH =
        GlobalConstants.class.getResource("/org/bioshock/images/walls/botRightCorner.png").getPath();
    public static Image BOT_RIGHT_CORNER_WALL_IMAGE = new Image(
        new File(BOT_RIGHT_CORNER_WALL_PATH).toURI().toString(), 
        UNIT_WIDTH, 
        UNIT_HEIGHT, 
        false, 
        true);
    
    public static final String TOP_HORI_WALL_PATH =
        GlobalConstants.class.getResource("/org/bioshock/images/walls/topHori.png").getPath();
    public static Image TOP_HORI_WALL_IMAGE = new Image(
        new File(TOP_HORI_WALL_PATH).toURI().toString(), 
        UNIT_WIDTH, 
        UNIT_HEIGHT, 
        false, 
        true);
    
    public static final String TOP_LEFT_CORNER_WALL_PATH =
        GlobalConstants.class.getResource("/org/bioshock/images/walls/topLeftCorner.png").getPath();
    public static Image TOP_LEFT_CORNER_WALL_IMAGE = new Image(
        new File(TOP_LEFT_CORNER_WALL_PATH).toURI().toString(), 
        UNIT_WIDTH, 
        UNIT_HEIGHT, 
        false, 
        true);
    
    public static final String TOP_RIGHT_CORNER_WALL_PATH =
        GlobalConstants.class.getResource("/org/bioshock/images/walls/topRightCorner.png").getPath();
    public static Image TOP_RIGHT_CORNER_WALL_IMAGE = new Image(
        new File(TOP_RIGHT_CORNER_WALL_PATH).toURI().toString(), 
        UNIT_WIDTH, 
        UNIT_HEIGHT, 
        false, 
        true);
    
    public static final String VERT_WALL_PATH =
        GlobalConstants.class.getResource("/org/bioshock/images/walls/vert.png").getPath();
    public static Image VERT_WALL_IMAGE = new Image(
        new File(VERT_WALL_PATH).toURI().toString(), 
        UNIT_WIDTH, 
        UNIT_HEIGHT, 
        false, 
        true);
    
    private static final String OBJECT_DIRECTORY_PATH = 
            GlobalConstants.class.getResource("/org/bioshock/images/inRoomObjects").getPath();
    private static final File OBJECT_DIRECTORY_FILE= new File(OBJECT_DIRECTORY_PATH);
    
    
    public static List<Image> inRoomObjects = new ArrayList<>();
    static {
        for(String fileName : OBJECT_DIRECTORY_FILE.list()) {
            String pathName = 
                GlobalConstants.class.getResource("/org/bioshock/images/inRoomObjects/" + fileName).getPath();
            
            inRoomObjects.add(new Image(
                new File(pathName).toURI().toString(), 
                UNIT_WIDTH, 
                UNIT_HEIGHT, 
                false, 
                true));
        }
    }
    
    
    private GlobalConstants() {}
}