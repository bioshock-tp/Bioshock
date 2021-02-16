package org.bioshock.engine.entity;

import java.util.ArrayList;

public final class GameEntityManager {

	private static ArrayList<GameEntityBase> gameEntities = new ArrayList<GameEntityBase>();
	
	public static void tick(double timeDelta) {
		for (GameEntityBase gameEntity : gameEntities) {
			gameEntity.safeTick(timeDelta);
		}
	}
	
	public static void register(GameEntityBase gameEntity) {
		gameEntities.add(gameEntity);
	}
	
	public static void unregister(GameEntityBase gameEntity) {
		gameEntities.remove(gameEntity);
	}
}
