package org.bioshock.engine.scene;

import org.bioshock.engine.utils.NonInitializedExecption;

import javafx.scene.Scene;

public final class SceneController {
	
	private static Scene root = null;
	
	public static GameScene currentScene;
	
	public static void initialize(Scene inputRoot, GameScene initialScene) {
		root = inputRoot;
		setScene(initialScene);
	}

	public static void setScene(GameScene scene) throws NonInitializedExecption{
		if (root == null) {
			throw new NonInitializedExecption();
		}
		if (root.getRoot() instanceof GameScene) {
			GameScene temp = (GameScene) root.getRoot();
			temp.unload();
		}
		
		try {
			root.setRoot(scene);
			currentScene = scene;
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
