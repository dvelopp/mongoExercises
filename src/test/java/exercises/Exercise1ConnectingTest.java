package exercises;

import com.mongodb.MongoClient;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class Exercise1ConnectingTest {

    @Test
    public void shouldCreateANewMongoClientConnectedToLocalhost() throws Exception {
        MongoClient mongoClient = MongoUtils.getMongoClient();

        assertThat(mongoClient, is(notNullValue()));
    }

}