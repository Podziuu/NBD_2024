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
        clientManager.createClient("John", "Kowalski", "DiamondMembership");
        Assertions.assertNotNull(clientManager.getClient(1));
    }

    @Test
    void createClientInvalidTypeTest() {
        Assertions.assertThrows(NoResultException.class, () -> clientManager.createClient("Joe", "Doe", "InvalidType"));
    }

    @Test
    void deleteClientTest() throws ClientNotExistsException {
        clientManager.createClient("Robert", "Nowak", "Membership");
        clientManager.deleteClient(1);
        Assertions.assertNull(clientManager.getClient(1));
    }

    @Test
    void deleteClientNotExistsTest() {
        Assertions.assertThrows(ClientNotExistsException.class, () -> clientManager.deleteClient(55));
    }

    @Test
    void updateClient() throws ClientNotExistsException {
        clientManager.createClient("Robert", "Nowak", "Membership");
        clientManager.updateClient(1, "Robert", "Kowalski", "DiamondMembership");
        Client client = clientManager.getClient(1);
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
        clientManager.createClient("Robert", "Nowak", "Membership");
        clientManager.archiveClient(1);
        Assertions.assertTrue(clientManager.getClient(1).isArchive());
    }

    @Test
    void archiveClientNotExistsTest() {
        Assertions.assertThrows(ClientNotExistsException.class, () -> clientManager.archiveClient(55));
    }

    @Test
    void unarchiveClientTest() throws ClientNotExistsException {
        clientManager.createClient("Robert", "Nowak", "Membership");
        clientManager.archiveClient(1);
        clientManager.unarchiveClient(1);
        Assertions.assertFalse(clientManager.getClient(1).isArchive());
    }

    @Test
    void unarchiveClientNotExistsTest() {
        Assertions.assertThrows(ClientNotExistsException.class, () -> clientManager.unarchiveClient(55));
    }
}
