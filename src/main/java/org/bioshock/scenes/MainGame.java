package org.bioshock.scenes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bioshock.components.NetworkC;
import org.bioshock.engine.core.FrameRate;
import org.bioshock.engine.input.InputManager;
import org.bioshock.entities.EntityManager;
import org.bioshock.entities.LabelEntity;
import org.bioshock.entities.items.food.Burger;
import org.bioshock.entities.items.food.Dessert;
import org.bioshock.entities.items.food.Donut;
import org.bioshock.entities.items.food.HotDog;
import org.bioshock.entities.items.food.Pizza;
import org.bioshock.entities.items.powerup_items.FreezeItem;
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
import org.bioshock.utils.Size;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MainGame extends GameScene {
    /**
     *
     */
    private static final int PADDING = 10;
    private static final double ENDTIME = 2 * 60f + 3;
    private static final double LOSEDELAY = 5;

    /**
     * Number of food items to be collected to win
     */
    private static final int FOOD_TO_WIN = 5;

    private int collectedFood = 0;

    private LabelEntity counter;

    private boolean cameraLock = true;
    private double runningTime = 0;
    private boolean losing = false;
    private double timeLosing = 0;

    private LabelEntity timer;

    private LabelEntity textChat;

    private LabelEntity chatLabel;
    private List<Room> playersNotSpawnedIn = new ArrayList<>();

    private Random rand;

    public MainGame() {
        super();

        setCursor(Cursor.HAND);
        setBackground(new Background(new BackgroundFill(
            Color.LIGHTGRAY,
            null,
            null
        )));

        InputManager.onRelease(KeyCode.Y, () ->	cameraLock = !cameraLock);

        InputManager.onRelease(KeyCode.C, () ->
            RenderManager.setClip(!RenderManager.clips())
        );

        InputManager.onPress(KeyCode.LEFT, () ->
            RenderManager.setCameraPos(
                RenderManager.getCameraPos().add(-10, 0)
            )
        );
        InputManager.onPress(KeyCode.RIGHT, () ->
            RenderManager.setCameraPos(
                RenderManager.getCameraPos().add(10, 0)
            )
        );
        InputManager.onPress(KeyCode.UP, () ->
            RenderManager.setCameraPos(
                RenderManager.getCameraPos().add(0, -10)
            )
        );
        InputManager.onPress(KeyCode.DOWN, () ->
            RenderManager.setCameraPos(
                RenderManager.getCameraPos().add(0, 10)
            )
        );

        initEntities();
    }

    private void initEntities() {
        initHiders();

        initCounter();

        initChat();

        registerEntities();
    }

    private void initHiders() {
        /* Players must render in exact order, do not play with z values */
        Hider hider = new Hider(
            new Point3D(0, 0, 0.5),
            new NetworkC(true),
            new Size(
                (double) GlobalConstants.UNIT_WIDTH - PADDING,
                (double) GlobalConstants.UNIT_HEIGHT - PADDING
            ),
            300,
            Color.PINK
        );
        children.add(hider);

        for (int i = 1; i < App.playerCount(); i++) {
            children.add(new Hider(
                new Point3D(GameScene.getGameScreen().getWidth() * i, 0, i),
                new NetworkC(true),
                new Size(
                    (double) GlobalConstants.UNIT_WIDTH - PADDING,
                    (double) GlobalConstants.UNIT_HEIGHT - PADDING
                ),
                300,
                Color.PINK
            ));
        }
    }


    private void initCounter() {
        counter = new LabelEntity(
            new Point3D(GameScene.getGameScreen().getWidth() / 2, 50, 100),
            String.format("%d/%d", collectedFood, FOOD_TO_WIN),
            new Font("arial", 20),
            50,
            Color.BLACK
        );

        children.add(counter);
    }

    private void initTimer() {
        timer = new LabelEntity(
            new Point3D(GameScene.getGameScreen().getWidth() / 2, 50, 100),
            "mm:ss.ms",
            new Font("arial", 20),
            50,
            Color.BLACK
        );

        children.add(timer);
    }

    private void initChat() {
        /*
         * Full screen is capable of up to 40 rows of messages.
         * Keep only the last 20 messages always
         */
        chatLabel = new LabelEntity(
            new Point3D(10, 70, 1000),
            new Font(20),
            100,
            Color.BLACK
        );

        chatLabel.setDisplay(false);

        children.add(chatLabel);

        children.add(FrameRate.getLabel());

		textChat = new LabelEntity(
            new Point3D(
                10,
                GameScene.getGameScreen().getHeight() / 2
                    + GameScene.getGameScreen().getHeight() / 15,
                1000
            ),
            new Font(20),
            86,
            Color.BLACK
        );

        textChat.setDisplay(false);

        children.add(textChat);
    }


    @Override
    public void initScene(long seed) {

        rand = new Random(seed);
        initMap(seed);

        List<Room> rooms = SceneManager.getMap().getRooms();
        playersNotSpawnedIn.addAll(rooms);

        List<Hider> hiders = EntityManager.getPlayers();

        for (int i = 0; i < App.playerCount(); i++) {
            Room roomToSpawn = playersNotSpawnedIn.get(
                rand.nextInt(playersNotSpawnedIn.size())
            );
            playersNotSpawnedIn.remove(roomToSpawn);

            double x = roomToSpawn.getRoomCenter().getX();
            double y = roomToSpawn.getRoomCenter().getY();
            playersNotSpawnedIn.remove(roomToSpawn);

            hiders.get(i).setPosition(
                x - (double) GlobalConstants.UNIT_WIDTH / 2,
                y - (double) GlobalConstants.UNIT_HEIGHT / 2
            );
        }

        initSeekers(2);

        initItems();

        renderEntities();

        SceneManager.setInLobby(false);
        SceneManager.setInGame(true);

        if (App.isNetworked()) {
            synchronized(NetworkManager.getPlayerJoinLock()) {
                NetworkManager.getPlayerJoinLock().notifyAll();
            }
            App.logger.debug("Notified networking thread");
        } else {
            assert(App.playerCount() == 1);
            EntityManager.getPlayers().get(0).getMovement().initMovement();
            EntityManager.getPlayers().get(0).initAnimations();
        }
    }

    private void initMap(long seed) {
        Map map;
        if (App.isNetworked()) {
            map = new GenericMap(
                new Point3D(0, 0, 0),
                1,
                new Size(5, 7),
                new Size(3, 5),
                Color.SADDLEBROWN,
                GlobalConstants.TEST_MAP,
                seed
            );
        }
        else {
            map = new RandomMap(
                new Point3D(0, 0, 0),
                2,
                new Size(9, 11),
                new Size(3, 5),
                Color.SADDLEBROWN,
                new Size(5, 5),
                null,
                seed
            );
        }

        SceneManager.setMap(map);
        EntityManager.registerAll(map.getWalls().toArray(new Wall[0]));
        children.addAll(map.getWalls());

        List<Room> rooms = map.getRooms();

        for (Room room : rooms) {
            RoomEntity roomEntity = new RoomEntity(room);
            EntityManager.register(roomEntity);
            children.add(roomEntity);
        }
    }

    private void initSeekers(int numSeekers) {
        for (int i = 0; i < numSeekers; i++) {
            Room roomToSpawn = playersNotSpawnedIn.get(
                rand.nextInt(playersNotSpawnedIn.size())
            );
            playersNotSpawnedIn.remove(roomToSpawn);

            double x = roomToSpawn.getRoomCenter().getX();
            double y = roomToSpawn.getRoomCenter().getY();
            SeekerAI seeker = new SeekerAI(
                new Point3D(
                    x - (double) GlobalConstants.UNIT_WIDTH / 2,
                    y - (double) GlobalConstants.UNIT_HEIGHT / 2,
                    0.25
                ),
                new NetworkC(true),
                new Size(
                    GlobalConstants.UNIT_WIDTH,
                    GlobalConstants.UNIT_HEIGHT
                ),
                300,
                Color.INDIANRED
            );

            seeker.initAnimations();
            EntityManager.register(seeker);
            children.add(seeker);
        }
    }

    private void initItems() {
        children.add(new Burger(rand.nextLong()));
        children.add(new Dessert(rand.nextLong()));
        children.add(new Donut(rand.nextLong()));
        children.add(new HotDog(rand.nextLong()));
        children.add(new Pizza(rand.nextLong()));
		children.add(new FreezeItem(rand.nextLong()));
    }

    @Override
    public void logicTick(double timeDelta) {
        if (!losing) {
            runningTime += timeDelta;

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
        if (cameraLock && hider != null) {
            RenderManager.setCameraPos(
                hider.getCentre().subtract(
                    getGameScreen().getWidth() / 2,
                    getGameScreen().getHeight() / 2
                )
            );
        }
    }

    public void collectFood() {
        if (++collectedFood == FOOD_TO_WIN) {
            NetworkManager.tick();
            SceneManager.setScene(new WinScreen());
        }

        counter.setLabel(String.format("%d/%d", collectedFood, FOOD_TO_WIN));
    }

    public void appendStringToChat(String string) {
        chatLabel.appendString(string);
    }

    public void setChatVisibility(boolean visible) {
        chatLabel.setDisplay(visible);
        textChat.setDisplay(visible);
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
    }
}
