import com.mongodb.*;
import org.hamcrest.CoreMatchers;
import org.junit.*;
import person.Address;
import person.Person;
import person.PersonAdaptor;

import java.net.UnknownHostException;
import java.util.Arrays;

public class Exercise5SimpleQueryTest {

    private DB database;
    private DBCollection collection;

    @Test
    public void shouldFindAllDBObjectsWithTheNameCharles() {
        // Given
        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), Arrays.asList(1, 74));
        collection.insert(PersonAdaptor.toDBObject(charlie));

        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), Arrays.asList(27464, 747854));
        collection.insert(PersonAdaptor.toDBObject(bob));

        // When
        DBObject query = new BasicDBObject("name", "Charles");
        DBCursor results = collection.find(query);

        // Then
        Assert.assertThat(results.size(), CoreMatchers.is(1));
        Assert.assertThat(results.next().get("_id"), CoreMatchers.is(charlie.getId()));
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
