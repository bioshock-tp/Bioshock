package org.bioshock.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.bioshock.engine.ai.SeekerAI;
import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.entity.Hider;
import org.bioshock.engine.entity.SeekerHuman;
import org.bioshock.engine.entity.Size;
import org.bioshock.entities.map.ThreeByThreeMap;

public class MainGame extends GameScene {
	public MainGame() {
		super();

		setCursor(Cursor.HAND);
		setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));





		/*SeekerHuman sh = new SeekerHuman(new Point3D(x, y, 0.5), new NetworkC(true), new Size(40, 40), 300, Color.INDIANRED,
				hider);
		children.add(sh);*/

		/*
		 * Room all4 = new Room( new Point3D(100, 100, 0), 40, new Size(300, 600), new
		 * Size(100, 100), new Exits(true, true, true, true), Color.SADDLEBROWN);
		 * children.addAll(all4.getWalls());
		 */

		ThreeByThreeMap map = new ThreeByThreeMap(
				new Point3D(100, 100, 0),
				10,
				new Size(300, 600),
				new Size(90, 90),
				Color.SADDLEBROWN);
		children.addAll(map.getWalls());

		Point3D startPos = map.getRooms().get(2).getRoomCenter();

		Hider hider = new Hider(new Point3D(300, 400, startPos.getZ()), new NetworkC(true), new Size(40, 40), 200, Color.PINK);
		SeekerHuman human = new SeekerHuman(new Point3D(200, 300, startPos.getZ()), new NetworkC(true), 200);
		children.add(hider);
		children.add(human);

		int x = WindowManager.getWindowWidth() / 2;
		int y = WindowManager.getWindowHeight() / 2;

		SeekerAI seeker = new SeekerAI(startPos, new NetworkC(true), new Size(40, 40), 300, Color.INDIANRED,
				hider);

		children.add(seeker);
	}
}
