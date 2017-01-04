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
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class Exercise12UpdateMultipleDocumentsTest {

    private DB database;
    private DBCollection collection;

    //Multi=false
    @Test
    public void shouldOnlyUpdateTheFirstDBObjectMatchingTheQuery() {
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
        DBObject findLondoners = new BasicDBObject("address.city", "LondonTown");
        assertThat(collection.find(findLondoners).count(), is(2));
        collection.update(findLondoners, new BasicDBObject("$set", new BasicDBObject("wasUpdated", true)));
        // Then
        List<DBObject> londoners = collection.find(findLondoners).sort(new BasicDBObject("_id", 1)).toArray();
        assertThat(londoners.size(), is(2));
        assertThat(londoners.get(0).get("name"), is(bob.getName()));
        assertThat(londoners.get(0).get("wasUpdated"), is(true));
        assertThat(londoners.get(1).get("name"), is(charlie.getName()));
        assertThat(londoners.get(1).get("wasUpdated"), is(nullValue()));
    }

    //Multi=true
    @Test
    public void shouldUpdateEveryoneLivingInLondon() {
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
        DBObject findLondoners = new BasicDBObject("address.city", "LondonTown");
        assertThat(collection.find(findLondoners).count(), is(2));
        collection.update(findLondoners, new BasicDBObject("$set", new BasicDBObject("wasUpdated", true)), false, true);
        // Then
        List<DBObject> londoners = collection.find(findLondoners).sort(new BasicDBObject("_id", 1)).toArray();
        assertThat(londoners.size(), is(2));
        DBObject firstLondoner = londoners.get(0);
        assertThat(firstLondoner.get("name"), is(bob.getName()));
        assertThat(firstLondoner.get("wasUpdated"), is(true));
        DBObject secondLondoner = londoners.get(1);
        assertThat(secondLondoner.get("name"), is(charlie.getName()));
        assertThat(secondLondoner.get("wasUpdated"), is(true));
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
