package managersTests;

import managers.ClientManager;
import models.Client;
import models.ClientType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.datastax.oss.driver.api.core.CqlSession;
import config.CassandraConfig;
import repos.ClientRepository;

import java.util.UUID;

public class ClientManagerTest {

    private ClientManager clientManager;
    private CassandraConfig cassandraConfig;
    private CqlSession session;
    private ClientRepository clientRepository;

    @BeforeEach
    public void setUp() throws Exception {
        cassandraConfig = new CassandraConfig();

        session = cassandraConfig.getSession();
        if (session == null) {
            throw new IllegalStateException("CqlSession jest null. Sprawdź inicjalizację w CassandraConfig.");
        }

        clientRepository = new ClientRepository(session);
        clientManager = new ClientManager(clientRepository);

        session.execute("TRUNCATE mediastore.clients");
    }


//    @AfterEach
//    public void tearDown() throws Exception {
//        if (cassandraConfig != null) {
//            cassandraConfig.close();
//        }
//    }

    @Test
    public void testAddClient() {
        String firstName = "John";
        String lastName = "Doe";
        long personalId = 123456789L;
        ClientType clientType = ClientType.createDiamondMembership();
        UUID clientId = clientManager.addClient(firstName, lastName, personalId, clientType);
        Client client = clientRepository.getClient(clientId);
        assertNotNull(client);
        assertEquals(firstName, client.getFirstName());
        assertEquals(lastName, client.getLastName());
        assertEquals(personalId, client.getPersonalId());
        assertEquals(clientType, client.getClientType());
    }


    @Test
    public void testUpdateClient() {
        UUID clientId = clientManager.addClient("John", "Doe", 123456789L, ClientType.createDiamondMembership());
        clientManager.updateClient(clientId, "Jane", "Smith", ClientType.createNoMembership());
        Client updatedClient = clientRepository.getClient(clientId);
        assertNotNull(updatedClient);
        assertEquals("Jane", updatedClient.getFirstName());
        assertEquals("Smith", updatedClient.getLastName());
        assertEquals(ClientType.createNoMembership(), updatedClient.getClientType());
    }

    @Test
    public void testRemoveClient() {
        UUID clientId = clientManager.addClient("John", "Doe", 123456789L, ClientType.createDiamondMembership());
        clientManager.removeClient(clientId);
        assertNull(clientRepository.getClient(clientId));
    }
}