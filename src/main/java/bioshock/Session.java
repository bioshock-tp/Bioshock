package bioshock;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import java.util.*;

public class Session {

    String token;
    String name;
    long time;

    public Session(String token, String name) {
        this.token = token;
        this.name = name;
        this.time = System.currentTimeMillis();
    }

    public Session(String token, String name, long time) {
        this.token = token;
        this.name = name;
        this.time = time;
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public long getTime() {
        return time;
    }

    public DBObject toDBObject(){
        return BasicDBObjectBuilder.start().
                add("Token", token).
                add( "Name", name).
                add("Time", time).get();
    }

    public String toString() {
        return "{\"Token\":\"" + token + "\",\"Name\":\"" + name + "\",\"Time\":\"" + time + "\"]}";
    }

}
