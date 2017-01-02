import com.mongodb.*;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.UnknownHostException;

public class Exercise9IndexTest {

    private DB database;
    private DBCollection collection;

    @Test
    public void shouldCreateAnAscendingIndex() {
        // given
        collection.insert(new BasicDBObject("fieldToIndex", "Bob"));

        // when
        collection.createIndex(new BasicDBObject("fieldToIndex", 1));

        // then
        DBObject indexKey = (DBObject) collection.getIndexInfo().get(1).get("key");
        Assert.assertTrue(indexKey.keySet().contains("fieldToIndex"));
        Assert.assertThat(indexKey.get("fieldToIndex"), CoreMatchers.is(1));
        Assert.assertThat(collection.getIndexInfo().get(1).get("name"), CoreMatchers.is("fieldToIndex_1"));
    }

    @Before
    public void setUp() throws UnknownHostException {
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        database = mongoClient.getDB("Examples");
        collection = database.getCollection("people");
    }

    @After
    public void tearDown() {
    }

}
