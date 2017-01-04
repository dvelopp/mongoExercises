import com.mongodb.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import person.Address;
import person.Person;
import person.PersonAdaptor;

import java.net.UnknownHostException;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class Exercise6SelectFieldsTest {

    private DB database;
    private DBCollection collection;

    @Test
    public void shouldFindAllDBObjectsWithTheNameCharlesAndOnlyReturnNameAndId() {
        // Given
        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890),
                asList(1, 74));
        collection.insert(PersonAdaptor.toDBObject(charlie));
        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890),
                asList(27464, 747854));
        collection.insert(PersonAdaptor.toDBObject(bob));
        // When
        DBObject query = new BasicDBObject("name", "Charles");
        DBCursor results = collection.find(query, new BasicDBObject("name", 1));
        // Then
        assertThat(results.size(), is(1));
        DBObject theOnlyResult = results.next();
        assertThat(theOnlyResult.get("_id"), is(charlie.getId()));
        assertThat(theOnlyResult.get("name"), is(charlie.getName()));
        assertThat(theOnlyResult.get("address"), is(nullValue()));
        assertThat(theOnlyResult.get("books"), is(nullValue()));
    }

    //BONUS
    @Test
    public void shouldFindAllDBObjectsWithTheNameCharlesAndExcludeAddressInReturn() {
        // Given
        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890),
                asList(1, 74));
        collection.insert(PersonAdaptor.toDBObject(charlie));
        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890),
                asList(27464, 747854));
        collection.insert(PersonAdaptor.toDBObject(bob));
        // When
        DBObject query = new BasicDBObject("name", "Charles");
        DBCursor results = collection.find(query, new BasicDBObject("address", 0));
        // Then
        assertThat(results.size(), is(1));
        DBObject theOnlyResult = results.next();
        assertThat(theOnlyResult.get("_id"), is(charlie.getId()));
        assertThat(theOnlyResult.get("name"), is(charlie.getName()));
        assertThat(theOnlyResult.get("address"), is(nullValue()));
        assertThat(theOnlyResult.get("books"), is(charlie.getBookIds()));
    }

    @Before
    public void setUp() throws UnknownHostException {
        MongoClient mongoClient = MongoUtils.getMongoClient();
        database = mongoClient.getDB("Examples");
        collection = database.getCollection("people");
    }

    @After
    public void tearDown() {
        database.dropDatabase();
    }

}
