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

public class Exercise7QueryOperatorsTest {

    private DB database;
    private DBCollection collection;

    @Test
    public void shouldReturnADBObjectWithAPhoneNumberLessThan1000000000() {
        // Given
        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), Arrays.asList(1, 74));
        collection.insert(PersonAdaptor.toDBObject(charlie));
        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 987654321), Arrays.asList(27464, 747854));
        collection.insert(PersonAdaptor.toDBObject(bob));

        // When
        DBObject query = new BasicDBObject("address.phone", new BasicDBObject("$lt", 1000000000));
        DBCursor results = collection.find(query);

        // Then
        Assert.assertThat(results.size(), CoreMatchers.is(1));
        Assert.assertThat(results.next().get("_id"), CoreMatchers.is(bob.getId()));
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
