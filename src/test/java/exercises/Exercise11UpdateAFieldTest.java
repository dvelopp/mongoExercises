package exercises;

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
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class Exercise11UpdateAFieldTest {

    private DB database;
    private DBCollection collection;

    @Test
    public void shouldUpdateCharliesAddress() {
        // Given
        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890),
                asList(27464, 747854));
        collection.insert(PersonAdaptor.toDBObject(bob));
        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890),
                asList(1, 74));
        collection.insert(PersonAdaptor.toDBObject(charlie));
        String charliesNewAddress = "987 The New Street";
        // When
        DBObject findCharlie = new BasicDBObject("_id", charlie.getId());
        WriteResult resultOfUpdate = collection.update(findCharlie, new BasicDBObject("$set",
                new BasicDBObject("address.street", charliesNewAddress)));
        // Then
        assertThat(resultOfUpdate.getN(), is(1));

        DBObject newCharlie = collection.find(findCharlie).toArray().get(0);
        // this stuff should all be the same
        assertThat(newCharlie.get("_id"), is(charlie.getId()));
        assertThat(newCharlie.get("name"), is(charlie.getName()));
        // the address street, and only the street, should have changed
        DBObject address = (DBObject) newCharlie.get("address");
        assertThat(address.get("street"), is(charliesNewAddress));
        assertThat(address.get("city"), is(charlie.getAddress().getTown()));
        assertThat(address.get("phone"), is(charlie.getAddress().getPhone()));
    }

    @Test
    public void shouldAddANewFieldToAnExistingDocument() {
        // Given
        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890),
                asList(27464, 747854));
        collection.insert(PersonAdaptor.toDBObject(bob));
        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890),
                asList(1, 74));
        collection.insert(PersonAdaptor.toDBObject(charlie));
        // When
        DBObject findCharlie = new BasicDBObject("_id", charlie.getId());
        WriteResult resultOfUpdate = collection.update(findCharlie, new BasicDBObject("$set",
                new BasicDBObject("newField", "A New Value")));
        // Then
        assertThat(resultOfUpdate.getN(), is(1));
        DBObject newCharlie = collection.find(findCharlie).toArray().get(0);
        // this stuff should all be the same
        assertThat(newCharlie.get("_id"), is(charlie.getId()));
        assertThat(newCharlie.get("name"), is(charlie.getName()));
        assertThat(newCharlie.get("newField"), is("A New Value"));
    }

    //BONUS
    @Test
    public void shouldAddAnotherBookToBobsBookIds() {
        // Given
        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890),
                asList(27464, 747854));
        collection.insert(PersonAdaptor.toDBObject(bob));
        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890),
                asList(1, 74));
        collection.insert(PersonAdaptor.toDBObject(charlie));
        // When
        DBObject findBob = new BasicDBObject("_id", bob.getId());
        collection.update(findBob, new BasicDBObject("$push", new BasicDBObject("books", 66)));
        // Then
        DBObject newBob = collection.find(findBob).toArray().get(0);
        assertThat(newBob.get("name"), is(bob.getName()));
        // there should be another item in the array
        List<Integer> bobsBooks = (List<Integer>) newBob.get("books");
        // note these are  ordered
        assertThat(bobsBooks.size(), is(3));
        assertThat(bobsBooks.get(0), is(27464));
        assertThat(bobsBooks.get(1), is(747854));
        assertThat(bobsBooks.get(2), is(66));
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
