package org.bioshock.scenes;

import java.util.ArrayList;

import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.entity.Entity;
import org.bioshock.engine.entity.EntityManager;
import org.bioshock.engine.entity.Size;
import org.bioshock.engine.rendering.RenderManager;

import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;

public abstract class GameScene extends Scene {
//	private static Size gameScreen = new Size(2560, 1440); //2k 16:9 monitor
	private static Size gameScreen = new Size(1920, 1080); //1080p 16:9 monitor
	private StackPane pane;
    private Canvas canvas = new Canvas(
        WindowManager.getWindowWidth(),
        WindowManager.getWindowHeight()
    );
    protected ArrayList<Entity> children = new ArrayList<>();

    private GameScene(StackPane pane) {
        super(pane);
        this.pane = pane;
        pane.getChildren().add(canvas);
        scaleCanvas();
    }

    protected GameScene() {
        this(new StackPane());
    }

	public void renderEntities() {
        children.forEach(EntityManager::register);
	}

    public void setBackground(Background background) {
        pane.setBackground(background);
	}

    public StackPane getPane() {
        return pane;
    }

	public Canvas getCanvas() {
		return canvas;
	}
	
	public void scaleCanvas() {
		RenderManager.setScale(new Point2D(
	    		WindowManager.getWindowWidth()/gameScreen.getWidth(),
	    		WindowManager.getWindowHeight()/gameScreen.getHeight())
			);
	}

	public void destroy() {

	}

	public void renderTick(double timeDelta) {

	}

	public void logicTick(double timeDelta) {

	}

 	public static Size getGameScreen() {
		return gameScreen;
	}

 	public void initScene() {}
}
