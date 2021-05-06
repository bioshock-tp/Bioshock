package bioshock;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;

public class Result {
    private String name;
    private int score;

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public DBObject toDBObject(){
        return BasicDBObjectBuilder.start().
                add("Name", name).
                add("Score", score).get();
    }

    @Override
    public String toString() {
        return "{\"Name\":\"" + name + "\",\"Score\":\"" + score + "\"]}";
    }

    Result(String Name, int Score){
        name = Name;
        score = Score;
    }
}
