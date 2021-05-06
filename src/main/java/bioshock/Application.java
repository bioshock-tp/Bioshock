package bioshock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

@SpringBootApplication
public class Application {

    private static MongoClient client;
    private static DB database;

    public static void main(String[] args) {

        try {
            // Connect to MongoDB Server on localhost, port 27017 (default)
            client = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
            // Connect to Database "bioshock"
            database = client.getDB("bioshock");

        } catch (Exception exception) {
            System.err.println(exception.getClass().getName() + ": " + exception.getMessage());
        }
        SpringApplication.run(Application.class, args);
    }

    static MongoClient getClient() {
        return client;
    }

    static DB getDatabase() {
        return database;
    }

}