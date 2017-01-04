import com.mongodb.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.UnknownHostException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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
        assertTrue(indexKey.keySet().contains("fieldToIndex"));
        assertThat(indexKey.get("fieldToIndex"), is(1));
        assertThat(collection.getIndexInfo().get(1).get("name"), is("fieldToIndex_1"));
    }

    @Before
    public void setUp() throws UnknownHostException {
        MongoClient mongoClient = MongoUtils.getMongoClient();
        database = mongoClient.getDB("Examples");
        collection = database.getCollection("people");
    }

    @After
    public void tearDown() {
    }

}
