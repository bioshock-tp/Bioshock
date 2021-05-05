package org.bioshock.scenes;

import org.bioshock.main.App;
import org.bioshock.networking.NetworkManager;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class Lobby extends GameScene {
    private Label playerCountLabel = new Label("0/" + App.playerCount());
    private MainGame mainGame;

    public Lobby() {
        setCursor(Cursor.HAND);
        setBackground(new Background(new BackgroundFill(
            Color.LIGHTGRAY,
            null,
            null
        )));


        mainGame = new MainGame();

        Label waitingText = new Label("Waiting for players...");

        waitingText.setTranslateY(-100);

        getPane().getChildren().add(waitingText);

        getPane().getChildren().add(playerCountLabel);
    }

    @Override
    public void initScene() {
        SceneManager.setInLobby(true);

        if (App.isNetworked()) {
            NetworkManager.initialise();
        }
    }

    public void updatePlayerCount(int newCount) {
        App.logger.debug("updating player count");
        playerCountLabel.setText(String.format(
            "%d/%d",
            newCount, App.playerCount()
        ));

        if (newCount == App.playerCount()) {
            SceneManager.setScene(mainGame);
        }
    }
}
