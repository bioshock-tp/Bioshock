package org.bioshock.engine.scene;

import java.lang.reflect.InvocationTargetException;

import org.bioshock.engine.utils.NonInitializedExecption;

import javafx.scene.Scene;

public final class SceneController {
	
	private static Scene root = null;
	
	public static GameScene currentScene;
	
	public static void initialize(Scene inputRoot, Class<? extends GameScene> initialScene) {
		root = inputRoot;
		setScene(initialScene);
	}

	public static void setScene(Class<? extends GameScene> sceneClass) throws NonInitializedExecption{
		if (root == null) {
			throw new NonInitializedExecption();
		}
		if (root.getRoot() instanceof GameScene) {
			GameScene temp = (GameScene) root.getRoot();
			temp.unload();
		}
		
		try {
			GameScene sceneInstance = sceneClass.getConstructor().newInstance(new Object [] {});
			root.setRoot(sceneInstance);
			currentScene = sceneInstance;
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
