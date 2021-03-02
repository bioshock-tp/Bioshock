package org.bioshock.networking;
import org.json.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.decimal4j.immutable.*;

public abstract class Messages {
    static abstract class ClientToServer {}
    public static abstract class ServerToClient {}
    private static final int CONST = 100000;
    static class Details extends ClientToServer {
        String name, game;
        public Details(String name, String game){
            this.name = name;
            this.game = game;
        }

    }
    public static class InQueue extends ServerToClient {
        public String timestamp;
        public String[] names;
        public int n;
        public int N;
        public InQueue(String timestamp, String[] names, int n, int N){
            this.timestamp = timestamp;
            this.names = names;
            this.n = n;
            this.N = N;
        }

    }

    static class GameReady extends ServerToClient {
        String[] names;
        public GameReady(String[] names){
            this.names = names;
        }
    }

    public static class ServerInputState extends ServerToClient {
        public final String[] names;
        public final ClientInput[] inputs;
        public final Decimal5f delta;

        public ServerInputState(String[] names, ClientInput[] inputs, Decimal5f delta) {
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

    static class QueueRequest extends ClientToServer {
        String name, game;
        int n;
        public QueueRequest(String name, String game, int n) {
            this.name = name;
            this.game = game;
            this.n = n;
        }
    }

    public static class ClientInput extends ClientToServer {
        public final Decimal5f dirX, dirY;
        public  ClientInput(Decimal5f dirX, Decimal5f dirY ){
            this.dirX = dirX;
            this.dirY = dirY;
        }

        @Override
        public String toString() {
            return "ClientInput{" +
                    "dirX=" + dirX +
                    ", dirY=" + dirY +
                    '}';
        }
    }

    public static class Serializer{

        public static byte[] serialize(ClientToServer m) {
            if(m instanceof Details) {
                var d = (Details)m;
                var s = "{\"Case\":\"Details\",\"Fields\":[\"" + d.name + "\",\"" + d.game + "\",[]]}";
                return (s.getBytes(StandardCharsets.UTF_8));
            }
            if(m instanceof QueueRequest) {
                var d = (QueueRequest)m;
                var s = "{\"Case\":\"QueueRequest\",\"Fields\":[\"" + d.name + "\",\"" + d.game + "\",\"\"," + d.n + ",null]}";
                return (s.getBytes(StandardCharsets.UTF_8));
            }
            if(m instanceof ClientInput) {
                var d = (ClientInput)m;
                var s1 = "{\"X\":\"" + d.dirX.multiply(CONST).intValue()+ "\",\"Y\":\"" + d.dirY.multiply(CONST).intValue() + "\"}";
                //System.out.println(s1);
                var ba = Base64.getEncoder().encodeToString(s1.getBytes(StandardCharsets.UTF_8));

                var s2 = "{\"Case\":\"ClientInput\",\"Fields\":[[],\"" + ba + "\"]}";
                //System.out.println("My input: " + s2);
                return (s2.getBytes(StandardCharsets.UTF_8));
            }
            return new byte[0];
        }


        public static ServerToClient deserialize(String s) {
            JSONObject obj = new JSONObject(s);
            String messageType = obj.getString("Case");
            //System.out.println(messageType);
            var fieldsArray = obj.getJSONArray("Fields");
            //System.out.println(fieldsArray);
            if (messageType.equals("InQueue")) {
                var timestamp = fieldsArray.getString(0);
                //System.out.println("Before names");
                var jnames = fieldsArray.getJSONArray(1);
                String[] names = new String[jnames.length()];
                for (int i = 0; i<jnames.length(); i++) {
                    names[i] = jnames.getString(i);
                    //System.out.println(i + " " +  jnames + " " + jnames.getString(i));
                }
                //System.out.println("!!" + Arrays.toString(names));
                //System.out.println("After names");
                var n = fieldsArray.getInt(2);
                var N = fieldsArray.getInt(3);
                return new InQueue(timestamp, names, n, N);
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
                var delta = (Decimal5f.valueOf(fieldsArray.getInt(2))).divide(1000);
                var jarray = fieldsArray.getJSONArray(1);
                String[] names = new String[jarray.length()];
                ClientInput[] inputs = new ClientInput[jarray.length()];
                for (int i = 0; i<jarray.length(); i++) {
                    var json = jarray.getJSONObject(i);
                    names[i] = json.getString("Item1");
                    var json1 = (json.getJSONObject("Item2")).getJSONObject("Item2");
                    var s1 = json1.getJSONArray("Fields").getString(0);
                    var s2 = new String((Base64.getDecoder().decode(s1)), StandardCharsets.UTF_8);
                    //System.out.println(s2);
                    JSONObject json2 = new JSONObject(s2);
                    inputs[i] = new ClientInput(Decimal5f.valueOf(json2.getInt("X")).divide(CONST), Decimal5f.valueOf(json2.getInt("Y")).divide(CONST));
                }
                return new ServerInputState(names, inputs, delta);
            }

            //System.out.println("fail deserialize");
            return null;
//
        }
    }
}
