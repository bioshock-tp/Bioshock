package org.bioshock.entities.map;

import java.util.ArrayList;

import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.entity.Entity;
import org.bioshock.engine.entity.Size;
import org.bioshock.entities.TexRectEntity;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

public class Sides {
	public static ArrayList<TexRectEntity> lExit(Point3D pos, int wallWidth, int roomHeight, int coriWidth, int coriLen, Color c){
		ArrayList<TexRectEntity> walls = new ArrayList<>();
		
		TexRectEntity roomWall1 = 
			new TexRectEntity(
				pos.add(coriLen - wallWidth, 0, 0), 
				new NetworkC(false), 
				new Size(wallWidth, (roomHeight - coriWidth)/2 - wallWidth), 			
				c
			);
		walls.add(roomWall1);
		
		TexRectEntity roomWall2 = 
			new TexRectEntity(
				pos.add(coriLen - wallWidth, (roomHeight + coriWidth)/2 + wallWidth, 0), 
				new NetworkC(false), 
				new Size(wallWidth, (roomHeight - coriWidth)/2 - wallWidth), 			
				c
			);
		walls.add(roomWall2);
		
		TexRectEntity coriWall1 =
			new TexRectEntity(
				pos.add(0, (roomHeight - coriWidth)/2 - wallWidth, 0), 
				new NetworkC(false), 
				new Size(coriLen - wallWidth, wallWidth), 			
				c
			);
		walls.add(coriWall1);
		
		TexRectEntity coriWall2 =
			new TexRectEntity(
				pos.add(0, (roomHeight + coriWidth)/2, 0), 
				new NetworkC(false), 
				new Size(coriLen - wallWidth, wallWidth), 			
				c
			);
		walls.add(coriWall2);
				
		TexRectEntity cor1 =
			new TexRectEntity(
				pos.add(coriLen - wallWidth, (roomHeight - coriWidth)/2 - wallWidth, 0), 
				new NetworkC(false), 
				new Size(wallWidth, wallWidth), 			
				c
			);
		walls.add(cor1);
		
		TexRectEntity cor2 =
			new TexRectEntity(
				pos.add(coriLen - wallWidth, (roomHeight + coriWidth)/2, 0), 
				new NetworkC(false), 
				new Size(wallWidth, wallWidth), 			
				c
			);
		walls.add(cor2);
		
		return walls;
	}
	
	public static ArrayList<TexRectEntity> rExit(Point3D pos, int wallWidth, int roomHeight, int coriWidth, int coriLen, Color c){
		ArrayList<TexRectEntity> walls = new ArrayList<>();
		TexRectEntity roomWall1 = 
			new TexRectEntity(
				pos.add(0, 0, 0), 
				new NetworkC(false), 
				new Size(wallWidth, (roomHeight - coriWidth)/2 - wallWidth), 			
				c
			);
		walls.add(roomWall1);
		
		TexRectEntity roomWall2 = 
			new TexRectEntity(
				pos.add(0, (roomHeight + coriWidth)/2 + wallWidth, 0), 
				new NetworkC(false), 
				new Size(wallWidth, (roomHeight - coriWidth)/2 - wallWidth), 			
				c
			);
		walls.add(roomWall2);
		
		TexRectEntity coriWall1 =
			new TexRectEntity(
				pos.add(wallWidth, (roomHeight - coriWidth)/2 - wallWidth, 0), 
				new NetworkC(false), 
				new Size(coriLen - wallWidth, wallWidth), 			
				c
			);
		walls.add(coriWall1);
		
		TexRectEntity coriWall2 =
			new TexRectEntity(
				pos.add(wallWidth, (roomHeight + coriWidth)/2, 0), 
				new NetworkC(false), 
				new Size(coriLen - wallWidth, wallWidth), 			
				c
			);
		walls.add(coriWall2);
				
		TexRectEntity cor1 =
			new TexRectEntity(
				pos.add(0, (roomHeight - coriWidth)/2 - wallWidth, 0), 
				new NetworkC(false), 
				new Size(wallWidth, wallWidth), 			
				c
			);
		walls.add(cor1);
		
		TexRectEntity cor2 =
			new TexRectEntity(
				pos.add(0, (roomHeight + coriWidth)/2, 0), 
				new NetworkC(false), 
				new Size(wallWidth, wallWidth), 			
				c
			);
		walls.add(cor2);

		return walls;
	}
	
	public static ArrayList<TexRectEntity> tExit(Point3D pos, int wallWidth, int roomWidth, int coriWidth, int coriLen, Color c){
		ArrayList<TexRectEntity> walls = new ArrayList<>();
		TexRectEntity roomWall1 = 
			new TexRectEntity(
				pos.add(0, coriLen - wallWidth, 0), 
				new NetworkC(false), 
				new Size((roomWidth - coriWidth)/2 - wallWidth, wallWidth), 			
				c
			);
		walls.add(roomWall1);
		
		TexRectEntity roomWall2 = 
			new TexRectEntity(
				pos.add((roomWidth + coriWidth)/2 + wallWidth, coriLen - wallWidth, 0), 
				new NetworkC(false), 
				new Size((roomWidth - coriWidth)/2 - wallWidth, wallWidth), 			
				c
			);
		walls.add(roomWall2);
		
		TexRectEntity coriWall1 =
			new TexRectEntity(
				pos.add((roomWidth - coriWidth)/2 - wallWidth, 0, 0), 
				new NetworkC(false), 
				new Size(wallWidth, coriLen - wallWidth), 			
				c
			);
		walls.add(coriWall1);
		
		TexRectEntity coriWall2 =
			new TexRectEntity(
				pos.add((roomWidth + coriWidth)/2, 0, 0), 
				new NetworkC(false), 
				new Size(wallWidth, coriLen - wallWidth), 			
				c
			);
		walls.add(coriWall2);
				
		TexRectEntity cor1 =
			new TexRectEntity(
				pos.add((roomWidth - coriWidth)/2 - wallWidth, coriLen - wallWidth, 0), 
				new NetworkC(false), 
				new Size(wallWidth, wallWidth), 			
				c
			);
		walls.add(cor1);
		
		TexRectEntity cor2 =
			new TexRectEntity(
				pos.add((roomWidth + coriWidth)/2, coriLen - wallWidth, 0), 
				new NetworkC(false), 
				new Size(wallWidth, wallWidth), 			
				c
			);
		walls.add(cor2);
		
		return walls;
	}
	
	public static ArrayList<TexRectEntity> bExit(Point3D pos, int wallWidth, int roomWidth, int coriWidth, int coriLen, Color c){
		ArrayList<TexRectEntity> walls = new ArrayList<>();
		TexRectEntity roomWall1 = 
			new TexRectEntity(
				pos.add(0, 0, 0), 
				new NetworkC(false), 
				new Size((roomWidth - coriWidth)/2 - wallWidth, wallWidth), 			
				c
			);
		walls.add(roomWall1);
		
		TexRectEntity roomWall2 = 
			new TexRectEntity(
				pos.add((roomWidth + coriWidth)/2 + wallWidth, 0, 0), 
				new NetworkC(false), 
				new Size((roomWidth - coriWidth)/2 - wallWidth, wallWidth), 			
				c
			);
		walls.add(roomWall2);
		
		TexRectEntity coriWall1 =
			new TexRectEntity(
				pos.add((roomWidth - coriWidth)/2 - wallWidth, wallWidth, 0), 
				new NetworkC(false), 
				new Size(wallWidth, coriLen - wallWidth), 			
				c
			);
		walls.add(coriWall1);
		
		TexRectEntity coriWall2 =
			new TexRectEntity(
				pos.add((roomWidth + coriWidth)/2, wallWidth, 0), 
				new NetworkC(false), 
				new Size(wallWidth, coriLen - wallWidth), 			
				c
			);
		walls.add(coriWall2);
				
		TexRectEntity cor1 =
			new TexRectEntity(
				pos.add((roomWidth - coriWidth)/2 - wallWidth, 0, 0), 
				new NetworkC(false), 
				new Size(wallWidth, wallWidth), 			
				c
			);
		walls.add(cor1);
		
		TexRectEntity cor2 =
			new TexRectEntity(
				pos.add((roomWidth + coriWidth)/2, 0, 0), 
				new NetworkC(false), 
				new Size(wallWidth, wallWidth), 			
				c
			);
		walls.add(cor2);
		
		return walls;
	}
	
	public static ArrayList<TexRectEntity> lNoExit(Point3D pos, int wallWidth, int roomHeight, int coriWidth, int coriLen, Color c){
		ArrayList<TexRectEntity> walls = new ArrayList<>();
		TexRectEntity wall = 
			new TexRectEntity(
				pos.add(coriLen - wallWidth, 0, 0), 
				new NetworkC(false), 
				new Size(wallWidth, roomHeight), 			
				c
			);
		walls.add(wall);
		
		return walls;
	}
	
	public static ArrayList<TexRectEntity> rNoExit(Point3D pos, int wallWidth, int roomHeight, int coriWidth, int coriLen, Color c){
		ArrayList<TexRectEntity> walls = new ArrayList<>();
		TexRectEntity wall = 
			new TexRectEntity(
				pos.add(0, 0, 0), 
				new NetworkC(false), 
				new Size(wallWidth, roomHeight), 			
				c
			);
		walls.add(wall);
		
		return walls;
	}
	
	public static ArrayList<TexRectEntity> tNoExit(Point3D pos, int wallWidth, int roomWidth, int coriWidth, int coriLen, Color c){
		ArrayList<TexRectEntity> walls = new ArrayList<>();
		TexRectEntity wall = 
			new TexRectEntity(
				pos.add(0, coriLen - wallWidth, 0), 
				new NetworkC(false), 
				new Size(roomWidth, wallWidth), 			
				c
			);
		walls.add(wall);
		
		return walls;
	}
	
	public static ArrayList<TexRectEntity> bNoExit(Point3D pos, int wallWidth, int roomWidth, int coriWidth, int coriLen, Color c){
		ArrayList<TexRectEntity> walls = new ArrayList<>();
		TexRectEntity wall = 
			new TexRectEntity(
				pos.add(0, 0, 0), 
				new NetworkC(false), 
				new Size(roomWidth, wallWidth), 			
				c
			);
		walls.add(wall);
		
		return walls;
	}
}
