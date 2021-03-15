package org.bioshock.scenes;

import java.util.ArrayList;
import java.util.List;

import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.entity.Entity;
import org.bioshock.engine.entity.EntityManager;
import org.bioshock.engine.rendering.RenderManager;

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
    protected List<Entity> children = new ArrayList<>();

    private GameScene(StackPane pane) {
        super(pane);
        this.pane = pane;
        pane.getChildren().add(canvas);
    }

    protected GameScene() {
        this(new StackPane());
    }

    public void initScene() {
        registerEntities();
        renderEntities();
    }

    public void registerEntities() {
        children.forEach(EntityManager::register);
	}

	public void renderEntities() {
        children.forEach(RenderManager::register);
	}

    public void unregisterEntities() {
        children.forEach(EntityManager::unregister);
	}

	public void destroyEntities() {
        children.forEach(RenderManager::unregister);
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
        unregisterEntities();
        destroyEntities();
	}
}
