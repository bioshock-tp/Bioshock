package org.bioshock.entities.map;

import java.util.ArrayList;

import org.bioshock.engine.entity.Size;
import org.bioshock.entities.TexRectEntity;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

public class ThreeByThreeMap {
	private ArrayList<Room> rooms = new ArrayList<>();
	
	public ArrayList<Room> getRooms() {
		return rooms;
	}
	
	public ArrayList<TexRectEntity> getWalls() {
		ArrayList<TexRectEntity> walls = new ArrayList<>();
		for (Room r : rooms) {
			walls.addAll(r.getWalls());
		}
		return walls;
	}
	
	public ThreeByThreeMap(Point3D newPos, int wallWidth, Size newRoomSize, Size coriSize, Color c) {
		Room firstRoom = new Room(newPos, wallWidth, newRoomSize, coriSize, 
				new Exits(false, true, false, true), 
				c);
		rooms.add(firstRoom);
		
		Size tRoomSize = firstRoom.getTotalSize();
		int tRoomWidth = tRoomSize.getWidth();
		int tRoomHeight = tRoomSize.getHeight();
		
		rooms.add(new Room(newPos.add(tRoomWidth, 0, 0), wallWidth, newRoomSize, coriSize, 
				new Exits(false, true, true, true), 
				c));
		rooms.add(new Room(newPos.add(tRoomWidth*2, 0, 0), wallWidth, newRoomSize, coriSize, 
				new Exits(false, true, true, false), 
				c));
		rooms.add(new Room(newPos.add(0, tRoomHeight, 0), wallWidth, newRoomSize, coriSize, 
				new Exits(true, true, false, true), 
				c));
		rooms.add(new Room(newPos.add(tRoomWidth, tRoomHeight, 0), wallWidth, newRoomSize, coriSize, 
				new Exits(true, true, true, true), 
				c));
		rooms.add(new Room(newPos.add(tRoomWidth*2, tRoomHeight, 0), wallWidth, newRoomSize, coriSize, 
				new Exits(true, true, true, false), 
				c));
		rooms.add(new Room(newPos.add(0, tRoomHeight*2, 0), wallWidth, newRoomSize, coriSize, 
				new Exits(true, false, false, true), 
				c));
		rooms.add(new Room(newPos.add(tRoomWidth, tRoomHeight*2, 0), wallWidth, newRoomSize, coriSize, 
				new Exits(true, false, true, true), 
				c));
		rooms.add(new Room(newPos.add(tRoomWidth*2, tRoomHeight*2, 0), wallWidth, newRoomSize, coriSize, 
				new Exits(true, false, true, false), 
				c));
	}
}
