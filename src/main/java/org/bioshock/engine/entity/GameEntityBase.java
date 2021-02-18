package org.bioshock.engine.entity;

import java.util.UUID;

import org.bioshock.engine.networking.NetworkManager;
import org.bioshock.engine.rendering.RenderManager;

public abstract class GameEntityBase {

	public INetworkComponent networkC = null;
	public IPhysicsComponent physicsC = null;
	public IRendererComponent rendererC = null;
	public ITransformComponent transformC = null;
	public boolean enabled = true;
	private boolean initialized = false;
	private UUID ID;
	
	public void init() {
		if (initialized) {
			return;
		}
		initialized = true;
		
		GameEntityManager.register(this);
		if(networkC != null) {
			NetworkManager.register(this);
		}
		if(rendererC != null) {
			RenderManager.register(this);
		}
	}

	public void destroyWrapper() {
		GameEntityManager.unregister(this);
		if(networkC != null) {
			NetworkManager.unregister(this);
		}
		if(rendererC != null) {
			RenderManager.unregister(this);
		}
		destroy();
	}
	
	public abstract void destroy();
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public UUID getID() {
		return ID;
	}
	
	public GameEntityBase() {
		ID = UUID.randomUUID();
	}

	public final void safeTick(double timeDelta) {
		if (enabled) {
			this.tick(timeDelta);
		}
	}
	
	protected abstract void tick(double timeDelta);
	
}
