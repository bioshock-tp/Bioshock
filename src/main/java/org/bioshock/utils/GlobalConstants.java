package org.bioshock.utils;

import org.bioshock.entities.map.utils.RoomType;

import static org.bioshock.entities.map.utils.RoomType.NO_ROOM;
import static org.bioshock.entities.map.utils.RoomType.SINGLE_ROOM;

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
    public static final int  UNIT_WIDTH = 40;
    public static final int  UNIT_HEIGHT = 80;
    
    public static final RoomType[][] testMap = 
       {{B,A,B,B,A,B},
        {B,A,A,B,B,B},
        {B,A,B,B,A,B},
        {B,B,A,B,B,A},
        {B,A,B,B,A,B}};
    
    public static final RoomType[][] simpleMap = 
       {{B,A},
        {B,B}};

    public static final RoomType[][] singletonMap = 
        {{B}};
    private GlobalConstants() {}
}