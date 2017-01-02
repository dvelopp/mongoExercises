import com.mongodb.*;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import person.Address;
import person.Person;
import person.PersonAdaptor;

import java.net.UnknownHostException;
import java.util.Arrays;

public class Exercise6SelectFieldsTest {

    private DB database;
    private DBCollection collection;

    @Test
    public void shouldFindAllDBObjectsWithTheNameCharlesAndOnlyReturnNameAndId() {
        // Given
        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), Arrays.asList(1, 74));
        collection.insert(PersonAdaptor.toDBObject(charlie));

        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), Arrays.asList(27464, 747854));
        collection.insert(PersonAdaptor.toDBObject(bob));

        // When
        DBObject query = new BasicDBObject("name", "Charles");
        DBCursor results = collection.find(query, new BasicDBObject("name", 1));

        // Then
        Assert.assertThat(results.size(), CoreMatchers.is(1));
        DBObject theOnlyResult = results.next();
        Assert.assertThat(theOnlyResult.get("_id"), CoreMatchers.is(charlie.getId()));
        Assert.assertThat(theOnlyResult.get("name"), CoreMatchers.is(charlie.getName()));
        Assert.assertThat(theOnlyResult.get("address"), CoreMatchers.is(CoreMatchers.nullValue()));
        Assert.assertThat(theOnlyResult.get("books"), CoreMatchers.is(CoreMatchers.nullValue()));
    }

    //BONUS
    @Test
    public void shouldFindAllDBObjectsWithTheNameCharlesAndExcludeAddressInReturn() {
        // Given
        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), Arrays.asList(1, 74));
        collection.insert(PersonAdaptor.toDBObject(charlie));

        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), Arrays.asList(27464, 747854));
        collection.insert(PersonAdaptor.toDBObject(bob));

        // When
        DBObject query = new BasicDBObject("name", "Charles");
        DBCursor results = collection.find(query, new BasicDBObject("address", 0));

        // Then
        Assert.assertThat(results.size(), CoreMatchers.is(1));
        DBObject theOnlyResult = results.next();
        Assert.assertThat(theOnlyResult.get("_id"), CoreMatchers.is(charlie.getId()));
        Assert.assertThat(theOnlyResult.get("name"), CoreMatchers.is(charlie.getName()));
        Assert.assertThat(theOnlyResult.get("address"), CoreMatchers.is(CoreMatchers.nullValue()));
        Assert.assertThat(theOnlyResult.get("books"), CoreMatchers.is(charlie.getBookIds()));
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
