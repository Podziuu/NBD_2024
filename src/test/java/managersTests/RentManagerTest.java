package managersTests;

import com.datastax.oss.driver.api.core.CqlSession;
import config.CassandraConfig;
import managers.ClientManager;
import managers.ItemManager;
import managers.RentManager;
import models.ClientType;
import models.Item;
import models.MusicGenre;
import models.Rent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repos.ClientRepository;
import repos.ItemRepository;
import repos.RentRepository;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class RentManagerTest {

    private static RentManager rentManager;
    private static CassandraConfig cassandraConfig;
    private static CqlSession session;
    private static RentRepository rentRepository;
    private static ClientRepository clientRepository;
    private static ClientManager clientManager;
    private static ItemManager itemManager;
    private static ItemRepository itemRepository;

    @BeforeAll
    public static void setUp() throws Exception {
        cassandraConfig = new CassandraConfig();

        session = cassandraConfig.getSession();
        if (session == null) {
            throw new IllegalStateException("CqlSession jest null. Sprawdź inicjalizację w CassandraConfig.");
        }

        clientRepository = new ClientRepository(session);
        itemRepository = new ItemRepository(session);
        clientManager = new ClientManager(clientRepository);
        itemManager = new ItemManager(itemRepository);
        rentRepository = new RentRepository(session);
        rentManager = new RentManager(rentRepository, clientManager, itemManager);

        session.execute("TRUNCATE mediastore.rents");
    }


    @AfterEach
    public void tearDown() throws Exception {
        if (cassandraConfig != null) {
            cassandraConfig.close();
        }
    }

    @Test
    void rentItem_Success() {
        ClientType clientType = ClientType.createDiamondMembership();
        UUID clientId = clientManager.addClient("John", "Doe", 12345L, clientType);
        UUID itemId = itemManager.addMusic(100, "Jazz Album", MusicGenre.Jazz, true);
        UUID rentId = rentManager.rentItem(Instant.now(), clientId, itemId);
        Rent rent = rentManager.getRent(rentId);
        assertNotNull(rent);
        assertEquals(rent.getItemId(), itemId);
        assertEquals(rent.getClientId(), clientId);
    }
//
//    @Test
//    void rentItem_ExceedsMaxRentals() {
//        ClientType clientType = ClientType.createNoMembership();
//        UUID clientId = clientManager.addClient("John", "Doe", 1234L, clientType);
//        UUID itemId1 = itemManager.addMusic(100, "Jazz Album", MusicGenre.Jazz, false);
//        UUID itemId2 = itemManager.addMovie(100, "Movie", 90, true);
//        UUID itemId3 = itemManager.addComics(100, "Comics", 30);
//        UUID rentId = rentManager.rentItem(Instant.now(), clientId, itemId1);
//        UUID rentId2 = rentManager.rentItem(Instant.now(), clientId, itemId2);
//        assertThrows(IllegalArgumentException.class, () -> rentManager.rentItem(Instant.now(), clientId, itemId3));
//    }
//
//    @Test
//    void rentItem_ItemAlreadyRented() {
//        ClientType clientType = ClientType.createDiamondMembership();
//        UUID clientId = clientManager.addClient("John", "Doe", 1234L, clientType);
//        UUID itemId = itemManager.addMusic(100, "Jazz Album", MusicGenre.Jazz, true);
//        UUID rentId = rentManager.rentItem(Instant.now(), clientId, itemId);
//        assertThrows(IllegalStateException.class, () -> rentManager.rentItem(Instant.now(), clientId, itemId));
//    }
//
//    @Test
//    void returnItem_Success() {
//        ClientType clientType = ClientType.createDiamondMembership();
//        UUID clientId = clientManager.addClient("John", "Doe", 1234L, clientType);
//        UUID itemId = itemManager.addMusic(100, "Jazz Album", MusicGenre.Jazz, true);
//        UUID rentId = rentManager.rentItem(Instant.now(), clientId, itemId);
//        rentManager.returnItem(rentId);
//        Item item = itemManager.getItem(itemId);
//        assertTrue(item.isAvailable());
//    }
//
//    @Test
//    void returnItem_RentNotFound() {
//        UUID randomUUID = UUID.randomUUID();
//        assertThrows(IllegalArgumentException.class, () -> rentManager.returnItem(randomUUID));
//    }
//
//    @Test
//    void getRent_NotFound() {
//        UUID randomUUID = UUID.randomUUID();
//        assertThrows(IllegalArgumentException.class, () -> rentManager.getRent(randomUUID));
//    }
}
