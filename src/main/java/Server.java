import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.concurrent.*;
import java.util.*;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class Server extends WebSocketServer {
    //private static final String host = "51.15.109.210";
    //String uuid = UUID.randomUUID().toString();
	private static final int port = 8029;
	HashMap<WebSocket,Client> clients=new HashMap<WebSocket,Client>();
	HashMap<String,Set<WebSocket>> games=new HashMap<String,Set<WebSocket>>();
	HashMap<WebSocket,String> players = new HashMap<WebSocket,String>();
	HashMap<Integer,Set<WebSocket>> lobbies = new HashMap<Integer,Set<WebSocket>>();
	HashMap<Integer, Long> lobbiesSeed = new HashMap<Integer, Long>();
	public Semaphore mutex = new Semaphore(1);
	//Set<String> s = new HashSet<String>();

    public Server(InetSocketAddress address) {
		super(address);
	}

	private void removeClient(WebSocket conn){
		try {
			mutex.acquire();
			Client currClient = clients.get(conn);
			if (currClient != null && currClient.getType() == 2) {
				String currGame = players.get(conn);
				Set<WebSocket> currSet = games.get(currGame);
				currSet.remove(conn);
				if (currSet.size() == 0)
					games.remove(currGame);
				else
					games.replace(currGame, currSet);
			} else if (currClient != null && currClient.getType() == 1) {
				Integer exNr = currClient.getGameSize();
				Set<WebSocket> currLobby = lobbies.get(exNr);
				currLobby.remove(conn);
				lobbies.replace(exNr, currLobby);
				broadcast(Integer.toString(currLobby.size()), currLobby);
			}
			clients.remove(conn);
			players.remove(conn);
		} catch(InterruptedException ie) {
			System.out.println(ie);
		} finally {
			mutex.release();
		}
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		try {
			System.out.println("Someone Joint");
			mutex.acquire();
			clients.put(conn, new Client(conn));
			players.put(conn, "");
		} catch(InterruptedException ie) {
			System.out.println(ie);
			Thread.currentThread().interrupt();
		} finally {
			mutex.release();
		}
    	//conn.send(Integer.toString(getConnections().size()));
		//System.out.println(String.format(
            //"New connection to %s",
            //conn.getRemoteSocketAddress()
       //));
	}

	@Override
	public void onClose(WebSocket conn, int code, String reas, boolean remote) {

		removeClient(conn);
		System.out.println("Somone closed");
        //System.out.println(String.format(
        //    "Closed %s with exit code %d additional info: %s",
        //    conn.getRemoteSocketAddress(),
       //     code,
       //     reas
       // ));
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		//System.out.println(String.format(
           // "Received message from %s: %s",
           // conn.getRemoteSocketAddress(),
           // message
       // ));
		try {
			mutex.acquire();
			Client currClient = clients.get(conn);
			try {
				int nr = Integer.parseInt(message);
				if(nr <= 0){
					if(currClient.getType() == 2) {
						String currGame = players.get(conn);
						Set<WebSocket> currSet = games.get(currGame);
						currSet.remove(conn);
						currClient.setClientType(0);
						if(currSet.size() == 0)
							games.remove(currGame);
						else
							games.replace(currGame, currSet);
						players.replace(conn, "");
					}
					else if(currClient.getType() == 1){
						Integer exNr = currClient.getGameSize();
						Set<WebSocket> currLobby = lobbies.get(exNr);
						currLobby.remove(conn);
						lobbies.replace(exNr, currLobby);
						currClient.setClientType(0);
						broadcast(Integer.toString(currLobby.size()), currLobby);
					}
					return;
				}
				if(nr == 10000){
					Set<WebSocket> auxSet = new HashSet<WebSocket>();
					auxSet.add(conn);
					broadcast(Long.toString(currClient.getGameSeed()), auxSet);
					return;
				}
				else {
					if(currClient.getType() == 0){
						currClient.setGameSize(nr);
						currClient.setClientType(1);
						currClient.setGameSeed(lobbiesSeed.get(nr));
						Set<WebSocket> currLobby = lobbies.get(nr);
						Set<WebSocket> currGame = new HashSet<WebSocket>();
						currLobby.add(conn);
						Set<WebSocket> auxSet = new HashSet<WebSocket>();
						auxSet.add(conn);
						if(currLobby.size() == nr){
							System.out.println("Game Start");
							String uuid = UUID.randomUUID().toString();
							for(WebSocket currWeb: currLobby){
								currClient = clients.get(currWeb);
								currClient.setClientType(2);
								players.replace(currWeb, uuid);
							}
							currGame.addAll(currLobby);
							games.put(uuid, currGame);
							broadcast(Integer.toString(currLobby.size()), auxSet);
							//broadcast(Integer.toString(currLobby.size()), currLobby);
							currLobby.clear();
							lobbies.replace(nr, currLobby);
							lobbiesSeed.replace(nr, new Random().nextLong());
							return;
						}
						lobbies.replace(nr, currLobby);
						//conn.send(Integer.toString(currLobby.size()));
						broadcast(Integer.toString(currLobby.size()), auxSet);
					}
				}
				return;
			} catch (NumberFormatException ignored) {
				/* Was not playerNumber message */
			}

			if(message.equals("New Player")){
				System.out.println(currClient.getGameSize());
			}

			if(currClient.getType() == 1){
				broadcast(message, lobbies.get(currClient.getGameSize()));
			}
			else if(currClient.getType() == 2){
				String currGame = players.get(conn);
				broadcast(message, games.get(currGame));
			}
		} catch(InterruptedException ie) {
			System.out.println(ie);
		} finally {
			mutex.release();
		}

	}

	@Override
	public void onMessage( WebSocket conn, ByteBuffer message) {
		//System.out.println(String.format(
            //"Received ByteBuffer from %s: %s",
            //conn.getRemoteSocketAddress(),
           // message
        //));
		try {
			mutex.acquire();
			onMessage(conn, message.toString());
		} catch(InterruptedException ie) {
			System.out.println(ie);
		} finally {
			mutex.release();
		}
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {

    	removeClient(conn);
    	System.out.println(String.format(
            "An error occurred: %s",
            ex
        ));
	}

	@Override
	public void onStart() {

    	for(int i = 1; i <= 15; i++){

			lobbies.put(i, new HashSet<WebSocket>());
			lobbiesSeed.put(i, new Random().nextLong());
		}
    	games.put("", new HashSet<WebSocket>());
    	System.out.println("Server started successfully");
	}

	public static void main(String[] args) {
		//WebSocketServer server = new Server(new InetSocketAddress(host, port));
		WebSocketServer server = new Server(new InetSocketAddress(port));
		server.run();
	}
}
