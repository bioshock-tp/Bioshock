package org.bioshock.entities.map;

import java.util.ArrayList;

import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.entity.Entity;
import org.bioshock.engine.entity.Size;
import org.bioshock.entities.TexRectEntity;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

public class Sides {
	/***
	 * A left side with an exit
	 * @param pos the position of the top left of the side
	 * @param wallWidth the width of the walls that make up the room
	 * @param roomHeight the height of the central room
	 * @param coriWidth the width of the corridor
	 * @param coriLen the length of the corridor
	 * @param c the colour of the room
	 * @return the walls that make up the side
	 */
	public static ArrayList<TexRectEntity> lExit(Point3D pos, int wallWidth, int roomHeight, int coriWidth, int coriLen, Color c){
		ArrayList<TexRectEntity> walls = new ArrayList<>();
		//vertical wall
		TexRectEntity roomWall1 = 
			new TexRectEntity(
				pos.add(coriLen - wallWidth, 0, 0), 
				new NetworkC(false), 
				new Size(wallWidth, (roomHeight - coriWidth)/2 - wallWidth), 			
				c
			);
		walls.add(roomWall1);
		
		//vertical wall
		TexRectEntity roomWall2 = 
			new TexRectEntity(
				pos.add(coriLen - wallWidth, (roomHeight + coriWidth)/2 + wallWidth, 0), 
				new NetworkC(false), 
				new Size(wallWidth, (roomHeight - coriWidth)/2 - wallWidth), 			
				c
			);
		walls.add(roomWall2);
		
		//top wall
		TexRectEntity coriWall1 =
			new TexRectEntity(
				pos.add(0, (roomHeight - coriWidth)/2 - wallWidth, 0), 
				new NetworkC(false), 
				new Size(coriLen - wallWidth, wallWidth), 			
				c
			);
		walls.add(coriWall1);
		
		//bottom wall
		TexRectEntity coriWall2 =
			new TexRectEntity(
				pos.add(0, (roomHeight + coriWidth)/2, 0), 
				new NetworkC(false), 
				new Size(coriLen - wallWidth, wallWidth), 			
				c
			);
		walls.add(coriWall2);
		
		//corner connecting top and left
		TexRectEntity cor1 =
			new TexRectEntity(
				pos.add(coriLen - wallWidth, (roomHeight - coriWidth)/2 - wallWidth, 0), 
				new NetworkC(false), 
				new Size(wallWidth, wallWidth), 			
				c
			);
		walls.add(cor1);

		//corner connecting bottom and left
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
	
	/***
	 * A left side with an exit
	 * @param pos the position of the top left of the side
	 * @param wallWidth the width of the walls that make up the room
	 * @param roomHeight the height of the central room
	 * @param coriWidth the width of the corridor
	 * @param coriLen the length of the corridor
	 * @param c the colour of the room
	 * @return the walls that make up the side
	 */
	public static ArrayList<TexRectEntity> rExit(Point3D pos, int wallWidth, int roomHeight, int coriWidth, int coriLen, Color c){
		ArrayList<TexRectEntity> walls = new ArrayList<>();
		//vertical wall
		TexRectEntity roomWall1 = 
			new TexRectEntity(
				pos.add(0, 0, 0), 
				new NetworkC(false), 
				new Size(wallWidth, (roomHeight - coriWidth)/2 - wallWidth), 			
				c
			);
		walls.add(roomWall1);
		
		//vertical wall
		TexRectEntity roomWall2 = 
			new TexRectEntity(
				pos.add(0, (roomHeight + coriWidth)/2 + wallWidth, 0), 
				new NetworkC(false), 
				new Size(wallWidth, (roomHeight - coriWidth)/2 - wallWidth), 			
				c
			);
		walls.add(roomWall2);
		
		//top wall
		TexRectEntity coriWall1 =
			new TexRectEntity(
				pos.add(wallWidth, (roomHeight - coriWidth)/2 - wallWidth, 0), 
				new NetworkC(false), 
				new Size(coriLen - wallWidth, wallWidth), 			
				c
			);
		walls.add(coriWall1);
		
		//bottom wall
		TexRectEntity coriWall2 =
			new TexRectEntity(
				pos.add(wallWidth, (roomHeight + coriWidth)/2, 0), 
				new NetworkC(false), 
				new Size(coriLen - wallWidth, wallWidth), 			
				c
			);
		walls.add(coriWall2);

		//corner connecting top and right		
		TexRectEntity cor1 =
			new TexRectEntity(
				pos.add(0, (roomHeight - coriWidth)/2 - wallWidth, 0), 
				new NetworkC(false), 
				new Size(wallWidth, wallWidth), 			
				c
			);
		walls.add(cor1);

		//corner connecting bottom and right
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
	
	/***
	 * A top side with an exit
	 * @param pos the position of the top left of the side
	 * @param wallWidth the width of the walls that make up the room
	 * @param roomWidth the width of the central room
	 * @param coriWidth the width of the corridor
	 * @param coriLen the length of the corridor
	 * @param c the colour of the room
	 * @return the walls that make up the side
	 */
	public static ArrayList<TexRectEntity> tExit(Point3D pos, int wallWidth, int roomWidth, int coriWidth, int coriLen, Color c){
		ArrayList<TexRectEntity> walls = new ArrayList<>();
		//top wall
		TexRectEntity roomWall1 = 
			new TexRectEntity(
				pos.add(0, coriLen - wallWidth, 0), 
				new NetworkC(false), 
				new Size((roomWidth - coriWidth)/2 - wallWidth, wallWidth), 			
				c
			);
		walls.add(roomWall1);
		
		//top wall
		TexRectEntity roomWall2 = 
			new TexRectEntity(
				pos.add((roomWidth + coriWidth)/2 + wallWidth, coriLen - wallWidth, 0), 
				new NetworkC(false), 
				new Size((roomWidth - coriWidth)/2 - wallWidth, wallWidth), 			
				c
			);
		walls.add(roomWall2);
		
		//vertical wall
		TexRectEntity coriWall1 =
			new TexRectEntity(
				pos.add((roomWidth - coriWidth)/2 - wallWidth, 0, 0), 
				new NetworkC(false), 
				new Size(wallWidth, coriLen - wallWidth), 			
				c
			);
		walls.add(coriWall1);
		
		//vertical wall
		TexRectEntity coriWall2 =
			new TexRectEntity(
				pos.add((roomWidth + coriWidth)/2, 0, 0), 
				new NetworkC(false), 
				new Size(wallWidth, coriLen - wallWidth), 			
				c
			);
		walls.add(coriWall2);
		
		//corner connecting top and left
		TexRectEntity cor1 =
			new TexRectEntity(
				pos.add((roomWidth - coriWidth)/2 - wallWidth, coriLen - wallWidth, 0), 
				new NetworkC(false), 
				new Size(wallWidth, wallWidth), 			
				c
			);
		walls.add(cor1);
		
		//corner connecting top and right
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
	
	/***
	 * A bottom side with an exit
	 * @param pos the position of the top left of the side
	 * @param wallWidth the width of the walls that make up the room
	 * @param roomWidth the width of the central room
	 * @param coriWidth the width of the corridor
	 * @param coriLen the length of the corridor
	 * @param c the colour of the room
	 * @return the walls that make up the side
	 */
	public static ArrayList<TexRectEntity> bExit(Point3D pos, int wallWidth, int roomWidth, int coriWidth, int coriLen, Color c){
		ArrayList<TexRectEntity> walls = new ArrayList<>();
		//bottom wall
		TexRectEntity roomWall1 = 
			new TexRectEntity(
				pos.add(0, 0, 0), 
				new NetworkC(false), 
				new Size((roomWidth - coriWidth)/2 - wallWidth, wallWidth), 			
				c
			);
		walls.add(roomWall1);
		
		//bottom wall
		TexRectEntity roomWall2 = 
			new TexRectEntity(
				pos.add((roomWidth + coriWidth)/2 + wallWidth, 0, 0), 
				new NetworkC(false), 
				new Size((roomWidth - coriWidth)/2 - wallWidth, wallWidth), 			
				c
			);
		walls.add(roomWall2);
		
		//vertical wall
		TexRectEntity coriWall1 =
			new TexRectEntity(
				pos.add((roomWidth - coriWidth)/2 - wallWidth, wallWidth, 0), 
				new NetworkC(false), 
				new Size(wallWidth, coriLen - wallWidth), 			
				c
			);
		walls.add(coriWall1);
		
		//vertical wall
		TexRectEntity coriWall2 =
			new TexRectEntity(
				pos.add((roomWidth + coriWidth)/2, wallWidth, 0), 
				new NetworkC(false), 
				new Size(wallWidth, coriLen - wallWidth), 			
				c
			);
		walls.add(coriWall2);
		
		//corner connecting bottom and left
		TexRectEntity cor1 =
			new TexRectEntity(
				pos.add((roomWidth - coriWidth)/2 - wallWidth, 0, 0), 
				new NetworkC(false), 
				new Size(wallWidth, wallWidth), 			
				c
			);
		walls.add(cor1);
		
		//corner connecting bottom and right
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
	
	/***
	 * A left side with no exit
	 * @param pos the position of the top left of the side
	 * @param wallWidth the width of the walls that make up the room
	 * @param roomHeight the height of the central room
	 * @param coriWidth the width of the corridor
	 * @param coriLen the length of the corridor
	 * @param c the colour of the room
	 * @return the walls that make up the side
	 */
	public static ArrayList<TexRectEntity> lNoExit(Point3D pos, int wallWidth, int roomHeight, int coriWidth, int coriLen, Color c){
		ArrayList<TexRectEntity> walls = new ArrayList<>();
		//vertical wall
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
	
	/***
	 * A left side with no exit
	 * @param pos the position of the top left of the side
	 * @param wallWidth the width of the walls that make up the room
	 * @param roomHeight the height of the central room
	 * @param coriWidth the width of the corridor
	 * @param coriLen the length of the corridor
	 * @param c the colour of the room
	 * @return the walls that make up the side
	 */
	public static ArrayList<TexRectEntity> rNoExit(Point3D pos, int wallWidth, int roomHeight, int coriWidth, int coriLen, Color c){
		ArrayList<TexRectEntity> walls = new ArrayList<>();
		//vertical wall
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
	
	/***
	 * A top side with no exit
	 * @param pos the position of the top left of the side
	 * @param wallWidth the width of the walls that make up the room
	 * @param roomWidth the width of the central room
	 * @param coriWidth the width of the corridor
	 * @param coriLen the length of the corridor
	 * @param c the colour of the room
	 * @return the walls that make up the side
	 */
	public static ArrayList<TexRectEntity> tNoExit(Point3D pos, int wallWidth, int roomWidth, int coriWidth, int coriLen, Color c){
		ArrayList<TexRectEntity> walls = new ArrayList<>();
		//top wall
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
	
	/***
	 * A bottom side with no exit
	 * @param pos the position of the top left of the side
	 * @param wallWidth the width of the walls that make up the room
	 * @param roomWidth the width of the central room
	 * @param coriWidth the width of the corridor
	 * @param coriLen the length of the corridor
	 * @param c the colour of the room
	 * @return the walls that make up the side
	 */
	public static ArrayList<TexRectEntity> bNoExit(Point3D pos, int wallWidth, int roomWidth, int coriWidth, int coriLen, Color c){
		ArrayList<TexRectEntity> walls = new ArrayList<>();
		
		//bottom wall
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
