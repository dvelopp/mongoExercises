import com.mongodb.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import person.Address;
import person.Person;
import person.PersonAdaptor;

import java.net.UnknownHostException;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class Exercise14RemoveTest {

    private DB database;
    private DBCollection collection;

    @Test
    public void shouldDeleteOnlyCharlieFromTheDatabase() {
        // Given
        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890),
                asList(27464, 747854));
        collection.insert(PersonAdaptor.toDBObject(bob));
        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890),
                asList(1, 74));
        collection.insert(PersonAdaptor.toDBObject(charlie));
        Person emily = new Person("emily", "Emily", new Address("5", "Some Town", 646383), emptyList());
        collection.insert(PersonAdaptor.toDBObject(emily));
        // When
        DBObject query = new BasicDBObject("_id", charlie.getId());
        WriteResult resultOfRemove = collection.remove(query);
        // Then
        assertThat(resultOfRemove.getN(), is(1));
        List<DBObject> remainingPeople = collection.find().toArray();
        assertThat(remainingPeople.size(), is(2));
        for (final DBObject remainingPerson : remainingPeople) {
            assertThat(remainingPerson.get("_id"), is(not(charlie.getId())));
        }
    }

    @Test
    public void shouldDeletePeopleWhoLiveInLondon() {
        // Given
        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890),
                asList(27464, 747854));
        collection.insert(PersonAdaptor.toDBObject(bob));
        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890),
                asList(1, 74));
        collection.insert(PersonAdaptor.toDBObject(charlie));
        Person emily = new Person("emily", "Emily", new Address("5", "Some Town", 646383), emptyList());
        collection.insert(PersonAdaptor.toDBObject(emily));
        // When
        DBObject query = new BasicDBObject("address.city", "LondonTown");
        WriteResult resultOfRemove = collection.remove(query);
        // Then
        assertThat(resultOfRemove.getN(), is(2));
        List<DBObject> remainingPeople = collection.find().toArray();
        assertThat(remainingPeople.size(), is(1));
        assertThat(remainingPeople.get(0).get("_id"), is(emily.getId()));
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
