package org.bioshock.networking;
import org.json.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class Messages {
    static abstract class ClientToServer {}
    static abstract class ServerToClient {}
    static class Details extends ClientToServer {
        String name, game;
        public Details(String name, String game){
            this.name = name;
            this.game = game;
        }

    }
    static class InQueue extends ServerToClient {
        String timestamp;
        String[] names;
        int n, N;
        public InQueue(String timestamp, String[] names, int n, int N){
            this.timestamp = timestamp;
            this.names = names;
            this.n = n;
            this.N = N;
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

    static class Serializer{

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
            //System.out.println("fail deserialize");
            return null;
//
        }
    }
}
