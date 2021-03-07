package org.bioshock.scenes;

import java.util.ArrayList;
import java.util.List;

import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.entity.Entity;
import org.bioshock.engine.entity.EntityManager;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;

public abstract class GameScene extends Scene {
    protected StackPane pane;
    protected Canvas canvas = new Canvas(
        WindowManager.getWindowWidth(),
        WindowManager.getWindowHeight()
    );
    protected List<Entity> children = new ArrayList<>();

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
}
