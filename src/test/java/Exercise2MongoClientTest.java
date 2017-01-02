import com.mongodb.*;
import org.hamcrest.CoreMatchers;
import org.junit.*;

import java.net.UnknownHostException;


public class Exercise2MongoClientTest {
    @Test
    public void shouldGetADatabaseFromTheMongoClient() throws Exception {
        // Given
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));

        // When
        DB database = mongoClient.getDB("TheDatabaseName");

        // Then
        Assert.assertThat(database, CoreMatchers.is(CoreMatchers.notNullValue()));
    }

    @Test
    public void shouldGetACollectionFromTheDatabase() throws Exception {
        // Given
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        DB database = mongoClient.getDB("TheDatabaseName");
        // When
        // TODO get collection
        DBCollection collection = database.getCollection("TheCollectionName");

        // Then
        Assert.assertThat(collection, CoreMatchers.is(CoreMatchers.notNullValue()));
    }

    @Test(expected = Exception.class)
    public void shouldNotBeAbleToUseMongoClientAfterItHasBeenClosed() throws UnknownHostException {
        // Given
        MongoClient mongoClient = new MongoClient();

        // When
        mongoClient.close();

        // Then
        mongoClient.getDB("SomeDatabase").getCollection("coll").insert(new BasicDBObject("field", "value"));
    }
}
