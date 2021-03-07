package org.bioshock.scenes;

import java.util.ArrayList;

import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.entity.Entity;
import org.bioshock.engine.entity.EntityManager;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;

public abstract class GameScene extends Scene {
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
    }

    protected GameScene() {
        this(new StackPane());
    }

	public void renderEntities() {
        children.forEach(EntityManager::register);
        children.forEach(entity -> pane.getChildren().add(entity.getHitbox()));
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

	public void destroy() {
	}
}
