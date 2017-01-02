import com.mongodb.*;
import org.hamcrest.CoreMatchers;
import org.junit.*;
import person.Address;
import person.Person;
import person.PersonAdaptor;

import java.net.UnknownHostException;
import java.util.Arrays;


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
        Assert.assertThat(bobAsDBObject.toString(), CoreMatchers.is(expectedDBObject));
    }

    @Test
    public void shouldBeAbleToSaveAPerson() throws UnknownHostException {
        // Given
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        DB database = mongoClient.getDB("Examples");
        DBCollection collection = database.getCollection("people");

        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890),
                Arrays.asList(1, 74));

        // When
        DBObject charlieAsDBObject = PersonAdaptor.toDBObject(charlie);
        collection.insert(charlieAsDBObject);

        // Then
        Assert.assertThat(collection.find().count(), CoreMatchers.is(1));

        // Clean up
        database.dropDatabase();
    }

}