import com.mongodb.*;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.UnknownHostException;

public class Exercise8SkipAndLimitTest {

    private DB database;
    private DBCollection collection;

    @Test
    public void shouldReturnDBObjects3to9Of20DBObjectsUsingSkipAndLimit() {
        // Given
        for (int i = 0; i < 20; i++) {
            collection.insert(new BasicDBObject("name", "person" + i).append("someIntValue", i));
        }

        // When
        DBCursor results = collection.find().skip(3).limit(7);

        // Then
        Assert.assertThat(results.size(), CoreMatchers.is(7));
        Assert.assertThat(results.next().get("someIntValue"), CoreMatchers.is(3));
        Assert.assertThat(results.next().get("someIntValue"), CoreMatchers.is(4));
        Assert.assertThat(results.next().get("someIntValue"), CoreMatchers.is(5));
        Assert.assertThat(results.next().get("someIntValue"), CoreMatchers.is(6));
        Assert.assertThat(results.next().get("someIntValue"), CoreMatchers.is(7));
        Assert.assertThat(results.next().get("someIntValue"), CoreMatchers.is(8));
        Assert.assertThat(results.next().get("someIntValue"), CoreMatchers.is(9));
    }

    @Before
    public void setUp() throws UnknownHostException {
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        database = mongoClient.getDB("Examples");
        collection = database.getCollection("people");
    }

    @After
    public void tearDown() {
        database.dropDatabase();
    }

}
