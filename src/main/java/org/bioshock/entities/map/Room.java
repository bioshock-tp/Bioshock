package org.bioshock.entities.map;

import java.util.ArrayList;
import java.util.List;

import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.entity.Size;
import org.bioshock.entities.TexRectEntity;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

public class Room {
	private Size totalSize;
	private Size roomSize;
	private List<TexRectEntity> walls = new ArrayList<>();
	private double z;
	private Point3D pos;

	/***
	 * Generates a room with the position being the top left of the room
	 * @param newPos the position of the top left of the room
	 * @param wallWidth the width of the walls that make up the room
	 * @param newRoomSize the size of the central room
	 * @param coriSize the corridor size of all exits
	 * @param exits the exits the room has
	 * @param c the colour of the room
	 */
	public Room(
        Point3D newPos,
        double wallWidth,
        Size newRoomSize,
        Size coriSize,
        Exits exits,
        Color c
    ) {
		this.pos = newPos;
		this.z = pos.getZ();
		this.roomSize = newRoomSize;
		this.totalSize = new Size(
            roomSize.getWidth() + 2 * coriSize.getHeight(),
            roomSize.getHeight() + 2 * coriSize.getHeight()
        );

		//chooses either a top side with or without an exit
		if (exits.isTop()) {
			walls.addAll(Sides.tExit(
                pos.add(coriSize.getHeight(), 0, 0),
				wallWidth,
                roomSize.getWidth(),
                coriSize.getWidth(),
                coriSize.getHeight(),
                c
            ));
		}
		else {
			walls.addAll(Sides.tNoExit(
                pos.add(coriSize.getHeight(), 0, 0),
				wallWidth,
                roomSize.getWidth(),
                coriSize.getWidth(),
                coriSize.getHeight(),
                c
            ));
		}

		//chooses either a bottom side with or without an exit
		if (exits.isBot()) {
			walls.addAll(Sides.bExit(
                pos.add(
                    coriSize.getHeight(),
                    coriSize.getHeight() + roomSize.getHeight(),
                    0
                ),
				wallWidth,
                roomSize.getWidth(),
                coriSize.getWidth(),
                coriSize.getHeight(),
                c
            ));
		}
		else {
			walls.addAll(Sides.bNoExit(
                pos.add(
                    coriSize.getHeight(),
                    coriSize.getHeight() + roomSize.getHeight(),
                    0
                ),
				wallWidth,
                roomSize.getWidth(),
                coriSize.getWidth(),
                coriSize.getHeight(),
                c
            ));
		}

		//chooses either a left side with or without an exit
		if (exits.isLeft()) {
			walls.addAll(Sides.lExit(
                pos.add(0, coriSize.getHeight(), 0),
				wallWidth, roomSize.getHeight(),
                coriSize.getWidth(),
                coriSize.getHeight(),
                c
            ));
		}
		else {
			walls.addAll(Sides.lNoExit(
                pos.add(0, coriSize.getHeight(), 0),
				wallWidth,
                roomSize.getHeight(),
                coriSize.getWidth(),
                coriSize.getHeight(),
                c
            ));
		}

		//chooses either a right side with or without an exit
		if (exits.isRight()) {
			walls.addAll(Sides.rExit(
                pos.add(
                    coriSize.getHeight() + roomSize.getWidth(),
                    coriSize.getHeight(),
                    0
                ),
				wallWidth,
                roomSize.getHeight(),
                coriSize.getWidth(),
                coriSize.getHeight(),
                c
            ));
		}
		else {
			walls.addAll(Sides.rNoExit(
                pos.add(
                    coriSize.getHeight() + roomSize.getWidth(),
                    coriSize.getHeight(),
                    0
                ),
				wallWidth,
                roomSize.getHeight(),
                coriSize.getWidth(),
                coriSize.getHeight(),
                c
            ));
		}

		//corner connecting bottom and right
		TexRectEntity corner1 = new TexRectEntity(
				pos.add(
                    coriSize.getHeight() - wallWidth,
                    coriSize.getHeight() - wallWidth,
                    0
                ),
				new NetworkC(false),
				new Size(wallWidth, wallWidth),
            c
        );
		walls.add(corner1);

		//corner connecting bottom and left
		TexRectEntity corner2 = new TexRectEntity(
				pos.add(
                    coriSize.getHeight() + roomSize.getWidth(),
                    coriSize.getHeight() - wallWidth,
                    0
                ),
				new NetworkC(false),
				new Size(wallWidth, wallWidth),
            c
        );
		walls.add(corner2);

		//corner connecting top and right
		TexRectEntity corner3 = new TexRectEntity(
				pos.add(
                    coriSize.getHeight() - wallWidth,
                    coriSize.getHeight() + roomSize.getHeight(),
                    0
                ),
				new NetworkC(false),
				new Size(wallWidth, wallWidth),
            c
        );
		walls.add(corner3);

		//corner connecting top and left
		TexRectEntity corner4 = new TexRectEntity(
				pos.add(
                    coriSize.getHeight() + roomSize.getWidth(),
                    coriSize.getHeight() + roomSize.getHeight(),
                    0
                ),
				new NetworkC(false),
				new Size(wallWidth, wallWidth),
            c
        );
		walls.add(corner4);
	}

	/***
	 *
	 * @return The walls that make up the room
	 */
	public List<TexRectEntity> getWalls() {
		return walls;
	}

	/***
	 *
	 * @return total size of the room with all the sides
	 */
	public Size getTotalSize() {
		return totalSize;
	}

	/***
	 *
	 * @return size of the internal room
	 */
	public Size getRoomSize() {
		return roomSize;
	}


	public double getZ() {
		return z;
	}

	/***
	 * sets the Z value of all the walls in the room to the newZ
	 * @param newZ
	 */
	public void setZ(double newZ) {
		for (TexRectEntity e : walls) {
			e.getRendererC().setZ(newZ);
		}
	}

	/***
	 *
	 * @return the centre of the room for AI purposes
	 */
	public Point3D getRoomCenter() {
		return pos.add(
            totalSize.getWidth() / 2,
            totalSize.getHeight() / 2,
            0
        );
	}
}
