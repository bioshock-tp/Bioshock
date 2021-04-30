package org.bioshock.networking;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.prefs.Preferences;

import org.bioshock.gui.SettingsController;
import org.bioshock.main.App;
import org.bioshock.scenes.SceneManager;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class Client extends WebSocketClient {
    /** URI of the game's server */
    private static final String DEF_URI = "ws://51.15.109.210:8029/";

    /** The players number within a lobby */
    private int playerNumber;

    private Semaphore mutex = new Semaphore(1);

    /** Messages received within a lobby */
    private Queue<Message> initialMessages = new ArrayDeque<>();

    /** Messages received during gameplay */
    private Queue<Message> messageQueue = new ArrayDeque<>();

    /** True if waiting for initial message */
    private boolean initMessage = true;

    /** Chosen name of player */
    private String playerName;


    /**
     * Creates a {@code Client} with a custom URI
     * @param serverURI URI of server to connect to
     */
    private Client(URI serverURI) {
        super(serverURI);
    }


    /** Creates a {@code Client} using the default URI */
    public Client() {
        this(DEF_URI);
    }


    /**
     * Creates a {@code Client} with a custom URI string
     * @param uri A string containing the URI of the server to connect to
     * @see URI
     */
    public Client(String uri) {
        this(getURI(uri));
    }


    /**
     * Creates a URI object from a string
     * @param uri A string containing the URI of the server to connect to
     * @return A URI object If the string has valid syntax, otherwise
     * {@code null}
     * @see URI
     */
    private static URI getURI(String uri) {
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            App.logger.fatal("Invalid URI {}: ", uri, e);
            App.exit(-1);
            return null; /* Suppress no return value warning */
        }
    }


    @Override
    public void onOpen(ServerHandshake handshakedata) {

        setTcpNoDelay(true);
        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        playerName = prefs.get("playerName", App.getBundle().getString("DEFAULT_PLAYER_NAME_TEXT"));
    }


    @Override
    public void onMessage(String string) {
        /* Case of player number */
        try {
            if (initMessage) {
                playerNumber = Integer.parseInt(string);
                send("10000");
                initMessage = (!initMessage);
            }
            else {
                long seed = Long.parseLong(string);
                SceneManager.setSeed(seed);
                send("New Player");
                initMessage = (!initMessage);
            }
            return;
        } catch (NumberFormatException ignored) {
            /* Was not playerNumber message */
        }

        /* Case of new player joining */
        if (string.equals("New Player")) {
            Message queueMessage = Message.inLobby(
                playerNumber,
                NetworkManager.getMyID(),
                playerName
            );

            send(Message.serialise(queueMessage));
            return;
        }

        Message message = Message.deserialise(string);

        try {
            mutex.acquire();

            /* Case of lobby message */
            if (message.playerNumber > 0) {
                initialMessages.add(message);
                App.logger.debug("Player Joined");

                synchronized(NetworkManager.getPlayerJoinLock()) {
                    NetworkManager.getPlayerJoinLock().notifyAll();
                }
            }

            /* Case of input */
            else {
                messageQueue.add(message);
            }
        } catch(InterruptedException ie) {
            App.logger.error("InterruptedException");
            Thread.currentThread().interrupt();
        } finally {
            mutex.release();
        }
    }


    @Override
    public void onMessage(ByteBuffer message) {
        App.logger.debug("received ByteBuffer");
        onMessage(message.toString());
    }


    @Override
    public void onError(Exception ex) {
        App.logger.error("A network error occurred: ", ex);

        try {
            this.connectBlocking();
        } catch (InterruptedException e) {
            App.logger.error(e);
            Thread.currentThread().interrupt();
        }

    }


    @Override
    public void onClose(int code, String reason, boolean remote) {
        App.logger.fatal(
            "Server connection closed with exit code {}, additional info: {}",
            code,
            reason
        );
        try {
            this.connectBlocking();
        } catch (InterruptedException e) {
            App.logger.error(e);
            Thread.currentThread().interrupt();
        }
    }


    /**
     * @return True if waiting for initial message
     */
    public boolean haveInitMessage() { return initMessage; }


    
    /**
     * @return Chosen name of player
     */
    public String getPlayerName() { return playerName; }


    /**
     * @return Messages received within a lobby
     */
    public Queue<Message> getInitialMessages() {
        return initialMessages;
    }


    /**
     * @return Messages received during gameplay
     * @see #getMutex()
     */
    public Queue<Message> getMessageQ() {
        return messageQueue;
    }


    /**
     * @return A semaphore for accessing {@link #messageQueue}
     */
    public Semaphore getMutex() {
        return mutex;
    }
}
