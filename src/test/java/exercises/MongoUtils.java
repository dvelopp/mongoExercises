package exercises;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.net.UnknownHostException;

public class MongoUtils {

    /**
     * TODO Get rid of hardcoded URLs
     */
    public static MongoClient getMongoClient() throws UnknownHostException {
        return new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
    }

}
