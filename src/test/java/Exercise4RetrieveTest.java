import com.mongodb.*;
import org.hamcrest.CoreMatchers;
import org.junit.*;
import person.Address;
import person.Person;
import person.PersonAdaptor;

import java.net.UnknownHostException;
import java.util.Arrays;

public class Exercise4RetrieveTest {

    private DB database;
    private DBCollection collection;

    @Before
    public void setUp() throws UnknownHostException {
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        database = mongoClient.getDB("Examples");
        collection = database.getCollection("people");
    }

    @Test
    public void shouldRetrieveBobFromTheDatabaseWhenHeIsTheOnlyOneInThere() {
        // Given
        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), Arrays.asList(27464, 747854));
        collection.insert(PersonAdaptor.toDBObject(bob));

        // When
        DBObject query = new BasicDBObject("_id", "bob");
        DBCursor cursor = collection.find(query);
        DBObject result = cursor.one();

        // Then
        Assert.assertThat((String) result.get("_id"), CoreMatchers.is("bob"));
    }

    @Test
    public void shouldRetrieveEverythingFromTheDatabase() {
        // Given
        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), Arrays.asList(1, 74));
        collection.insert(PersonAdaptor.toDBObject(charlie));

        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), Arrays.asList(27464, 747854));
        collection.insert(PersonAdaptor.toDBObject(bob));

        // When
        DBObject query = new BasicDBObject();
        DBCursor cursor = collection.find(query);

        // Then
        Assert.assertThat(cursor.size(), CoreMatchers.is(2));
        // they should come back in the same order they were put in
        Assert.assertThat((String) cursor.next().get("_id"), CoreMatchers.is("charlie"));
        Assert.assertThat((String) cursor.next().get("_id"), CoreMatchers.is("bob"));
    }

    @Test
    public void shouldSearchForAndReturnOnlyBobFromTheDatabaseWhenMorePeopleExist() {
        // Given
        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), Arrays.asList(1, 74));
        collection.insert(PersonAdaptor.toDBObject(charlie));

        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), Arrays.asList(27464, 747854));
        collection.insert(PersonAdaptor.toDBObject(bob));

        // When
        DBObject query = new BasicDBObject("_id", "bob");
        DBCursor cursor = collection.find(query);

        // Then
        Assert.assertThat(cursor.count(), CoreMatchers.is(1));
        Assert.assertThat((String) cursor.one().get("name"), CoreMatchers.is("Bob The Amazing"));
    }

    @After
    public void tearDown() {
        database.dropDatabase();
    }

}
