package org.bioshock.scenes;

import java.util.List;

import org.bioshock.engine.ai.SeekerAI;
import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.core.FrameRate;
import org.bioshock.engine.core.WindowManager;
import org.bioshock.engine.entity.EntityManager;
import org.bioshock.engine.entity.Hider;
import org.bioshock.engine.entity.Size;
import org.bioshock.engine.input.InputManager;
import org.bioshock.engine.networking.NetworkManager;
import org.bioshock.engine.rendering.RenderManager;
import org.bioshock.engine.scene.SceneManager;
import org.bioshock.entities.map.Room;
import org.bioshock.entities.map.ThreeByThreeMap;
import org.bioshock.main.App;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class MainGame extends GameScene {
    private static final double ENDTIME = 2 * 60f + 3;
    private static final double LOSEDELAY = 5;

    private boolean cameraLock = true;
    private double runningTime = 0;
    private boolean losing = false;
    private double timeLosing = 0;

    private Label timer;

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

        for (int i = 1; i < App.playerCount(); i++) {
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
        double centreY = rooms.get(rooms.size() / 2).getRoomCenter().getY();

        SeekerAI seeker = new SeekerAI(
            new Point3D(centreX, centreY, 0.25),
            new NetworkC(true),
            new Size(40, 40),
            300,
            Color.INDIANRED,
            hider
        );

        children.add(seeker);

        Size timerSize = new Size(100, 100);
        timer = new Label("mm:ss.ms");
        timer.setStyle("-fx-font: 20 arial; -fx-text-fill: black;");
        timer.setPrefSize(timerSize.getWidth(), timerSize.getHeight());
        timer.setTranslateX(-timerSize.getWidth()/2);
        timer.setTranslateY(
            -WindowManager.getWindowHeight() / 2 + timerSize.getHeight() / 2
        );
        getPane().getChildren().add(timer);

        InputManager.onRelease(KeyCode.Y, () ->	cameraLock = !cameraLock);

        registerEntities();
    }

    @Override
    public void initScene() {
        renderEntities();

        FrameRate.initialise();

        SceneManager.setInLobby(false);
        SceneManager.setInGame(true);

        if (App.isNetworked()) {
            Object lock = NetworkManager.getPlayerJoinLock();
            synchronized(lock) {
                lock.notifyAll();
            }
            App.logger.debug("Notified networking thread");
        } else {
            assert(App.playerCount() == 1);
            EntityManager.getPlayers().get(0).getMovement().initMovement();
        }
    }

    @Override
    public void renderTick(double timeDelta) {
        if(cameraLock) {
            Hider hider = EntityManager.getCurrentPlayer();

            if (hider != null) {
                RenderManager.setCameraPos(hider.getCentre().subtract(
                    getGameScreen().getWidth() / 2,
                    getGameScreen().getHeight() / 2)
                );
            }
        }

        double timeLeft = ENDTIME - runningTime;
        int numMins = (int) timeLeft / 60;
        timer.setText(String.format(
            "%d:%.2f",
            numMins,
            timeLeft - numMins * 60
        ));
    }

    @Override
    public void logicTick(double timeDelta) {
        if(!losing) {
            runningTime += timeDelta;

            if (runningTime >= ENDTIME) {
                SceneManager.setScene(new WinScreen());
                return;
            }

            if (
                !EntityManager.getPlayers().isEmpty()
                && EntityManager.getPlayers().stream().allMatch(Hider::isDead)
            ) {
                losing = true;
            }
        }
        else {
            timeLosing += timeDelta;
            if (timeLosing >= LOSEDELAY) {
                SceneManager.setScene(new LoseScreen());
            }
        }
    }

    @Override
    public void destroy() {
        super.destroy();

        SceneManager.setInGame(false);

        InputManager.removeKeyListeners(
            KeyCode.W,
            KeyCode.A,
            KeyCode.S,
            KeyCode.D,
            KeyCode.Y
        );

        RenderManager.setCameraPos(new Point2D(0, 0));

        SceneManager.setInGame(false);
    }
}
