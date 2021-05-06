package bioshock;

import com.mongodb.DBCursor;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;

import java.net.UnknownHostException;
import java.util.Iterator;

@RestController
public class Controller {

    @PostMapping("/register")
    public DBObject newPlayer(@RequestBody String request) {
        JSONObject objReq = new JSONObject(request);
        String name = objReq.getString("Name");
        String password = objReq.getString("Password");
        String passwordConfirmation = objReq.getString("PasswordConfirmation");

        if(name == null || password == null || passwordConfirmation == null){
            return BasicDBObjectBuilder.start().add("Message", "Bad request").add("Status", 500).get();
        }

        if(!(UsefulFunctions.onlyDigitsAndLetters(name) && UsefulFunctions.onlyDigitsAndLetters(password) && UsefulFunctions.onlyDigitsAndLetters(passwordConfirmation))){
            return BasicDBObjectBuilder.start().add("Message", "Name, Password and PasswordConfirmation must contain only digits or letters").add("Status", 500).get();
        }
        if(name.length() < 3 || name.length() > 10){
            return BasicDBObjectBuilder.start().add("Message", "Name should be between 3 and 10 characters long").add("Status", 500).get();
        }

        if(password.length() < 4 || password.length() > 15){
            return BasicDBObjectBuilder.start().add("Message", "Password should be between 4 and 15 characters long").add("Status", 500).get();
        }

        if(!password.equals(passwordConfirmation)){
            return BasicDBObjectBuilder.start().add("Message", "Password not the same as the PasswordConfirmation").add("Status", 500).get();
        }
        String hashedPassword;
        try {
            hashedPassword = UsefulFunctions.getSHA(password);
        }
        catch (NoSuchAlgorithmException err){
            return BasicDBObjectBuilder.start().add("Message", "Server Error").add("Status", 500).get();
        }

        Player player = new Player(name, hashedPassword);
        Session session = new Session(UsefulFunctions.generateCode(200), name);

        try {
            if (CRUDMongoDB.isExistent("Players", BasicDBObjectBuilder.start().add("Name", name).get())){
                return BasicDBObjectBuilder.start().add("Message", "Name Already used").add("Status", 500).get();
            }

            CRUDMongoDB.createOne("Players", player.toDBObject());
            CRUDMongoDB.createOne("Sessions", session.toDBObject());

        }
        catch (UnknownHostException err){
            return BasicDBObjectBuilder.start().add("Message", "Server Error").add("Status", 500).get();
        }
        return BasicDBObjectBuilder.start().
                add("Message", "You are registered").add("Token", session.getToken()).add("Score", 0).add("Status", 200).get();
        //return "{\"Message\":\"User Successfully inserted\"}";
    }

    @PostMapping("/login")
    public DBObject newSession(@RequestBody String request) {

        JSONObject objReq = new JSONObject(request);
        String name = objReq.getString("Name");
        String password = objReq.getString("Password");

        if(name == null || password == null){
            return BasicDBObjectBuilder.start().add("Message", "Bad request").add("Status", 500).get();
        }

        if(!(UsefulFunctions.onlyDigitsAndLetters(name) && UsefulFunctions.onlyDigitsAndLetters(password))){
            return BasicDBObjectBuilder.start().add("Message", "Name and Password must contain only digits or letters").add("Status", 500).get();
        }
        if(name.length() < 3 || name.length() > 10){
            return BasicDBObjectBuilder.start().add("Message", "Name should be between 3 and 10 characters long").add("Status", 500).get();
        }

        if(password.length() < 4 || password.length() > 15){
            return BasicDBObjectBuilder.start().add("Message", "Password should be between 4 and 15 characters long").add("Status", 500).get();
        }

        String hashedPassword;
        try {
            hashedPassword = UsefulFunctions.getSHA(password);
        }
        catch (NoSuchAlgorithmException err){
            return BasicDBObjectBuilder.start().add("Message", "Server Error").add("Status", 500).get();
        }

        Player player = new Player(name, hashedPassword);

        Session session = new Session(UsefulFunctions.generateCode(200), name);

        try {
            if(!CRUDMongoDB.isExistent("Players", player.toDBObjectWithoutScore())) {
                return BasicDBObjectBuilder.start().add("Message", "Name or password incorrect").add("Status", 500).get();
            }

            DBObject dbPlayer = CRUDMongoDB.findOne("Players", player.toDBObjectWithoutScore());

            player = new Player(name, hashedPassword, new Integer(dbPlayer.get("Score").toString()));

            CRUDMongoDB.delete("Sessions", BasicDBObjectBuilder.start().add("Name", name).get());
            CRUDMongoDB.createOne("Sessions", session.toDBObject());
        }
        catch (UnknownHostException err){
            return BasicDBObjectBuilder.start().add("Message", "Server Error").add("Status", 500).get();
        }

        return BasicDBObjectBuilder.start().
                add("Message", "You are logged in").add("Token", session.getToken()).add("Score", player.getScore()).add("Status", 200).get();
    }

    @PutMapping("/increaseScore")
    public DBObject increaseScore(@RequestBody String request) {

        JSONObject objReq = new JSONObject(request);
        String token = objReq.getString("Token");
        int score = objReq.getInt("Score");

        if(token == null){
            return BasicDBObjectBuilder.start().add("Message", "Bad request").add("Status", 500).get();
        }

        if(score <= 0) {
            return BasicDBObjectBuilder.start().add("Message", "The score for increasing must be bigger than 0").add("Status", 500).get();
        }

        if(!(UsefulFunctions.onlyDigitsAndLetters(token))){
            return BasicDBObjectBuilder.start().add("Message", "Your token is corrupt, please login correctly!").add("Status", 500).get();
        }

        try {

            if(!CRUDMongoDB.isExistent("Sessions", BasicDBObjectBuilder.start().add("Token", token).get())) {
                return BasicDBObjectBuilder.start().add("Message", "Your token is corrupt, please login correctly!").add("Status", 500).get();
            }

            DBObject sessObj = CRUDMongoDB.findOne("Sessions", BasicDBObjectBuilder.start().add("Token", token).get());
            Session session = new Session(sessObj.get("Token").toString(), sessObj.get("Name").toString(), new Long(sessObj.get("Time").toString()));

            long sessTime = session.getTime();
            long currTime = System.currentTimeMillis();

            if(currTime - sessTime > 18000000){
                return BasicDBObjectBuilder.start().add("Message", "Your token is expired, please login again!").add("Status", 500).get();
            }

            DBObject playerObj = CRUDMongoDB.findOne("Players", BasicDBObjectBuilder.start().add("Name", session.getName()).get());

            Player player = new Player(playerObj.get("Name").toString(), playerObj.get("Password").toString(), new Integer(playerObj.get("Score").toString()));
            Player updatePlayer = new Player(player.getName(), player.getPassword(), player.getScore() + score);

            CRUDMongoDB.updateOne("Players", player.toDBObject(), updatePlayer.toDBObject());

            score = updatePlayer.getScore();

        }
        catch (UnknownHostException err){
            return BasicDBObjectBuilder.start().add("Message", "Server Error").add("Status", 500).get();
        }

        return BasicDBObjectBuilder.start().add("Message", "Score successfully increased!").add("Score", score).add("Status", 200).get();
    }

    @GetMapping("/getYourScore")
    public DBObject getYourScore(@RequestBody String request) {

        JSONObject objReq = new JSONObject(request);
        String token = objReq.getString("Token");
        int score;

        if(token == null){
            return BasicDBObjectBuilder.start().add("Message", "Bad request").add("Status", 500).get();
        }

        if(!(UsefulFunctions.onlyDigitsAndLetters(token))){
            return BasicDBObjectBuilder.start().add("Message", "Your token is corrupt, please login correctly!").add("Status", 500).get();
        }

        try {

            if(!CRUDMongoDB.isExistent("Sessions", BasicDBObjectBuilder.start().add("Token", token).get())) {
                return BasicDBObjectBuilder.start().add("Message", "Your token is corrupt, please login correctly!").add("Status", 500).get();
            }

            DBObject sessObj = CRUDMongoDB.findOne("Sessions", BasicDBObjectBuilder.start().add("Token", token).get());
            Session session = new Session(sessObj.get("Token").toString(), sessObj.get("Name").toString(), new Long(sessObj.get("Time").toString()));

            long sessTime = session.getTime();
            long currTime = System.currentTimeMillis();

            if(currTime - sessTime > 18000000){
                return BasicDBObjectBuilder.start().add("Message", "Your token is expired, please login again!").add("Status", 500).get();
            }

            DBObject playerObj = CRUDMongoDB.findOne("Players", BasicDBObjectBuilder.start().add("Name", session.getName()).get());

            Player player = new Player(playerObj.get("Name").toString(), playerObj.get("Password").toString(), new Integer(playerObj.get("Score").toString()));

            score = player.getScore();

        }
        catch (UnknownHostException err){
            return BasicDBObjectBuilder.start().add("Message", "Server Error").add("Status", 500).get();
        }

        return BasicDBObjectBuilder.start().add("Score", score).add("Status", 200).get();
    }

    @GetMapping("/getTop5Scores")
    public DBObject getTop5Scores() {

        int[] scores = new int[6];
        String[] names = new String[6];
        for(int i = 1; i < 6; i++) {
            scores[i] = 0;
            names[i] = "";
        }

        try {

            DBCursor results = CRUDMongoDB.findAllAndSort("Players", BasicDBObjectBuilder.start().add("Score", -1).get());
            Iterator<DBObject> iterator = results.iterator();
            int i = 0;
            while(iterator.hasNext() && i < 5) {

                i++;
                DBObject currIterator = iterator.next();
                names[i] = currIterator.get("Name").toString();
                scores[i] = new Integer(currIterator.get("Score").toString());

            }

        }
        catch (UnknownHostException err){
            return BasicDBObjectBuilder.start().add("Message", "Server Error").add("Status", 500).get();
        }

        return BasicDBObjectBuilder.start()
                .add("Score1", scores[1]).add("Name1", names[1])
                .add("Score2", scores[2]).add("Name2", names[2])
                .add("Score3", scores[3]).add("Name3", names[3])
                .add("Score4", scores[4]).add("Name4", names[4])
                .add("Score5", scores[5]).add("Name5", names[5])
                .add("Status", 200).get();
    }

}