package exercises;

import com.mongodb.*;
import org.junit.Test;

import java.net.UnknownHostException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;


public class Exercise2MongoClientTest {

    @Test
    public void shouldGetADatabaseFromTheMongoClient() throws Exception {
        // Given
        MongoClient mongoClient = MongoUtils.getMongoClient();
        // When
        DB database = mongoClient.getDB("TheDatabaseName");
        // Then
        assertThat(database, is(notNullValue()));
    }

    @Test
    public void shouldGetACollectionFromTheDatabase() throws Exception {
        // Given
        MongoClient mongoClient = MongoUtils.getMongoClient();
        DB database = mongoClient.getDB("TheDatabaseName");
        // When
        DBCollection collection = database.getCollection("TheCollectionName");
        // Then
        assertThat(collection, is(notNullValue()));
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
