package org.bioshock.scenes;

import java.util.ArrayList;

import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.entity.Entity;
import org.bioshock.engine.entity.EntityManager;
import org.bioshock.engine.entity.SquareEntity;

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
        children.forEach(entity -> {
            if (entity instanceof SquareEntity) {
                pane.getChildren().add(((SquareEntity) entity).getHitbox());
            }
        });
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
