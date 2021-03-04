package org.bioshock.scenes;

import javafx.geometry.Point2D;
import org.bioshock.engine.ai.SeekerAI;
import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.entity.Hider;
import org.bioshock.engine.entity.Size;
import org.bioshock.entities.map.ThreeByThreeMap;

import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class MainGame extends GameScene {
	public MainGame() {
		super();

		setCursor(Cursor.HAND);
		setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));





		/*SeekerHuman sh = new SeekerHuman(new Point3D(x, y, 0.5), new NetworkC(true), new Size(40, 40), 300, Color.INDIANRED,
				hider);
		children.add(sh);*/

		ThreeByThreeMap map = new ThreeByThreeMap(
				new Point3D(100, 100, 0),
				10,
				new Size(300, 600),
				new Size(90, 90),
				Color.SADDLEBROWN);
		children.addAll(map.getWalls());

		Point3D startPos = map.getRooms().get(2).getRoomCenter();

		Hider hider = new Hider(new Point3D(300, 400, startPos.getZ()), new NetworkC(true), new Size(40, 40), 200, Color.PINK);
		children.add(hider);

		int x = WindowManager.getWindowWidth() / 2;
		int y = WindowManager.getWindowHeight() / 2;

		SeekerAI seeker = new SeekerAI(startPos, new NetworkC(true), new Size(40, 40), 300, Color.INDIANRED,
				hider);

		children.add(seeker);
	}
}
