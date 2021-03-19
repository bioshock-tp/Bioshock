package org.bioshock.entities.map;

import java.util.ArrayList;
import java.util.List;

import org.bioshock.utils.Size;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

public class ThreeByThreeMap {
	//stores the rooms in a map
	private List<Room> rooms = new ArrayList<>();

	/***
	 * Generates a new ThreeByThreeMap
	 * @param newPos the top left position of the map
	 * @param wallWidth the width of the walls that make up the map
	 * @param newRoomSize the size of the internal room
	 * @param coriSize the size of all corridors
	 * @param c the colour of the map
	 */
	public ThreeByThreeMap(
        Point3D newPos,
        double wallWidth,
        Size newRoomSize,
        Size coriSize,
        Color c
    ) {
		//generate the top left room
		Room firstRoom = new Room(
            newPos, wallWidth, newRoomSize, coriSize,
            new Exits(false, true, false, true),
            c
        );
		rooms.add(firstRoom);

		//get the total room size of each room
		//Note all the rooms are the same size in this configuration
		Size tRoomSize = firstRoom.getTotalSize();
		double tRoomWidth = tRoomSize.getWidth();
		double tRoomHeight = tRoomSize.getHeight();

		/*
		 * The rooms are generated out in the order
		 * 1, 2, 3
		 * 4, 5, 6
		 * 7, 8, 9
		 */

		//2
		rooms.add(new Room(
            newPos.add(tRoomWidth, 0, 0), wallWidth, newRoomSize, coriSize,
            new Exits(false, true, true, true),
            c)
        );

		//3
		rooms.add(new Room(
            newPos.add(tRoomWidth * 2, 0, 0), wallWidth, newRoomSize, coriSize,
            new Exits(false, true, true, false),
            c)
        );

		//4
		rooms.add(new Room(
            newPos.add(0, tRoomHeight, 0), wallWidth, newRoomSize, coriSize,
            new Exits(true, true, false, true),
            c)
        );

		//5
		rooms.add(new Room(
            newPos.add(tRoomWidth, tRoomHeight, 0),
            wallWidth,
            newRoomSize,
            coriSize,
            new Exits(true, true, true, true),
            c)
        );

		//6
		rooms.add(new Room(
            newPos.add(tRoomWidth * 2, tRoomHeight, 0),
            wallWidth,
            newRoomSize,
            coriSize,
            new Exits(true, true, true, false),
            c)
        );

		//7
		rooms.add(new Room(
            newPos.add(0, tRoomHeight*2, 0), wallWidth, newRoomSize, coriSize,
            new Exits(true, false, false, true),
            c)
        );

		//8
		rooms.add(new Room(
            newPos.add(tRoomWidth, tRoomHeight * 2, 0),
            wallWidth,
            newRoomSize,
            coriSize,
            new Exits(true, false, true, true),
            c)
        );

		//9
		rooms.add(new Room(
            newPos.add(tRoomWidth * 2, tRoomHeight * 2, 0),
            wallWidth,
            newRoomSize,
            coriSize,
            new Exits(true, false, true, false),
            c)
        );

		setAdjacents();
	}

	private void setAdjacents() {
		for(Room room : rooms) {
			double x = room.getRoomCenter().getX();
			double y = room.getRoomCenter().getY();
			for(Room adj : rooms) {
				if(adj != room) {
					double xa = adj.getRoomCenter().getX();
					double ya = adj.getRoomCenter().getY();

					double roomH = room.getTotalSize().getHeight();
					double roomW = room.getTotalSize().getWidth();
					if(x == xa) {
						if((ya-y) == -roomH) {
							room.addAdjacent(adj, 0);
						}
						else if((ya-y) == roomH) {
							room.addAdjacent(adj, 1);
						}
					}
					else if(y == ya) {
						if((xa-x) == -roomW) {
							room.addAdjacent(adj, 2);
						}
						else if((xa-x) == roomW) {
							room.addAdjacent(adj, 3);
						}
					}

				}
			}
		}
	}

	/***
	 *
	 * @return the rooms in a map
	 */
	public List<Room> getRooms() {
		return rooms;
	}

	/***
	 *
	 * @return all the walls that make up a map
	 */
	public List<TexRectEntity> getWalls() {
		List<TexRectEntity> walls = new ArrayList<>();
		for (Room r : rooms) {
			walls.addAll(r.getWalls());
		}
		return walls;
	}
}
