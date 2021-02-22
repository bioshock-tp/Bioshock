package org.bioshock.engine.networking;

import java.util.List;

import org.bioshock.engine.entity.Entity;

public class NetworkManager {
    private NetworkManager() {}

	public static void tick() {
		// TODO Auto-generated method stub
		
	}

	public static void register(Entity entity) {
		// TODO Auto-generated method stub
		
	}

    public static void registerAll(List<Entity> entities) {
        entities.forEach(NetworkManager::register);		
	}

	public static void unregister(Entity entity) {
		// TODO Auto-generated method stub
		
	}

	public static void unregisterAll(List<Entity> entities) {
       entities.forEach(NetworkManager::unregister);	
	}
}
