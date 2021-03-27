package org.bioshock.utils;

import static org.bioshock.entities.map.RoomType.*;

import org.bioshock.entities.map.RoomType;

public final class GlobalConstants {
    public static final double PLAYER_WIDTH = 18;
    public static final double PLAYER_HEIGHT = 21;
    public static final int PLAYER_SCALE = 3;
    public static final double PLAYER_ANIMATION_SPEED = 0.15;
    public static final RoomType[][] threeByThreeMap = 
    	{{NO_ROOM, SINGLE_ROOM, SINGLE_ROOM, SINGLE_ROOM},
		{NO_ROOM, SINGLE_ROOM, SINGLE_ROOM, SINGLE_ROOM},
		{NO_ROOM, SINGLE_ROOM, SINGLE_ROOM, SINGLE_ROOM}};
    

    private GlobalConstants() {}
}