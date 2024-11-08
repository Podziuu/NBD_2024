import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import models.ClientType;
import models.DiamondMembership;
import org.junit.jupiter.api.*;
import config.MongoEntity;
import models.Client;

public class clientTest {
    private static MongoEntity mongoEntity;
    private static MongoDatabase database;
    private static MongoCollection<Client> clientCollection;

    @BeforeAll
    static void setUp() {
        mongoEntity = new MongoEntity();
        database = mongoEntity.getDatabase();
        clientCollection = database.getCollection("clients", Client.class);
    }

    @AfterAll
    static void tearDown() throws Exception {
        mongoEntity.close();
    }

    @Test
    void test() {
        System.out.println("Test");
        ClientType clientType = new DiamondMembership();
        System.out.println();
        Client client = new Client(123456789, "Januszek", "Kowalski", clientType);
        clientCollection.insertOne(client);
    }
}
