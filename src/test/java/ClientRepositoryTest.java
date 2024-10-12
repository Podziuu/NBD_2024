import org.junit.jupiter.api.*;
import models.*;
import repos.*;

public class ClientRepositoryTest {

    @Test
    void testCreateClient() {
        ClientRepository clientRepository = new ClientRepository();
        ClientTypeRepository clientTypeRepository = new ClientTypeRepository();
        clientTypeRepository.create(new DiamondMembership());
        ClientType clientType = clientTypeRepository.get(1);
        System.out.println(clientType);
        Client client = new Client("12345678901", "Jan", "Kowalski", clientType);
        clientRepository.create(client);
    }
}
