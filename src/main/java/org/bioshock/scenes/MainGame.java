package org.bioshock.scenes;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.bioshock.components.NetworkC;
import org.bioshock.engine.core.FrameRate;
import org.bioshock.engine.input.InputManager;
import org.bioshock.entities.EntityManager;
import org.bioshock.entities.LabelEntity;
import org.bioshock.entities.items.food.*;
import org.bioshock.entities.items.powerup_items.FreezeItem;
import org.bioshock.entities.items.powerup_items.InvisibilityItem;
import org.bioshock.entities.items.powerup_items.SpeedItem;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGame extends GameScene {
    /**
     * Number of seekers to spawn
     */
    private static final int SEEKER_COUNT = 2;
    /**
     * How much smaller hiders and seekers are going to be compared to 
     * UNIT_WIDTH and UNIT_HEIGHT
     */
    private static final int PADDING = 10;
    /**
     * How long until the the hiders win the game
     * (Currently timer isn't used as win condition is now collecting a certain amount of food)
     */
    private static final double ENDTIME = 2 * 60f + 3;
    /**
     * The amount of time between you losing and the lose screen being shown
     */
    private static final double LOSEDELAY = 0;
    /**
     * Number of food items to be collected to win
     */
    private static final int FOOD_TO_WIN = 5;
    /**
     * The amount of food currently collected
     */
    private int collectedFood = 0;
    /**
     * The counter representing how much food has been collected
     */
    private LabelEntity counter;
    /**
     * A boolean representing if the camera is locked to the currentPlayers position or not
     */
    private boolean cameraLock = true;
    /**
     * The amount of time the game has been running
     */
    private double runningTime = 0;
    /**
     * Boolean representing if you are losing the game or not
     */
    private boolean losing = false;
    /**
     * Boolean representing if you have lost the game 
     * i.e. LOSEDELAY amount of time has passed since losing has been set to true
     */
    private boolean lost;
    /**
     * The current amount of time you have been losing for
     */
    private double timeLosing = 0;
    /**
     * The timer entity representing how much time is left in the game
     * (Currently timer isn't used as win condition is now collecting a certain amount of food)
     */
    private LabelEntity timer;
    /**
     * The label that displays the text you are currently typing
     */
    private LabelEntity textChat;
    /**
     * The label that shows the chat that has been sent
     */
    private LabelEntity chatLabel;
    /**
     * A list of rooms that players haven't been spawned in yet
     * Used to make sure seekers aren't spawned in the same room as the hiders
     */
    private List<Room> playersNotSpawnedIn = new ArrayList<>();
    /**
     * The random number generator used across the class
     */
    private Random rand;

    public MainGame() {
        super();

        setCursor(Cursor.HAND);
        setBackground(new Background(new BackgroundFill(
            Color.LIGHTGRAY,
            null,
            null
        )));
        
        //Make it so you can toggle the camera lock on and off by pressing Y
        InputManager.onRelease(KeyCode.Y, () ->	cameraLock = !cameraLock);

        //Make it so you can toggle clipping on and off by pressing C
        InputManager.onRelease(KeyCode.C, () ->
            RenderManager.setClip(!RenderManager.clips())
        );
        
        //Make it so if you press the left key the camera moves to the left
        InputManager.onPress(KeyCode.LEFT, () ->
            RenderManager.setCameraPos(
                RenderManager.getCameraPos().add(-10, 0)
            )
        );
        //Make it so if you press the right key the camera moves to the right
        InputManager.onPress(KeyCode.RIGHT, () ->
            RenderManager.setCameraPos(
                RenderManager.getCameraPos().add(10, 0)
            )
        );
        //Make it so if you press the up key the camera moves up
        InputManager.onPress(KeyCode.UP, () ->
            RenderManager.setCameraPos(
                RenderManager.getCameraPos().add(0, -10)
            )
        );
        //Make it so if you press the down key the camera moves down
        InputManager.onPress(KeyCode.DOWN, () ->
            RenderManager.setCameraPos(
                RenderManager.getCameraPos().add(0, 10)
            )
        );

        initEntities();
    }

    /**
     * Initialise all entities that don't need the game seed 
     * or entities that need entities that need the game seed
     */
    private void initEntities() {
        initHiders();

        children.add(FrameRate.getLabel());

        initCounter();

        initChat();

        registerEntities();
    }

    /**
     * Initialise all the hiders but not their positions as the locations won't be 
     * known as the map hasn't been generated yet
     */
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
    
    /**
     * Initialize the game counter
     */
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
    
    /**
     * Initialize the game timer
     * (Currently timer isn't used as win condition is now collecting a certain amount of food)
     */
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
    
    /**
     * Initialize the chat
     */
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
        
        //Set the location of every hider so they're in the centre of a room
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

        initItems();

        renderEntities();

        startCountdown();

        SceneManager.setInLobby(false);
        SceneManager.setInGame(true);

        if (App.isNetworked()) {
            synchronized(NetworkManager.getPlayerJoinLock()) {
                NetworkManager.getPlayerJoinLock().notifyAll();
            }
            App.logger.debug("Notified networking thread");
        } else {
            //If not networked there should be one player and then initialize 
            //the movement and animations of the player/hider
            assert(App.playerCount() == 1);
            EntityManager.getPlayers().get(0).getMovement().initMovement();
            EntityManager.getPlayers().get(0).initAnimations();
        }
    }
    
    /**
     * Initialize the map with the given seed
     * @param seed
     */
    private void initMap(long seed) {
        //Generate a map based off the seed 
        //A random one if in local mode and a pre-made one if in online mode
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

        //Set the scenes map to the generated map and register all the walls in the map
        //The walls include the objects that are in the room
        SceneManager.setMap(map);
        EntityManager.registerAll(map.getWalls().toArray(new Wall[0]));
        children.addAll(map.getWalls());

        List<Room> rooms = map.getRooms();
        
        //For every room in the map put it into a RoomEntity and register that entity
        for (Room room : rooms) {
            RoomEntity roomEntity = new RoomEntity(room);
            EntityManager.register(roomEntity);
            children.add(roomEntity);
        }
    }

    /**
     * Start the countdown until the seekers get spawned in 
     */
    private void startCountdown() {
        RenderManager.initLabel("10");

        Label label = RenderManager.getLabel();
        label.setOpacity(0.5);
        label.setLayoutX(
            GameScene.getGameScreen().getWidth() / 2 - label.getWidth()
        );
        label.setLayoutX(
            GameScene.getGameScreen().getHeight() / 2 - label.getHeight()
        );

        Timeline timeline = new Timeline();

        for (int i = 0; i < 10; i++) {
            timeline.getKeyFrames().add(new KeyFrame(
                Duration.seconds(i + 1f),
                new KeyValue(
                    RenderManager.getLabel().textProperty(),
                    Integer.toString(9 - i)
                )
            ));
        }

        timeline.setOnFinished(e -> {
            RenderManager.displayText();
            initSeekers(SEEKER_COUNT);
            label.setOpacity(1);
            label.setOpacity(0.5);
            label.setFont(new Font(150));
            label.setStyle("-fx-font-weight: normal");
            label.setLayoutX(
                GameScene.getGameScreen().getWidth() / 2 - label.getWidth()
            );
            label.setLayoutX(
                GameScene.getGameScreen().getHeight() / 2 - label.getHeight()
            );
        });

        timeline.play();
    }
    
    /**
     * Construct and initialize numSeekers seekers
     * making the spawn in random rooms but ensuring they aren't spawned 
     * in the same room as the rooms the hiders were spawned in 
     * @param numSeekers The number of seekers to construct
     */
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
                520,
                Color.INDIANRED
            );

            seeker.initAnimations();
            EntityManager.register(seeker);
            RenderManager.register(seeker);
        }
    }

    /**
     * Initialize the item
     */
    private void initItems() {
        children.add(new Burger(rand.nextLong()));
        children.add(new Dessert(rand.nextLong()));
        children.add(new Donut(rand.nextLong()));
        children.add(new HotDog(rand.nextLong()));
        children.add(new Pizza(rand.nextLong()));
		children.add(new FreezeItem(rand.nextLong()));
		children.add(new InvisibilityItem(rand.nextLong()));
		children.add(new SpeedItem(rand.nextLong()));
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
        else if (!lost) {
            timeLosing += timeDelta;
            if (timeLosing >= LOSEDELAY) {
                lost = true;
                App.lose();
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

    /**
     * Method to call when food gets collected and if it's reached the number FOOD_TO_WIN
     * Set the game to win
     */
    public void collectFood() {
        if (++collectedFood == FOOD_TO_WIN) {
            NetworkManager.tick();
            App.win();
        }

        counter.setLabel(String.format("%d/%d", collectedFood, FOOD_TO_WIN));
    }

    /**
     * Appends the given string to the chatLabel
     * @param string The string to append
     */
    public void appendStringToChat(String string) {
        chatLabel.appendString(string);
    }

    /**
     * Sets the chat visibility
     * @param visible Whether the chat is visible or not
     */
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
