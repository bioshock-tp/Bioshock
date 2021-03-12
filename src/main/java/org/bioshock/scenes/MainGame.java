package org.bioshock.scenes;

import org.bioshock.engine.ai.SeekerAI;
import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.entity.EntityManager;
import org.bioshock.engine.entity.Hider;
import org.bioshock.engine.entity.Size;
import org.bioshock.engine.input.InputManager;
import org.bioshock.engine.rendering.RenderManager;
import org.bioshock.entities.map.ThreeByThreeMap;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class MainGame extends GameScene {
	private boolean cameraLock = true;
	
	public MainGame() {
		super();

		setCursor(Cursor.HAND);
		setBackground(new Background(new BackgroundFill(
            Color.LIGHTGRAY,
            null,
            null
        )));

		ThreeByThreeMap map = new ThreeByThreeMap(
            new Point3D(100, 100, 0),
            10,
            new Size(300, 600),
            new Size(90, 90),
            Color.SADDLEBROWN
        );
		children.addAll(map.getWalls());

		Point3D startPos = map.getRooms().get(2).getRoomCenter();

		Hider hider = new Hider(
            new Point3D(300, 400, 0.5),
            new NetworkC(true),
            new Size(40, 40),
            200,
            Color.PINK
        );
		children.add(hider);

		SeekerAI seeker = new SeekerAI(
            startPos,
            new NetworkC(true),
            new Size(40, 40),
            300,
            Color.INDIANRED,
			hider
        );

		children.add(seeker);
		
		
		InputManager.onRelease(KeyCode.Y, 
			() ->	{cameraLock = !cameraLock;});
	}
	
	@Override
	public void tick(double timeDelta) {
		if(cameraLock) {
			RenderManager.setCameraPos(
					EntityManager.getPlayers().get(0).getCentre().subtract(
							super.getGameScreen().getWidth()/2,
							super.getGameScreen().getHeight()/2));
		}
	}
	
    @Override
    public void destroy() {
        InputManager.removeKeyListeners(
            KeyCode.W,
            KeyCode.A,
            KeyCode.S,
            KeyCode.D,
            KeyCode.Y
        );
        
        RenderManager.setCameraPos(new Point2D(0, 0));
    }
}
