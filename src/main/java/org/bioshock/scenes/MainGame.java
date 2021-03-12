package org.bioshock.scenes;

import java.util.List;

import org.bioshock.engine.ai.SeekerAI;
import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.entity.EntityManager;
import org.bioshock.engine.entity.Hider;
import org.bioshock.engine.entity.Size;
import org.bioshock.engine.input.InputManager;
import org.bioshock.engine.rendering.RenderManager;
import org.bioshock.entities.map.Room;
import org.bioshock.entities.map.ThreeByThreeMap;
import org.bioshock.main.App;

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

        List<Room> rooms = map.getRooms();

        double x = rooms.get(0).getRoomCenter().getX();
        double y = rooms.get(0).getRoomCenter().getY();

        /* Players must render in exact order, do not play with z values */
        Hider hider = new Hider(
            new Point3D(x, y, 0.5),
            new NetworkC(true),
            new Size(40, 40),
            200,
            Color.PINK
        );
        children.add(hider);

        for (int i = 1; i < App.PLAYERCOUNT; i++) {
            int roomNumber = i % rooms.size();
            if (roomNumber >= rooms.size() / 2) roomNumber++;
            x = rooms.get(roomNumber % rooms.size()).getRoomCenter().getX();
            y = rooms.get(roomNumber % rooms.size()).getRoomCenter().getY();

            children.add(new Hider(
                new Point3D(x, y, i),
                new NetworkC(true),
                new Size(40, 40),
                200,
                Color.PINK
            ));
        }

		double centreX = rooms.get(rooms.size() / 2).getRoomCenter().getX();
		double centreY = rooms.get(rooms.size() / 2).getRoomCenter().getX();

		SeekerAI seeker = new SeekerAI(
            new Point3D(centreX, centreY, 0.5),
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
