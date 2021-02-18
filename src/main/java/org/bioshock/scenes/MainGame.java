package org.bioshock.scenes;

import org.bioshock.engine.ai.Enemy;
import org.bioshock.engine.core.WindowInitializer;
import org.bioshock.engine.rendering.RenderManager;
import org.bioshock.engine.scene.GameScene;
import org.bioshock.engine.sprites.Player;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MainGame extends GameScene{
	public MainGame() {
		Canvas canvas = new Canvas(WindowInitializer.getWindowWidth(), WindowInitializer.getWindowHeight());
		this.getChildren().add(canvas);
		
		//Label gameLabel = new Label("Main Game");
		//this.getChildren().add(gameLabel);
		//StackPane.setAlignment(gameLabel, Pos.TOP_CENTER);
		//gameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 70));
		
		setCursor(Cursor.HAND);
		setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
		RenderManager.canvas = canvas;
		
		Player player = new Player(300, 400, 40, 40, 200,Color.PINK, 1.0);
		this.AddEntity(player);
		this.AddEntity(new Enemy((int) (WindowInitializer.getWindowWidth()/2), (int) (WindowInitializer.getWindowHeight()/2), 40, 40, 300, Color.INDIANRED, 0.5, player));
		
		
	}	
}
