package bioshock;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;


public class Player {

    private String name;
    private String password;
    private int score;

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getScore() {
        return score;
    }

    public DBObject toDBObject(){
        return BasicDBObjectBuilder.start().
                add("Name", name).
                add( "Password", password).
                add("Score", score).get();
    }

    public DBObject toDBObjectWithoutScore(){
        return BasicDBObjectBuilder.start().
                add("Name", name).
                add( "Password", password).get();
    }

    public DBObject toDBObjectWithoutPassword(){
        return BasicDBObjectBuilder.start().
                add("Name", name).
                add( "Score", score).get();
    }

    public String toString() {
        return "{\"Name\":\"" + name + "\",\"Password\":\"" + password + "\",\"Score\":\"" + score + "\"]}";
    }

    Player(String Name, String Password){
        name = Name;
        password = Password;
        score = 0;
    }

    Player(String Name, String Password, int Score){
        name = Name;
        password = Password;
        score = Score;
    }

}
