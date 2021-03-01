package org.bioshock.scenes;

import org.bioshock.engine.ai.Seeker;
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

		Hider hider = new Hider(new Point3D(300, 400, 1), new NetworkC(true), new Size(40, 40), 200, Color.PINK);
		children.add(hider);

		int x = WindowManager.getWindowWidth() / 2;
		int y = WindowManager.getWindowHeight() / 2;

		Seeker seeker = new Seeker(new Point3D(x, y, 0.5), new NetworkC(true), new Size(40, 40), 300, Color.INDIANRED,
				hider);

		children.add(seeker);
		children.add(seeker.getSwatter());

		/*
		 * Room all4 = new Room( new Point3D(100, 100, 0), 40, new Size(300, 600), new
		 * Size(100, 100), new Exits(true, true, true, true), Color.SADDLEBROWN);
		 * children.addAll(all4.getWalls());
		 */

		ThreeByThreeMap map = new ThreeByThreeMap(
				new Point3D(100, 100, 0),
				10,
				new Size(100, 200),
				new Size(30, 30),
				Color.SADDLEBROWN);
		children.addAll(map.getWalls());
	}
}
