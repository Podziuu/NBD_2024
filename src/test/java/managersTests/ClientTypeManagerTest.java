package managersTests;

import org.junit.jupiter.api.*;
import managers.*;
import models.*;

public class ClientTypeManagerTest {

    @Test
    public void createClientTypeTest() {
        ClientTypeManager clientTypeManager = new ClientTypeManager();
        ClientType clientType = new DiamondMembership();
        clientTypeManager.createClientType(clientType);
        Assertions.assertEquals(clientType.getClientTypeInfo(), clientTypeManager.getClientType(clientType.getId()).getClientTypeInfo());
    }

    @Test
    public void createBasicClientTypesTest() {
        ClientTypeManager clientTypeManager = new ClientTypeManager();
        clientTypeManager.createBasicClientTypes();
        Assertions.assertEquals("Diamond Membership: 15 articles, discount: 30%", clientTypeManager.getClientTypeByType("DiamondMembership").getClientTypeInfo());
        Assertions.assertEquals("Membership: 10 articles, discount: 20%", clientTypeManager.getClientTypeByType("Membership").getClientTypeInfo());
        Assertions.assertEquals("No Membership: 5 articles, no discount", clientTypeManager.getClientTypeByType("NoMembership").getClientTypeInfo());
    }

    @Test
    public void updateClientTypeTest() {
        ClientTypeManager clientTypeManager = new ClientTypeManager();
        ClientType clientType = new DiamondMembership();
        clientTypeManager.createClientType(clientType);
        clientType.setDiscount(40);
        clientTypeManager.updateClientType(clientType);
        Assertions.assertEquals(clientType.getClientTypeInfo(), clientTypeManager.getClientType(clientType.getId()).getClientTypeInfo());
    }

    @Test
    public void deleteClientTypeTest() {
        ClientTypeManager clientTypeManager = new ClientTypeManager();
        ClientType clientType = new DiamondMembership();
        clientTypeManager.createClientType(clientType);
        clientTypeManager.deleteClientType(clientType);
        Assertions.assertNull(clientTypeManager.getClientType(clientType.getId()));
    }
}
