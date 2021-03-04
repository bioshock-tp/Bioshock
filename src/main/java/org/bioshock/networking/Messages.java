package org.bioshock.networking;
import org.json.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

public abstract class Messages {
    private Messages() {}

    static abstract class ClientToServer {}
    public static abstract class ServerToClient {}
    // private static final int CONST = 100000;
    public static class Details extends ClientToServer {
        String uuid;
        String game;
        public Details(String uuid, String game){
            this.uuid = uuid;
            this.game = game;
        }
    }

    public static class InQueue extends ServerToClient {
        public String timestamp;
        public String[] uuids;
        public int n;
        public int numberOfPlayers;
        public InQueue(String timestamp, String[] names, int n, int numberOfPlayers) {
            this.timestamp = timestamp;
            this.uuids = names;
            this.n = n;
            this.numberOfPlayers = numberOfPlayers;
        }
    }

    static class GameReady extends ServerToClient {
        String[] names;
        public GameReady(String[] names) {
            this.names = names;
        }
    }

    public static class ServerInputState extends ServerToClient {
        public final String[] names;
        public final ClientInput[] inputs;
        public final double delta;

        public ServerInputState(String[] names, ClientInput[] inputs, double delta) {
            this.names = names;
            this.inputs = inputs;
            this.delta = delta;
        }

        @Override
        public String toString() {
            return "ServerInputState{" +
                    "names=" + Arrays.toString(names) +
                    ", inputs=" + Arrays.toString(inputs) +
                    ", delta=" + delta +
                    '}';
        }
    }

    public static class QueueRequest extends ClientToServer {
        String uuid;
        String game;
        int n;

        public QueueRequest(String uuid, String game, int n) {
            this.uuid = uuid;
            this.game = game;
            this.n = n;
        }
    }

    public static class ClientInput extends ClientToServer {
        public final int x;
        public final int y;

        public ClientInput(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "ClientInput{" +
                    "dirX=" + x +
                    ", dirY=" + y +
                    '}';
        }
    }

    public static class Serializer {
        private Serializer() {}

        public static byte[] serialize(ClientToServer m) {
            if(m instanceof Details) {
                Details d = (Details) m;
                String  s = "{\"Case\":\"Details\",\"Fields\":[\"" + d.uuid + "\",\"" + d.game + "\",[]]}";

                return (s.getBytes(StandardCharsets.UTF_8));
            }

            if(m instanceof QueueRequest) {
                QueueRequest d = (QueueRequest) m;
                String s = "{\"Case\":\"QueueRequest\",\"Fields\":[\"" + d.uuid + "\",\"" + d.game + "\",\"\"," + d.n + ",null]}";

                return (s.getBytes(StandardCharsets.UTF_8));
            }

            if(m instanceof ClientInput) {
                ClientInput d = (ClientInput) m;
                String s1 = "{\"X\":\"" + d.x + "\",\"Y\":\"" + d.y + "\"}";
                String ba = Base64.getEncoder().encodeToString(s1.getBytes(StandardCharsets.UTF_8));
                String s2 = "{\"Case\":\"ClientInput\",\"Fields\":[[],\"" + ba + "\"]}";

                return (s2.getBytes(StandardCharsets.UTF_8));
            }

            return new byte[0];
        }


        public static ServerToClient deserialize(String s) {
            JSONObject obj = new JSONObject(s);
            String messageType = obj.getString("Case");
            var fieldsArray = obj.getJSONArray("Fields");

            if (messageType.equals("InQueue")) {
                var timestamp = fieldsArray.getString(0);
                var jnames = fieldsArray.getJSONArray(1);
                String[] names = new String[jnames.length()];

                for (int i = 0; i<jnames.length(); i++) {
                    names[i] = jnames.getString(i);
                }

                var n = fieldsArray.getInt(2);
                var numberOfPlayers
     = fieldsArray.getInt(3);

                return new InQueue(timestamp, names, n, numberOfPlayers
    );
            }

            if(messageType.equals("GameReady")) {
                var jnames = fieldsArray.getJSONArray(0);
                String[] names = new String[jnames.length()];

                for (int i = 0; i<jnames.length(); i++) {
                    names[i] = jnames.getString(i);
                }

                return new GameReady(names);
            }

            if(messageType.equals("ServerInputState")) {
                var delta = fieldsArray.getInt(2);
                var jarray = fieldsArray.getJSONArray(1);
                String[] names = new String[jarray.length()];
                ClientInput[] inputs = new ClientInput[jarray.length()];

                for (int i = 0; i<jarray.length(); i++) {
                    var json = jarray.getJSONObject(i);
                    names[i] = json.getString("Item1");

                    var json1 = (json.getJSONObject("Item2")).getJSONObject("Item2");
                    var s1 = json1.getJSONArray("Fields").getString(0);
                    var s2 = new String((Base64.getDecoder().decode(s1)), StandardCharsets.UTF_8);

                    JSONObject json2 = new JSONObject(s2);
                    inputs[i] = new ClientInput(json2.getInt("X"), json2.getInt("Y"));
                }

                return new ServerInputState(names, inputs, delta);
            }
            return null;
        }
    }
}
