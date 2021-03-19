package org.bioshock.networking;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import org.bioshock.main.App;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class Client extends WebSocketClient {
    private static final String DEFURI = "ws://51.15.109.210:8010/";

    private int playerNumber;

    private Semaphore mutex = new Semaphore(1);
    private Queue<Message> initialMessages = new ArrayDeque<>();
    private Queue<Message> messageQueue = new ArrayDeque<>();
    private boolean connected = false;

    private Client(URI serverURI) {
        super(serverURI);
    }

    public Client() {
        this(DEFURI);
    }

    public Client(String uri) {
        this(getURI(uri));
    }

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
    }

    @Override
    public void onMessage(String string) {
        /* Case of player number */
        try {
            playerNumber = Integer.parseInt(string);
            send("New Player");
            return;
        } catch (NumberFormatException ignored) {
            /* Was not playerNumber message */
        }

        /* Case of new player joining */
        if (string.equals("New Player")) {
            Message queueMessage = Message.inLobby(
                playerNumber,
                NetworkManager.getMyID()
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

                Object messageMutex = NetworkManager.getPlayerJoinLock();
                synchronized(messageMutex) {
                    messageMutex.notifyAll();
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
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        App.logger.fatal(
            "Server connection closed with exit code {}, additional info: {}",
            code,
            reason
        );
        App.exit(-1);
    }

    public Queue<Message> getInitialMessages() {
        return initialMessages;
    }

    public Queue<Message> getMessageQ() {
        return messageQueue;
    }

    public Semaphore getMutex() {
        return mutex;
    }

    public boolean isConnected() {
        return connected;
    }
}
