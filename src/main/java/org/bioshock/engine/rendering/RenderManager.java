package org.bioshock.engine.rendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bioshock.engine.entity.GameEntityBase;
import org.bioshock.engine.entity.IRendererComponent;
import org.bioshock.render.components.EnemyRendererC;
import org.bioshock.render.components.PlayerRendererC;
import org.bioshock.render.components.SwatterRendererC;
import org.bioshock.render.renderers.EnemyRenderer;
import org.bioshock.render.renderers.PlayerRenderer;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.bioshock.render.renderers.SwatterRenderer;

public final class RenderManager {
	private static ArrayList<GameEntityBase> renderableEntities = new ArrayList<GameEntityBase>();
	private static Map<Class<? extends IRendererComponent>, IBaseRenderer> map = 
			new HashMap<Class<? extends IRendererComponent>, IBaseRenderer>();
	static {
		map.put(PlayerRendererC.class, new PlayerRenderer());
		map.put(EnemyRendererC.class, new EnemyRenderer());
		map.put(SwatterRendererC.class, new SwatterRenderer());
	}
	
	
	public static Canvas canvas;
	
	
	/**
	 * A method that attempts to render every entity registered to the RenderManager in Ascending Y order
	 * but cannot render if it has no canvas to render entities on
	 * 
	 * before rendering it sets the entire canvas to Color.LIGHTGRAY
	 */
	public static void tick() {
		//System.out.println("Render Tick");
		if (canvas == null) {
			return;
		}
		//System.out.println("Render Tick With Canvas");
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		//Set Background to LightGray
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		
		sort(renderableEntities);
		
		//renders each entity with the renderer defined in the map 
		for (GameEntityBase entity : renderableEntities) {
			if(entity.isEnabled()) {
				for (Class<? extends IRendererComponent> component : map.keySet()) {
					if (entity.rendererC.getClass() == component) {
							IBaseRenderer renderer = map.get(component);
							renderer.render(gc, entity);						
					}
				}
			}
		}
	}
	
	/**
	 * A method that sorts a list of entities in ascending order
	 * Current Implementation uses Insertion sort as there is likely to be minimal changes from frame to frame in order
	 * @param entityList The list to be sorted by ref
	 */
	public static void sort(ArrayList<GameEntityBase> entityList) {  
        int n = entityList.size();  
        for (int j = 1; j < n; j++) {  
        	GameEntityBase key = entityList.get(j);  
            int i = j-1;  
            while ( (i > -1) && (entityList.get(i).rendererC.getZ() > key.rendererC.getZ() ) ) {  
                entityList.set(i+1, entityList.get(i));  
                i--;  
            }  
            entityList.set(i+1, key);
        }  
    } 

	/**
	 * Unregisters an entity from the RenderManager
	 * @param entityToRemove
	 */
	public static void unregister(GameEntityBase entityToRemove) {
		renderableEntities.remove(entityToRemove);
	}

	/**
	 * Registers an entity to the RenderManager 
	 * and 
	 * Stores it in ascending order with regards to it's Y value given in it's render component
	 * @param entityToAdd
	 */
	public static void register(GameEntityBase entityToAdd) {
		if (renderableEntities.size() == 0) {
			renderableEntities.add(entityToAdd);
		}
		else {
			int i = 0;
			GameEntityBase currentEntity = renderableEntities.get(0);
			while (currentEntity.rendererC.getZ() < entityToAdd.rendererC.getZ()) {
				i++;
				currentEntity = renderableEntities.get(i);
			}
			
			renderableEntities.add(i, entityToAdd);
		}
		

	}

}
