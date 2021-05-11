package org.bioshock.scenes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bioshock.components.NetworkC;
import org.bioshock.engine.core.ChatManager;
import org.bioshock.engine.core.FrameRate;
import org.bioshock.engine.input.InputManager;
import org.bioshock.entities.Entity;
import org.bioshock.entities.EntityManager;
import org.bioshock.entities.LabelEntity;
import org.bioshock.entities.items.Bomb;
import org.bioshock.entities.items.Teleporter;
import org.bioshock.entities.items.loot.Bag;
import org.bioshock.entities.items.loot.Chest;
import org.bioshock.entities.items.loot.Coins;
import org.bioshock.entities.items.loot.Crystal;
import org.bioshock.entities.items.loot.Goblet;
import org.bioshock.entities.items.loot.Key;
import org.bioshock.entities.items.loot.Loot;
import org.bioshock.entities.items.loot.Necklace;
import org.bioshock.entities.items.loot.TreasureSack;
import org.bioshock.entities.items.powerups.FreezeItem;
import org.bioshock.entities.items.powerups.InvisibilityItem;
import org.bioshock.entities.items.powerups.SpeedItem;
import org.bioshock.entities.items.powerups.Trap;
import org.bioshock.entities.map.Room;
import org.bioshock.entities.map.RoomEntity;
import org.bioshock.entities.map.Wall;
import org.bioshock.entities.map.maps.GenericMap;
import org.bioshock.entities.map.maps.Map;
import org.bioshock.entities.map.maps.RandomMap;
import org.bioshock.entities.players.Hider;
import org.bioshock.entities.players.SeekerAI;
import org.bioshock.main.App;
import org.bioshock.networking.Account;
import org.bioshock.networking.NetworkManager;
import org.bioshock.rendering.RenderManager;
import org.bioshock.utils.Difficulty;
import org.bioshock.utils.GlobalConstants;
import org.bioshock.utils.Size;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;


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
     * (Currently timer isn't used as win condition is now collecting a certain
     * amount of loot)
     */
    private static final double END_TIME = 2 * 60f + 3;

    /**
     * The amount of time between you losing and the lose screen being shown
     */
    private static final double LOSE_DELAY = 0;

    /**
     * Number of loot items to be collected to win
     */
    private int lootToWin;

    /**
     * The amount of loot currently collected
     */
    private int collectedLoot = 0;

    /**
     * Maps each player to their score
     */
    private java.util.Map<Hider, IntegerProperty> playerScores;

    /**
     * Maps each player to the number of power up items they have collected
     */
    private java.util.Map<Hider, IntegerProperty> playerPowerUpScore;

    /**
     * The counter representing how much loot has been collected
     */
    private LabelEntity counter;

    /**
     * True if the camera is locked to the currentPlayers position
     */
    private boolean cameraLock = true;

    /**
     * The amount of time the game has been running
     */
    private double runningTime = 0;

    /**
     * True if you are losing the game
     */
    private boolean losing = false;

    /**
     * True if you have lost the game
     * i.e. {@link #LOSE_DELAY} amount of time has passed since
     * has been true
     */
    private boolean lost;

    /**
     * The current amount of time you have been losing for
     */
    private double timeLosing = 0;

    /**
     * The timer entity representing how much time is left in the game
     * (Currently timer isn't used as win condition is now collecting a certain
     *  amount of loot)
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

    /**
     * Shows the information about how the player is performing
     */
    private TableView<Hider> scoreboard;

    /**
     * Contains nodes to be shown over the top of the game
     */
    private BorderPane borderPane;


    public MainGame() {
        EntityManager.unregisterAll();

        setCursor(Cursor.HAND);
        setBackground(new Background(new BackgroundFill(
            Color.LIGHTGRAY,
            null,
            null
        )));

        initEntities();
    }


    /**
     * Initialise all entities that don't need the game seed
     * or entities that need entities that need the game seed
     */
    private void initEntities() {
        initHiders();

        children.add(FrameRate.getLabel());

        initChat();

        registerEntities();
    }


    /**
     * Initialise all the hiders but not their positions as the locations won't
     * be known as the map hasn't been generated yet
     */
    private void initHiders() {
        List<Hider> players = new ArrayList<>(App.playerCount());

        /* Players must render in exact order, do not play with z values */
        Hider hider = new Hider(
            new Point3D(0, 0, 0.5),
            new NetworkC(true),
            new Size(
                (double) GlobalConstants.UNIT_WIDTH - PADDING,
                (double) GlobalConstants.UNIT_HEIGHT - PADDING
            ),
            400,
            Color.PINK
        );
        players.add(hider);

        for (int i = 1; i < App.playerCount(); i++) {
            players.add(new Hider(
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

        children.addAll(players);

        playerScores = new HashMap<>(App.playerCount());
        playerPowerUpScore = new HashMap<>(App.playerCount());
        players.forEach(player -> {
            playerScores.put(player, new SimpleIntegerProperty(0));
            playerPowerUpScore.put(player, new SimpleIntegerProperty(0));
        });
    }


    /**
     * Initialise the game timer
     * (Currently timer isn't used as win condition is now collecting a certain
     * amount of items)
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
     * Initialise the chat
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
    }


    @Override
    public void initScene() {
        initScene(SceneManager.getSeed());
    }


    /**
     * @see #initScene()
     * @param seed The seed to be used for map generation
     */
    private void initScene(long seed) {
        RenderManager.setClip(true);

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

            hiders.get(i).setInitPositionX(x - (double) GlobalConstants.UNIT_WIDTH / 2);
            hiders.get(i).setInitPositionY(y - (double) GlobalConstants.UNIT_HEIGHT / 2);

            hiders.get(i).setPosition(
                x - (double) GlobalConstants.UNIT_WIDTH / 2,
                y - (double) GlobalConstants.UNIT_HEIGHT / 2
            );
        }

        initItems();

        initCounter();

        renderEntities();

        startCountdown();

        initScoreboard();

        ChatManager.initialise();

        SceneManager.setInLobby(false);
        SceneManager.setInGame(true);

        if (App.isNetworked()) {
            synchronized(NetworkManager.getPlayerJoinLock()) {
                NetworkManager.getPlayerJoinLock().notifyAll();
            }
            App.logger.debug("Notified networking thread");
        } else {
            // If not networked there should be one player and then initialise
            // the movement and animations of the player/hider
            assert(App.playerCount() == 1);
            EntityManager.getPlayers().get(0).getMovement().initMovement();
            EntityManager.getPlayers().get(0).initAnimations();
        }
    }


    /**
     * Initialise the map with the given seed
     * @param seed
     */
    private void initMap(long seed) {
        // Generate a map based off the seed
        // A random one if in local mode and a pre-made one if in online mode
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

        /*
         * Set the scenes map to the generated map and register all the walls
         * in the map
         * The walls include the objects that are in the room
         */
        SceneManager.setMap(map);
        EntityManager.registerAll(map.getWalls().toArray(new Wall[0]));
        children.addAll(map.getWalls());

        List<Room> rooms = map.getRooms();

        /*
         * For every room in the map put it into a RoomEntity and register that
         * entity
         */
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
        label.setFont(new Font(400));
        label.setStyle("-fx-font-weight: bold");

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

            initSeekers(
                App.getDifficulty() == Difficulty.EASY ?
                    SEEKER_COUNT
                    : SEEKER_COUNT + 1
            );
        });

        timeline.play();
    }


    /**
     * Construct and initialise n seekers spawning them in rooms no player is
     * within
     * @param n The number of seekers to construct
     */
    private void initSeekers(int n) {
        for (int i = 0; i < n; i++) {
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

            if (App.getDifficulty() == Difficulty.HARD) {
                double originalSpeed = seeker.getMovement().getSpeed();
                seeker.getMovement().setSpeed(originalSpeed * 1.1);

                seeker.setRadius(seeker.getRadius() * 1.3);
            }

            seeker.initAnimations();
            EntityManager.register(seeker);
            children.add(seeker);
        }
    }


    /**
     * Initialises the collectible items
     */
    private void initItems() {
        List<Loot> loot = List.of(
            new Bag(rand.nextLong()),
            new Chest(rand.nextLong()),
            new Coins(rand.nextLong()),
            new Crystal(rand.nextLong()),
            new Goblet(rand.nextLong()),
            new Key(rand.nextLong()),
            new Necklace(rand.nextLong()),
            new TreasureSack(rand.nextLong())
        );

        lootToWin = loot.size();

        children.addAll(loot);

		children.add(new FreezeItem(rand.nextLong()));
		children.add(new InvisibilityItem(rand.nextLong()));
		children.add(new SpeedItem(rand.nextLong()));
		children.add(new Teleporter(rand.nextLong()));
		children.add(new Bomb(rand.nextLong()));
        children.add(new Trap(rand.nextLong()));
    }


    /**
     * Initialise the game counter
     */
    private void initCounter() {
        counter = new LabelEntity(
            new Point3D(GameScene.getGameScreen().getWidth() / 2, 50, 100),
            String.format("%d/%d", collectedLoot, lootToWin),
            new Font("arial", 20),
            50,
            Color.LIGHTGRAY
        );

        children.add(counter);
    }


    /**
     * Initialises the scoreboard
     */
    public void initScoreboard() {
        this.getStylesheets().add(GlobalConstants.STYLESHEET_PATH);

        scoreboard = new TableView<>();
        scoreboard.setVisible(false);
        scoreboard.setSelectionModel(null);
        scoreboard.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        scoreboard.getStyleClass().add("table-view");

        final String RIGHT_ALLIGN = "-fx-alignment: CENTER-RIGHT;";

        /* Ping */
        TableColumn<Hider, Integer> ping = new TableColumn<>("Ping");
        ping.setCellValueFactory(cellData ->
            NetworkManager.getPingMap().getOrDefault(
                cellData.getValue(),
                new SimpleIntegerProperty(0)
            ).asObject()
        );
        ping.setSortable(false);
        ping.setStyle(RIGHT_ALLIGN);

        /* Player Names */
        TableColumn<Hider, String> players = new TableColumn<>("Player");
        players.setCellValueFactory(new PropertyValueFactory<>("name"));
        players.setSortable(false);

        /* Power Up Score */
        TableColumn<Hider, Integer> powerUpScore =
            new TableColumn<>("Power Up Score");
        powerUpScore.setCellValueFactory(cellData ->
            playerPowerUpScore.get(cellData.getValue()).asObject()
        );
        powerUpScore.setSortable(false);
        powerUpScore.setStyle(RIGHT_ALLIGN);

        /* Item Score */
        TableColumn<Hider, Integer> score = new TableColumn<>("Score");
        score.setCellValueFactory(cellData ->
            playerScores.get(cellData.getValue()).asObject()
        );
        score.setSortable(false);
        score.setStyle(RIGHT_ALLIGN);

        scoreboard.getColumns().add(ping);
        scoreboard.getColumns().add(players);
        scoreboard.getColumns().add(powerUpScore);
        scoreboard.getColumns().add(score);

        scoreboard.getItems().addAll(EntityManager.getPlayers());

        borderPane = new BorderPane(scoreboard);
        BorderPane.setMargin(scoreboard, new Insets(300, 500, 300, 500));
        SceneManager.getMainGame().getPane().getChildren().add(borderPane);

        InputManager.onPress(KeyCode.TAB, () -> showScoreboard(true));
        InputManager.onRelease(KeyCode.TAB, () -> showScoreboard(false));
    }


    /**
     * @param show True if {@link #scoreboard} should be shown
     */
    public void showScoreboard(boolean show) {
        scoreboard.setVisible(show);
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
            if (timeLosing >= LOSE_DELAY) {
                lost = true;
                App.end(false);
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
     * Handles item collection<p />
     * If number of items collected is {@link #lootToWin}, game is won
     */
    public void collectLoot(Hider hider) {
        if (hider == NetworkManager.me()) {
            Account.setScoreToInc(Account.getScoreToInc() + 1);
        }

        if (++collectedLoot == lootToWin) {

            NetworkManager.tick();

            App.end(true);
        }

        counter.setLabel(String.format("%d/%d", collectedLoot, lootToWin));

        IntegerProperty score = playerScores.get(hider);
        score.set(score.getValue() + 1);
    }


    /**
     * Appends the given string to the chatLabel
     * @param string The string to append
     */
    public void appendStringToChat(String string) {
        chatLabel.appendString(string);
    }


    /**
     * @param visible True if chat should be visible
     */
    public void setChatVisibility(boolean visible) {
        chatLabel.setDisplay(visible);
        textChat.setDisplay(visible);
    }


    /**
     * @return A map of players to how many power up items they have collected
     */
    public void increasePowerUpScore(Hider hider) {
        IntegerProperty playerScore = playerPowerUpScore.get(hider);
        playerScore.set(playerScore.get() + 1);
    }


    /**
     * @return The BorderPane displayed in game
     */
    public BorderPane getBorderPane() {
        return borderPane;
    }


    /**
     * @return The scoreboard displayed in game
     */
    public TableView<Hider> getScoreboard() {
        return scoreboard;
    }


    @Override
    public void destroy() {
        super.destroy();

        Entity[] childArray = children.toArray(new Entity[children.size()]);
        EntityManager.unregisterAll(childArray);
        RenderManager.unregisterAll(children);

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
