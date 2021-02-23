package org.bioshock.networking;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

public class EmptyClient extends WebSocketClient {
    Queue<Messages.ServerToClient> msgQ = new LinkedList<>();
    public EmptyClient(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

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
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("closed with exit code " + code + " additional info: " + reason);
        this.connect();
    }

    @Override
    public void onMessage(String message) {
        //var queueMessage = Messages.Serializer.deserialize(message);
        //msgQ.add(queueMessage);
//        System.out.println("_" + _queueMessage);
        var queueMessage = (Messages.InQueue) Messages.Serializer.deserialize(message);
        System.out.println("timestamp: " + queueMessage.timestamp + "; " + "names: " + Arrays.toString(queueMessage.names) + "; " + "n: " + queueMessage.n + "; " + "N: " + queueMessage.N);
        //System.out.println("received message: " + message);
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
