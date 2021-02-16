package org.bioshock.engine.scene;

import java.util.ArrayList;

import org.bioshock.engine.entity.GameEntityBase;

import javafx.scene.layout.StackPane;

public abstract class GameScene extends StackPane {
	private ArrayList<GameEntityBase> GameEntities = new ArrayList<GameEntityBase>();
	
	public final void AddEntity(GameEntityBase ge) {
		GameEntities.add(ge);
		ge.init();
	}
	
	public void unload() {
		for (GameEntityBase ge : GameEntities) {
			ge.destroyWrapper();
		}
	}
}
