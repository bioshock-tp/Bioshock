import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class Server extends WebSocketServer {
    private static final String host = "51.15.109.210";
    private static final int port = 8010;

    public Server(InetSocketAddress address) {
		super(address);
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		conn.send(Integer.toString(getConnections().size()));

		System.out.println(String.format(
            "New connection to %s",
            conn.getRemoteSocketAddress()
        ));
	}

	@Override
	public void onClose(WebSocket conn, int code, String reas, boolean remote) {
        System.out.println(String.format(
            "Closed %s with exit code %d additional info: %s",
            conn.getRemoteSocketAddress(),
            code,
            reas
        ));
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		System.out.println(String.format(
            "Received message from %s: %s",
            conn.getRemoteSocketAddress(),
            message
        ));

        broadcast(message);
	}

	@Override
	public void onMessage( WebSocket conn, ByteBuffer message) {
		System.out.println(String.format(
            "Received ByteBuffer from %s: %s",
            conn.getRemoteSocketAddress(),
            message
        ));

        onMessage(conn, message.toString());
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		System.out.println(String.format(
            "An error occurred on connection %s: %s",
            conn.getRemoteSocketAddress(),
            ex
        ));
	}

	@Override
	public void onStart() {
		System.out.println("Server started successfully");
	}

	public static void main(String[] args) {
		WebSocketServer server = new Server(new InetSocketAddress(host, port));
		server.run();
	}
}
