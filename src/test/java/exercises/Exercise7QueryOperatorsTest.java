package exercises;

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
import static org.junit.Assert.assertThat;

public class Exercise7QueryOperatorsTest {

    private DB database;
    private DBCollection collection;

    @Test
    public void shouldReturnADBObjectWithAPhoneNumberLessThan1000000000() {
        // Given
        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890),
                asList(1, 74));
        collection.insert(PersonAdaptor.toDBObject(charlie));
        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 987654321),
                asList(27464, 747854));
        collection.insert(PersonAdaptor.toDBObject(bob));
        // When
        DBObject query = new BasicDBObject("address.phone", new BasicDBObject("$lt", 1000000000));
        DBCursor results = collection.find(query);
        // Then
        assertThat(results.size(), is(1));
        assertThat(results.next().get("_id"), is(bob.getId()));
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
