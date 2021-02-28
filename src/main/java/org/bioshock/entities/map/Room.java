package org.bioshock.entities.map;

import java.util.ArrayList;

import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.entity.Size;
import org.bioshock.entities.TexRectEntity;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

public class Room {
	private Size totalSize;
	private Size roomSize;
	private ArrayList<TexRectEntity> walls = new ArrayList<>();
	private double z;
	private Point3D pos;
	
	public Room(Point3D newPos, int wallWidth, Size newRoomSize, Size coriSize, Exits exits, Color c) {
		this.pos = newPos;
		this.z = pos.getZ();
		this.roomSize = newRoomSize;
		this.totalSize = new Size(roomSize.getWidth() + 2*coriSize.getHeight(), roomSize.getHeight() + 2*coriSize.getHeight());
					
		if (exits.isTop()) { 
			walls.addAll(Sides.tExit(pos.add(coriSize.getHeight(), 0, 0), 
					wallWidth, roomSize.getWidth(), coriSize.getWidth(), coriSize.getHeight(), c )); 
		}
		else {
			walls.addAll(Sides.tNoExit(pos.add(coriSize.getHeight(), 0, 0), 
					wallWidth, roomSize.getWidth(), coriSize.getWidth(), coriSize.getHeight(), c ));
		}
		  
		if (exits.isBot()) {
			walls.addAll(Sides.bExit(pos.add(coriSize.getHeight(), coriSize.getHeight() + roomSize.getHeight(), 0), 
					wallWidth, roomSize.getWidth(), coriSize.getWidth(), coriSize.getHeight(), c ));
		}
		else {
			walls.addAll(Sides.bNoExit(pos.add(coriSize.getHeight(), coriSize.getHeight() + roomSize.getHeight(), 0), 
					wallWidth, roomSize.getWidth(), coriSize.getWidth(), coriSize.getHeight(), c ));
		}
		  
		if (exits.isLeft()) {
			walls.addAll(Sides.lExit(pos.add(0, coriSize.getHeight(), 0), 
					wallWidth, roomSize.getHeight(), coriSize.getWidth(), coriSize.getHeight(), c ));
		}
		else {
			walls.addAll(Sides.lNoExit(pos.add(0, coriSize.getHeight(), 0), 
					wallWidth, roomSize.getHeight(), coriSize.getWidth(), coriSize.getHeight(), c ));
		}
		
		if (exits.isRight()) {
			walls.addAll(Sides.rExit(pos.add(coriSize.getHeight() + roomSize.getWidth(), coriSize.getHeight(), 0), 
					wallWidth, roomSize.getHeight(), coriSize.getWidth(), coriSize.getHeight(), c ));
		}
		else {
			walls.addAll(Sides.rNoExit(pos.add(coriSize.getHeight() + roomSize.getWidth(), coriSize.getHeight(), 0), 
					wallWidth, roomSize.getHeight(), coriSize.getWidth(), coriSize.getHeight(), c ));
		}
		
		TexRectEntity corner1 = new TexRectEntity(
				pos.add(coriSize.getHeight() - wallWidth, coriSize.getHeight() - wallWidth, 0), 
				new NetworkC(false), 
				new Size(wallWidth, wallWidth), 
				c);
		walls.add(corner1);
		
		TexRectEntity corner2 = new TexRectEntity(
				pos.add(coriSize.getHeight() + roomSize.getWidth(), coriSize.getHeight() - wallWidth, 0), 
				new NetworkC(false), 
				new Size(wallWidth, wallWidth), 
				c);
		walls.add(corner2);
		
		TexRectEntity corner3 = new TexRectEntity(
				pos.add(coriSize.getHeight() - wallWidth, coriSize.getHeight() + roomSize.getHeight(), 0), 
				new NetworkC(false), 
				new Size(wallWidth, wallWidth), 
				c);
		walls.add(corner3);
		
		TexRectEntity corner4 = new TexRectEntity(
				pos.add(coriSize.getHeight() + roomSize.getWidth(), coriSize.getHeight() + roomSize.getHeight(), 0), 
				new NetworkC(false), 
				new Size(wallWidth, wallWidth), 
				c);
		walls.add(corner4);
	}

	public ArrayList<TexRectEntity> getWalls() {
		return walls;
	}
	
	public Size getTotalSize() {
		return totalSize;
	}

	public Size getRoomSize() {
		return roomSize;
	}

	public double getZ() {
		return z;
	}
	
	public void setZ(double newZ) {
		for (TexRectEntity e : walls) {
			e.getRendererC().setZ(z);
		}
	}
	
	public Point3D getRoomCenter() {
		return pos.add(totalSize.getWidth()/2, totalSize.getHeight()/2, 0);
	}
}
