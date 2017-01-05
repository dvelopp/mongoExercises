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
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class Exercise13UpsertTest {

    private DB database;
    private DBCollection collection;

    //Upsert
    @Test
    public void shouldOnlyInsertDBObjectIfItDidNotExistWhenUpsertIsTrue() {
        // Given
        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890),
                asList(27464, 747854));
        collection.insert(PersonAdaptor.toDBObject(bob));
        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890),
                asList(1, 74));
        collection.insert(PersonAdaptor.toDBObject(charlie));
        // new person not in the database yet
        Person claire = new Person("claire", "Claire", new Address("1", "Town", 836558493), emptyList());
        // When
        DBObject findClaire = new BasicDBObject("_id", claire.getId());
        WriteResult resultOfUpdate = collection.update(findClaire, new BasicDBObject("$set",
                new BasicDBObject("wasUpdated", true)));
        // Then
        assertThat(resultOfUpdate.getN(), is(0));
        // without upsert this should not have been inserted
        assertThat(collection.find(findClaire).count(), is(0));
        // When
        WriteResult resultOfUpsert = collection.update(findClaire, new BasicDBObject("$set",
                new BasicDBObject("wasUpdated", true)), true, false);
        // Then
        assertThat(resultOfUpsert.getN(), is(1));
        DBObject newClaireDBObject = collection.find(findClaire).toArray().get(0);
        // all values should have been updated to the new object values
        assertThat(newClaireDBObject.get("_id"), is(claire.getId()));
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
