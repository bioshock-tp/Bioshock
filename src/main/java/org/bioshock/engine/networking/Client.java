package org.bioshock.engine.networking;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import org.bioshock.main.App;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class Client extends WebSocketClient {
    private static final String DEFURI = "ws://51.15.109.210:8010/lobby";

    private Semaphore mutex = new Semaphore(1);
    private Queue<Message> initialMessages = new LinkedList<>();
    private Queue<Message> inputQueue = new LinkedList<>();
    private boolean connected = false;

    private Client(URI serverURI) {
        super(serverURI);
    }

    public Client() {
        this(getDefaultURI());
    }

    public static URI getDefaultURI() {
        try {
            return new URI(DEFURI);
        } catch (URISyntaxException e) {
            App.logger.error("Invalid default URI; {}", e.getMessage());
            App.exit();
            return null; /* Suppress no return value warning */
        }
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        setTcpNoDelay(true);
        Message queueMessage = Message.inLobby(
            NetworkManager.getMe()
        );
        App.logger.debug("Sending {}", queueMessage);
        App.logger.debug("Serialised {}", Message.serialise(queueMessage));
        // send(Message.serialize(queueMessage));
        send("yo");
        App.logger.debug("Sent");
        connected = true;
        App.logger.debug("Changed Boolean (end of onOpen)");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        App.logger.error(
            "Closed with exit code {} additional info: {}",
            code,
            reason
        );
        connect();
    }

    @Override
    public void onMessage(String string) {
        // App.logger.debug("Un-serialised {}", string);
        // Message message = Message.deserialize(string);
        // App.logger.debug("Message {}", message);
        // try {
        //     mutex.acquire();

        //     if (!message.uuid.isEmpty() && message.input == null) {
        //         /* In this case message is lobby message */
        //         getInitialMessages().add(message);

        //         App.logger.debug("message");
        //         Object messageMutex = NetworkManager.getMessageMutex();
        //         synchronized(messageMutex) {
        //             messageMutex.notifyAll();
        //         }

        //         return;
        //     }

        //     inputQueue.add(message);
        // } catch(InterruptedException ie) {
        //     Thread.currentThread().interrupt();
        // } finally {
        //     mutex.release();
        // }
        App.logger.debug("yo");
    }

    @Override
    public void onMessage(ByteBuffer message) {
        App.logger.debug("received ByteBuffer");
    }

    @Override
    public void onError(Exception ex) {
        App.logger.error("A network error occurred: {}", ex.getMessage());
    }

    public Queue<Message> getInitialMessages() {
        return initialMessages;
    }

	public Queue<Message> getMsgQ() {
		return inputQueue;
	}

    public Semaphore getMutex() {
        return mutex;
    }

    public boolean isConnected() {
        return connected;
    }
}
