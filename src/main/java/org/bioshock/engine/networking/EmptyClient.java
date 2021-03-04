package org.bioshock.engine.networking;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import org.bioshock.main.App;
import org.bioshock.networking.Messages;
import org.bioshock.networking.Messages.ServerToClient;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;


public class EmptyClient extends WebSocketClient {
    private static final String DEFURI = "ws://51.15.109.210:8010/lobby";

    private Semaphore mutex = new Semaphore(1);
    private Queue<Messages.ServerToClient> msgQ = new LinkedList<>();
    private boolean connected = false;

    private EmptyClient(URI serverURI) {
        super(serverURI);
    }

    // private EmptyClient(URI serverUri, Draft draft) {
    //     super(serverUri, draft);
    // }

    public EmptyClient() {
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
        this.setTcpNoDelay(true);
        var initMesage = new Messages.Details(NetworkManager.getMe(), "hideandseek");
        send(Messages.Serializer.serialize(initMesage));

        var queuMesage = new Messages.QueueRequest(NetworkManager.getMe(), "hideandseek", 2);
        send(Messages.Serializer.serialize(queuMesage));

        connected = true;
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        App.logger.debug("closed with exit code {} additional info: {}", code, reason);
        this.connect();
    }

    @Override
    public void onMessage(String message) {
        var queueMessage = Messages.Serializer.deserialize(message);
        try {
            mutex.acquire();
            try {
                msgQ.add(queueMessage);
            } finally {
                mutex.release();
            }
        } catch(InterruptedException ie) {
            App.logger.error(ie);
            Thread.currentThread().interrupt(); /* Shoud not ignore interuptions */
        }
    }

    @Override
    public void onMessage(ByteBuffer message) {
        App.logger.debug("received ByteBuffer");
    }

    @Override
    public void onError(Exception ex) {
        App.logger.error("an error occurred: {}", ex.getMessage());
    }

	public Queue<ServerToClient> getMsgQ() {
		return msgQ;
	}

    public Semaphore getMutex() {
        return mutex;
    }

    public boolean isConnected() {
        return connected;
    }
}
