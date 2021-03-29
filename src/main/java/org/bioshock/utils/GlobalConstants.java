package org.bioshock.utils;

import static org.bioshock.entities.map.RoomType.*;

import org.bioshock.entities.map.RoomType;

public final class GlobalConstants {
    public static final double PLAYER_WIDTH = 18;
    public static final double PLAYER_HEIGHT = 21;
    public static final int PLAYER_SCALE = 3;
    public static final double PLAYER_ANIMATION_SPEED = 0.15;
    static RoomType A = NO_ROOM, B = SINGLE_ROOM;
    public static final RoomType[][] threeByThreeMap = 
    	{{NO_ROOM, SINGLE_ROOM, SINGLE_ROOM, SINGLE_ROOM},
		{NO_ROOM, SINGLE_ROOM, SINGLE_ROOM, SINGLE_ROOM},
		{NO_ROOM, SINGLE_ROOM, SINGLE_ROOM, SINGLE_ROOM}};
    
    public static final RoomType[][] testMap = 
       {{B,A,B,B,A,B},
        {B,A,A,B,B,B},
        {B,A,B,B,A,B},
        {B,B,A,B,B,A},
        {B,A,B,B,A,B}};

    private GlobalConstants() {}
}