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
import javafx.scene.control.ScrollPane;
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
    private ScrollPane scrollPane = new ScrollPane();

    private GameScene(StackPane pane) {
        super(pane);
        this.pane = pane;
        pane.getChildren().add(canvas);
        RenderManager.setScale(new Point2D(
    		WindowManager.getWindowWidth()/gameScreen.getWidth(),
    		WindowManager.getWindowHeight()/gameScreen.getHeight())
		);

//        scrollPane.setPrefSize(WindowManager.getWindowWidth(), WindowManager.getWindowHeight());
//        scrollPane.setContent(canvas);
//        //allows panning of the canvas
//        scrollPane.pannableProperty().set(true);
//        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
//        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
//
//        scrollPane.addEventFilter(ScrollEvent.SCROLL,new EventHandler<ScrollEvent>() {
//            @Override
//            public void handle(ScrollEvent event) {
//                event.consume();
//            }
//        });

        //InputManager.onPress( KeyCode.P, () -> scaleCanvas());
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

	public void destroy() {

	}

	public void renderTick(double timeDelta) {

	}

	public void logicTick(double timeDelta) {

	}

 	public static Size getGameScreen() {
		return gameScreen;
	}

 	public abstract void initScene();
}
