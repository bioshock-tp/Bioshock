package org.bioshock.scenes;

import org.bioshock.engine.ai.Enemy;
import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.entity.Player;
import org.bioshock.engine.entity.Size;
import org.bioshock.main.App;

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
		
		//Label gameLabel = new Label("Main Game");
		//this.getChildren().add(gameLabel);
		//StackPane.setAlignment(gameLabel, Pos.TOP_CENTER);
		//gameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 70));
		
		setCursor(Cursor.HAND);
		setBackground(new Background(new BackgroundFill(
            Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY
        )));
		
		Player player = new Player(
            new Point3D(300, 400, 1), new Size(40, 40),
            200, Color.PINK
        );
        children.add(player);
        App.logger.debug("Player object - {}", (Object) player);
        
		// SceneController.addEntity(player);

        int x = WindowManager.getWindowWidth() / 2;
        int y = WindowManager.getWindowHeight() / 2;

        Enemy enemy = new Enemy(
            new Point3D(x, y, 0.5), new Size(40, 40),
            300, Color.INDIANRED, player
        );
        children.add(enemy);
        App.logger.debug("Enemy object - {}", (Object) enemy);
        
		// SceneController.addEntity(enemy);
	}
}
