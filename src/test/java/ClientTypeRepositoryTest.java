import org.junit.jupiter.api.*;
import models.*;
import repos.*;

public class ClientTypeRepositoryTest {

    @Test
    void testCreateClientType() {
        ClientTypeRepository clientTypeRepository = new ClientTypeRepository();
        ClientType diamondMembership = new DiamondMembership();
        ClientType membership = new Membership();
        ClientType noMembership = new NoMembership();
        clientTypeRepository.create(diamondMembership);
        clientTypeRepository.create(membership);
        clientTypeRepository.create(noMembership);
    }
}
