import com.mongodb.*;
import org.junit.Test;
import person.Address;
import person.Person;
import person.PersonAdaptor;

import java.net.UnknownHostException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class Exercise3InsertTest {

    @Test
    public void shouldTurnAPersonIntoADBObject() {
        // Given
        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890),
                Arrays.asList(27464, 747854));
        // When
        DBObject bobAsDBObject = PersonAdaptor.toDBObject(bob);
        // Then
        String expectedDBObject = "{" +
                " \"_id\" : \"bob\" ," +
                " \"name\" : \"Bob The Amazing\" ," +
                " \"address\" : {" +
                " \"street\" : \"123 Fake St\" ," +
                " \"city\" : \"LondonTown\" ," +
                " \"phone\" : 1234567890" +
                "} ," +
                " \"books\" : [ 27464 , 747854]" +
                "}";
        assertThat(bobAsDBObject.toString(), is(expectedDBObject));
    }

    @Test
    public void shouldBeAbleToSaveAPerson() throws UnknownHostException {
        // Given
        MongoClient mongoClient = MongoUtils.getMongoClient();
        DB database = mongoClient.getDB("Examples");
        DBCollection collection = database.getCollection("people");
        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890),
                Arrays.asList(1, 74));
        // When
        DBObject charlieAsDBObject = PersonAdaptor.toDBObject(charlie);
        collection.insert(charlieAsDBObject);
        // Then
        assertThat(collection.find().count(), is(1));
        // Clean up
        database.dropDatabase();
    }

}
