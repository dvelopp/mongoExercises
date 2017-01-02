import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.hamcrest.CoreMatchers;
import org.junit.*;

public class Exercise1ConnectingTest {
    @Test
    public void shouldCreateANewMongoClientConnectedToLocalhost() throws Exception {
        // When
        // TODO: get/create the MongoClient
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));

        // Then
        Assert.assertThat(mongoClient, CoreMatchers.is(CoreMatchers.notNullValue()));
    }
}