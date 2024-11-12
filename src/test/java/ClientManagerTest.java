import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import config.MongoEntity;
import managers.ClientManager;
import models.Client;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repos.ClientRepository;

public class ClientManagerTest {
    private static MongoEntity mongoEntity;
    private static MongoDatabase database;
    private static MongoCollection<Client> clientCollection;
    private static ClientRepository clientRepository;
    private static ClientManager clientManager;

    @BeforeAll
    static void setUp() {
        mongoEntity = new MongoEntity();
        database = mongoEntity.getDatabase();
        clientCollection = database.getCollection("clients", Client.class);
        clientRepository = new ClientRepository(clientCollection);
        clientManager = new ClientManager(clientRepository);
    }

    @AfterAll
    static void tearDown() throws Exception {
        mongoEntity.close();
    }

    @Test
    void createClientTest() {
        ObjectId id = clientManager.addClient("Jan", "Kowalski", 123456789, "DiamondMembership");
        Assertions.assertNotNull(clientManager.getClient(id));
    }

    @Test
    void createClientInvalidTypeTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            clientManager.addClient("Jan", "Kowalski", 123456789, "InvalidType");
        });
    }

    @Test
    void deleteClientTest() {
        ObjectId id = clientManager.addClient("Jan", "Kowalski", 123456789, "DiamondMembership");
        clientManager.removeClient(id);
        Assertions.assertNull(clientManager.getClient(id));
    }

    @Test
    void deleteClientInvalidIdTest() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            clientManager.removeClient(new ObjectId());
        });
    }

    @Test
    void updateClientTest() {
        ObjectId id = clientManager.addClient("Jan", "Kowalski", 123456789, "DiamondMembership");
        clientManager.updateClient(id, "Janusz", "Nowak", "NoMembership");
        Client client = clientManager.getClient(id);
        System.out.println(client.getClientType().getClass());
        Assertions.assertEquals("Janusz", client.getFirstName());
        Assertions.assertEquals("Nowak", client.getLastName());
        Assertions.assertEquals(0, client.getClientType().getDiscount());
    }

    @Test
    void updateClientInvalidIdTest() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            clientManager.updateClient(new ObjectId(), "Janusz", "Kowalski", "NoMembership");
        });
    }

    @Test
    void archiveClientTest() {
        ObjectId id = clientManager.addClient("Jan", "Kowalski", 123456789, "DiamondMembership");
        clientManager.archiveClient(id);
        Assertions.assertTrue(clientManager.getClient(id).isArchive());
    }

    @Test
    void archiveClientInvalidIdTest() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            clientManager.archiveClient(new ObjectId());
        });
    }

    @Test
    void unarchiveClientTest() {
        ObjectId id = clientManager.addClient("Janek", "Kowalski", 123456789, "DiamondMembership");
        clientManager.archiveClient(id);
        clientManager.unarchiveClient(id);
        Assertions.assertFalse(clientManager.getClient(id).isArchive());
    }

    @Test
    void unarchiveClientInvalidIdTest() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            clientManager.unarchiveClient(new ObjectId());
        });
    }
}
