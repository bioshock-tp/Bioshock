package org.bioshock.networking;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import org.decimal4j.immutable.Decimal5f;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.*;

public class EmptyClient extends WebSocketClient {
    public Semaphore mutex = new Semaphore(1);
    public Queue<Messages.ServerToClient> msgQ = new LinkedList<>();
    public EmptyClient(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }
    public boolean connected = false;
    public EmptyClient(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        this.setTcpNoDelay(true);
        //var s = "{\"Case\":\"Details\",\"Fields\":[\"mircea\",\"hidenseek\",[]]}";
        //send(s.getBytes(StandardCharsets.UTF_8));
        //var s = "{\"Case\":\"QueueRequest\",\"Fields\":[\"mircea\",\"hidenseek\",\"\",2,null]}";
        //send(s.getBytes(StandardCharsets.UTF_8));
        //send("Hello, it is me :)");
        var initMesage = new Messages.Details("mircea", "hideandseek");
        send(Messages.Serializer.serialize(initMesage));
        var queuMesage = new Messages.QueueRequest("mircea", "hideandseek", 2);
        send(Messages.Serializer.serialize(queuMesage));
        //System.out.println("new connection opened");
        connected = true;
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("closed with exit code " + code + " additional info: " + reason);
        this.connect();
    }

    @Override
    public void onMessage(String message) {
        //System.out.println("received message: " + message);
        var queueMessage = (Messages.ServerToClient) Messages.Serializer.deserialize(message);
        try {
            mutex.acquire();
            try {
                msgQ.add(queueMessage);
            } finally {
                mutex.release();
            }
        } catch(InterruptedException ie) {
            System.out.println(ie);
        }
//        System.out.println("_" + _queueMessage);
        /*var queueMessage = (Messages.ServerToClient) Messages.Serializer.deserialize(message);
        if(queueMessage instanceof Messages.InQueue) {
            var d = (Messages.InQueue)queueMessage;
            System.out.println("timestamp: " + d.timestamp + "; " + "names: " + Arrays.toString(d.names) + "; " + "n: " + d.n + "; " + "N: " + d.N);
        }
        else if(queueMessage instanceof Messages.ServerInputState){
            var d = (Messages.ServerInputState)queueMessage;
            System.out.println(d);
        }*/
    }

    @Override
    public void onMessage(ByteBuffer message) {
        System.out.println("received ByteBuffer");
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("an error occurred:" + ex);
    }

    /*public static void main(String[] args) throws URISyntaxException {
        WebSocketClient client = new EmptyClient(new URI("ws://localhost:8887"));
        client.connect();
    }*/
}
