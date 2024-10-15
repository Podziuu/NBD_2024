package managersTests;

import exceptions.ClientNotExistsException;
import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.*;
import managers.*;
import models.*;

public class ClientManagerTest {
    private ClientTypeManager clientTypeManager;
    private ClientManager clientManager;

    @BeforeEach
    void setUp() {
        clientTypeManager = new ClientTypeManager();
        clientManager = new ClientManager();
        clientTypeManager.createBasicClientTypes();
    }

    @Test
    void createClientTest() {
        long id = clientManager.createClient("John", "Kowalski", "DiamondMembership");
        Assertions.assertNotNull(clientManager.getClient(id));
    }

    @Test
    void createClientInvalidTypeTest() {
        Assertions.assertThrows(NoResultException.class, () -> clientManager.createClient("Joe", "Doe", "InvalidType"));
    }

    @Test
    void deleteClientTest() throws ClientNotExistsException {
        long id = clientManager.createClient("Robert", "Nowak", "Membership");
        clientManager.deleteClient(id);
        Assertions.assertNull(clientManager.getClient(id));
    }

    @Test
    void deleteClientNotExistsTest() {
        Assertions.assertThrows(ClientNotExistsException.class, () -> clientManager.deleteClient(55));
    }

    @Test
    void updateClient() throws ClientNotExistsException {
        long id = clientManager.createClient("Robert", "Nowak", "Membership");
        clientManager.updateClient(id, "Robert", "Kowalski", "DiamondMembership");
        Client client = clientManager.getClient(id);
        Assertions.assertEquals("Robert", client.getFirstName());
        Assertions.assertEquals("Kowalski", client.getLastName());
        Assertions.assertEquals("Diamond Membership: 15 articles, discount: 30%", client.getClientType().getClientTypeInfo());
    }

    @Test
    void updateClientNotExistsTest() {
        Assertions.assertThrows(ClientNotExistsException.class, () -> clientManager.updateClient(55, "Robert", "Kowalski", "DiamondMembership"));
    }

    @Test
    void archiveClientTest() throws ClientNotExistsException {
        long id = clientManager.createClient("Robert", "Nowak", "Membership");
        clientManager.archiveClient(id);
        Assertions.assertTrue(clientManager.getClient(id).isArchive());
    }

    @Test
    void archiveClientNotExistsTest() {
        Assertions.assertThrows(ClientNotExistsException.class, () -> clientManager.archiveClient(55));
    }

    @Test
    void unarchiveClientTest() throws ClientNotExistsException {
        long id = clientManager.createClient("Robert", "Nowak", "Membership");
        clientManager.archiveClient(id);
        clientManager.unarchiveClient(id);
        Assertions.assertFalse(clientManager.getClient(id).isArchive());
    }

    @Test
    void unarchiveClientNotExistsTest() {
        Assertions.assertThrows(ClientNotExistsException.class, () -> clientManager.unarchiveClient(55));
    }
}
