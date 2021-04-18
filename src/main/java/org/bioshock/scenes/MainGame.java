package org.bioshock.scenes;

import java.util.List;

import org.bioshock.components.NetworkC;
import org.bioshock.engine.core.FrameRate;
import org.bioshock.engine.input.InputManager;
import org.bioshock.entities.EntityManager;
import org.bioshock.entities.LabelEntity;
import org.bioshock.entities.map.Room;
import org.bioshock.entities.map.RoomEntity;
import org.bioshock.entities.map.Wall;
import org.bioshock.entities.map.maps.GenericMap;
import org.bioshock.entities.map.maps.Map;
import org.bioshock.entities.map.maps.RandomMap;
import org.bioshock.entities.players.Hider;
import org.bioshock.entities.players.SeekerAI;
import org.bioshock.main.App;
import org.bioshock.networking.NetworkManager;
import org.bioshock.rendering.RenderManager;
import org.bioshock.utils.GlobalConstants;
import org.bioshock.utils.SizeD;
import org.bioshock.utils.SizeI;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MainGame extends GameScene {
    private static final double ENDTIME = 2 * 60f + 3;
    private static final double LOSEDELAY = 5;

    private boolean cameraLock = true;
    private double runningTime = 0;
    private boolean losing = false;
    private double timeLosing = 0;

    private LabelEntity timer;

    private Map map;

    public MainGame() {
        super();

        setCursor(Cursor.HAND);
        setBackground(new Background(new BackgroundFill(
            Color.LIGHTGRAY,
            null,
            null
        )));
        
        map = new GenericMap(
                new Point3D(0, 0, 0),
                1, 
                new SizeI(5, 7), 
                new SizeI(3, 5), 
                Color.SADDLEBROWN, 
                GlobalConstants.singletonMap,
                0
            );
        
        
        
        /* Players must render in exact order, do not play with z values */
        Hider hider = new Hider(
            new Point3D(0, 0, 0.5),
            new NetworkC(true),
            new SizeD(GlobalConstants.UNIT_WIDTH, GlobalConstants.UNIT_HEIGHT),
            300,
            Color.PINK
        );
        children.add(hider);

        for (int i = 1; i < App.playerCount(); i++) {
            children.add(new Hider(
                new Point3D(GameScene.getGameScreen().getWidth()*i, 0, i),
                new NetworkC(true),
                new SizeD(GlobalConstants.UNIT_WIDTH, GlobalConstants.UNIT_HEIGHT),
                300,
                Color.PINK
            ));
        }

        timer = new LabelEntity(
            new Point3D(GameScene.getGameScreen().getWidth()/2, 50, 100), 
            "mm:ss.ms", 
            new Font("arial", 20), 
            50, 
            Color.BLACK);
        
        children.add(timer);
        
//        timer = new Label("mm:ss.ms");
//        timer.setStyle("-fx-font: 20 arial; -fx-text-fill: black;");
//        timer.setPrefSize(timerSize.getWidth(), timerSize.getHeight());
//        timer.setTranslateX();
//        timer.setTranslateY(
//            -WindowManager.getWindowHeight() / 2 + timerSize.getHeight() / 2
//        );
//        getPane().getChildren().add(timer);

        InputManager.onRelease(KeyCode.Y, () ->	cameraLock = !cameraLock);
        InputManager.onRelease(KeyCode.C, () -> RenderManager.setClip(!RenderManager.isClip()));
        InputManager.onPress(KeyCode.LEFT, 
                () -> RenderManager.setCameraPos(RenderManager.getCameraPos().add(-10,0)));
        InputManager.onPress(KeyCode.RIGHT, 
                () -> RenderManager.setCameraPos(RenderManager.getCameraPos().add(10,0)));
        InputManager.onPress(KeyCode.UP, 
                () -> RenderManager.setCameraPos(RenderManager.getCameraPos().add(0,-10)));
        InputManager.onPress(KeyCode.DOWN, 
                () -> RenderManager.setCameraPos(RenderManager.getCameraPos().add(0,10)));
        InputManager.onPress(KeyCode.EQUALS, 
        		() -> RenderManager.setZoom(RenderManager.getZoom()*1/0.9));
        InputManager.onPress(KeyCode.MINUS, 
        		() -> RenderManager.setZoom(RenderManager.getZoom()*0.9));
        
        LabelEntity testLabel = new LabelEntity(
            new Point3D(10, 70, 100), 
            "This is a test string that is longer than 30 characters long"
            + "\n with\n newline\n charachters\n in", 
            new Font(20), 
            30,
            Color.BLACK);
        
        children.add(testLabel);

        FrameRate.initialise();
        children.add(FrameRate.getLabel());
        
        registerEntities();
    }

    @Override
    public void initScene(long seed) {
        
        
        if(App.isNetworked()) {
            map = new GenericMap(
                new Point3D(0, 0, 0),
                1, 
                new SizeI(5, 7), 
                new SizeI(3, 5), 
                Color.SADDLEBROWN, 
                GlobalConstants.testMap,
                seed
            );
        }
        else {
            map = new RandomMap(
                new Point3D(0, 0, 0),
                1,
                new SizeI(9, 11),
                new SizeI(3, 5),
                Color.SADDLEBROWN,
                new SizeD(3, 3),
                null,
                seed
            );
        }
        
        SceneManager.setMap(map);
        EntityManager.registerAll(map.getWalls().toArray(new Wall[0]));
        children.addAll(map.getWalls());

        List<Room> rooms = map.getRooms();
        
        for(Room room : rooms) {
            RoomEntity roomE = new RoomEntity(room);
            EntityManager.register(roomE);
            children.add(roomE);
        }    
        
        List<Hider> hiders = EntityManager.getPlayers();

        SeekerAI seeker = new SeekerAI(
            new Point3D(
                    rooms.get(rooms.size() / 2).getRoomCenter().getX()-GlobalConstants.UNIT_WIDTH/2, 
                    rooms.get(rooms.size() / 2).getRoomCenter().getY()-GlobalConstants.UNIT_HEIGHT/2, 
                    0.25),
            new NetworkC(true),
            new SizeD(GlobalConstants.UNIT_WIDTH, GlobalConstants.UNIT_HEIGHT),
            300,
            Color.INDIANRED,
            hiders.get(0)
        );
        
        seeker.initAnimations();

        EntityManager.register(seeker);
        children.add(seeker);        
        
        hiders.get(0).setPosition(
                rooms.get(0).getRoomCenter().getX()-GlobalConstants.UNIT_WIDTH/2, 
                rooms.get(0).getRoomCenter().getY()-GlobalConstants.UNIT_HEIGHT/2);
        
        for (int i = 1; i < App.playerCount(); i++) {
            int roomNumber = i % rooms.size();
            if (roomNumber >= rooms.size() / 2) roomNumber++;
            double x = rooms.get(roomNumber % rooms.size()).getRoomCenter().getX();
            double y = rooms.get(roomNumber % rooms.size()).getRoomCenter().getY();
            hiders.get(i).setPosition(x, y);
        }
            
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
            EntityManager.getPlayers().get(0).initAnimations();
        }
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
    public void renderTick(double timeDelta) {
        Hider hider = EntityManager.getCurrentPlayer();
        if(cameraLock && hider != null) {
            RenderManager.setCameraPos(
                hider.getCentre().subtract(
                    getGameScreen().getWidth() / 2,
                    getGameScreen().getHeight() / 2
                )
            );
        }

        double timeLeft = ENDTIME - runningTime;
        int numMins = (int) timeLeft / 60;
        timer.getStringBuilder().setLength(0);
        timer.getStringBuilder().append(String.format(
            "%d:%.2f",
            numMins,
            timeLeft - numMins * 60
        ));
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
