import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class Client {
    private int clientType;
    private int gameSize;
    private long gameSeed;
    private WebSocket conn;

    Client(WebSocket conn){
        this.clientType = 0;
        this.gameSize = -1;
        this.conn = conn;
    }

    public long getGameSeed() {
        return gameSeed;
    }

    int getType() {
        return this.clientType;
    }

    int getGameSize() {
        return this.gameSize;
    }

    WebSocket getConn(){
        return this.conn;
    }

    public void setGameSeed(long gameSeed) {
        this.gameSeed = gameSeed;
    }

    void setClientType(int clientType){
        this.clientType = clientType;
    }

    void setGameSize(int gameSize){
        this.gameSize = gameSize;
    }
}
