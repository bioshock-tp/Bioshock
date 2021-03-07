package org.bioshock.scenes;

import org.bioshock.engine.ai.Seeker;
import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.entity.Hider;
import org.bioshock.engine.entity.Size;
import org.bioshock.main.App;

import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class MainGame extends GameScene {
	public MainGame() {
		super();

		setCursor(Cursor.HAND);
		setBackground(new Background(new BackgroundFill(
            Color.LIGHTGRAY,
            null,
            null
        )));

        double x = 300;
        double y = 400;

        /* Players must render in exact order, do not play with z values */
        Hider hider = new Hider(
            new Point3D(x, y, 0.5),
            new NetworkC(true),
            new Size(40, 40),
            200,
            Color.PINK
        );
        children.add(hider);

        final double w = WindowManager.getWindowWidth();
        final double h = WindowManager.getWindowHeight();
        for (int i = 1; i < App.PLAYERCOUNT; i++) {
            x += 300 % w;
            y += 300 % h;

            children.add(new Hider(
                new Point3D(x, y, i),
                new NetworkC(true),
                new Size(40, 40),
                200,
                Color.PINK
            ));
        }

		double centreX = WindowManager.getWindowWidth() / 2;
		double centreY = WindowManager.getWindowHeight() / 2;

		Seeker seeker = new Seeker(
            new Point3D(centreX, centreY, 0.5),
            new NetworkC(true),
            new Size(40, 40),
            300,
            Color.INDIANRED,
			hider
        );

		children.add(seeker);
		children.add(seeker.getSwatter());
	}
}
