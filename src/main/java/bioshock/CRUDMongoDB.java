package bioshock;
import bioshock.Application;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import java.net.UnknownHostException;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

public class CRUDMongoDB {

    private static MongoClient client = Application.getClient();
    private static DB database = Application.getDatabase();

    public static void createOne(String collName, DBObject obj) throws UnknownHostException {
        DBCollection collection = database.getCollection(collName);
        collection.insert(obj);
        return;
    }

    public static void createMany(String collName, DBObject[] obj) throws UnknownHostException {
        DBCollection collection = database.getCollection(collName);
        collection.insert(obj);
        return;
    }

    public static DBObject findOne(String collName, DBObject obj) throws UnknownHostException {
        DBCollection collection = database.getCollection(collName);
        DBObject result = collection.findOne(obj);
        return result;
    }

    public static DBCursor findMany(String collName, DBObject obj) throws UnknownHostException {
        DBCollection collection = database.getCollection(collName);
        DBCursor result = collection.find(obj);
        return result;
    }

    public static DBCursor findAll(String collName) throws UnknownHostException {
        DBCollection collection = database.getCollection(collName);
        DBCursor result = collection.find();
        return result;
    }

    public static DBCursor findAllAndSort(String collName, DBObject sortObj) throws UnknownHostException {
        DBCollection collection = database.getCollection(collName);
        DBCursor result = collection.find().sort(sortObj);
        return result;
    }

    public static boolean isExistent(String collName, DBObject obj) throws UnknownHostException {
        DBCollection collection = database.getCollection(collName);
        DBObject result = collection.findOne(obj);
        return (result != null);
    }

    public static void delete(String collName, DBObject obj) throws UnknownHostException {
        DBCollection collection = database.getCollection(collName);
        collection.remove(obj);
        return;
    }

    public static void updateOne(String collName, DBObject obj, DBObject update) throws UnknownHostException {
        DBCollection collection = database.getCollection(collName);
        collection.update(obj, update);
        return;
    }

    public static void updateMany(String collName, DBObject obj, DBObject update) throws UnknownHostException {
        DBCollection collection = database.getCollection(collName);
        collection.updateMulti(obj, update);
        return;
    }

}
