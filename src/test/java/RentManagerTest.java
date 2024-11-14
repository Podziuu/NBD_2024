/*import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import config.MongoEntity;
import managers.RentManager;
import managers.ClientManager;
import managers.ItemManager;
import models.*;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repos.RentRepository;
import repos.ClientRepository;
import repos.ItemRepository;

import java.time.LocalDateTime;

public class RentManagerTest {
    private static MongoEntity mongoEntity;
    private static MongoDatabase database;
    private static MongoCollection<Rent> rentCollection;
    private static MongoCollection<Client> clientCollection;
    private static MongoCollection<Item> itemCollection;
    private static RentRepository rentRepository;
    private static ClientRepository clientRepository;
    private static ItemRepository itemRepository;
    private static RentManager rentManager;
    private static ClientManager clientManager;
    private static ItemManager itemManager;

    @BeforeAll
    static void setUp() {
        mongoEntity = new MongoEntity();
        database = mongoEntity.getDatabase();
        rentCollection = database.getCollection("rents", Rent.class);
        clientCollection = database.getCollection("clients", Client.class);
        itemCollection = database.getCollection("items", Item.class);

        rentRepository = new RentRepository(rentCollection);
        clientRepository = new ClientRepository(clientCollection);
        itemRepository = new ItemRepository(itemCollection);

        clientManager = new ClientManager(clientRepository);
        itemManager = new ItemManager(itemRepository);
        rentManager = new RentManager(rentRepository, clientManager, itemManager);
    }

    @AfterAll
    static void tearDown() throws Exception {
        mongoEntity.close();
    }

    @Test
    void rentItemTest() {
        ClientType clientType = new DiamondMembership();
        Client client = new Client(123456789, "Jan", "Kowalski", clientType);
        clientRepository.addClient(client);

        ItemType itemType = new Music();
        Item item = new Item(12, "Kizo", itemType);
        itemRepository.addItem(item);

        LocalDateTime beginTime = LocalDateTime.now();
        rentManager.rentItem(beginTime, client, item);

        Rent rent = rentRepository.getRent(item.getId());
        Assertions.assertNotNull(rent);
        Assertions.assertEquals(client.getId(), rent.getClient().getId());
        Assertions.assertEquals(item.getId(), rent.getItem().getId());
    }

    @Test
    void rentItemMaxRentExceededTest() {
        ClientType clientType = new NoMembership();
        Client client = new Client(123456789, "Jan", "Kowalski", clientType);
        clientRepository.addClient(client);

        ItemType itemType1 = new Music();
        Item item1 = new Item(12, "Kizo", itemType1);

        ItemType itemType2 = new Movie();
        Item item2 = new Item(34, "Szergowiec Ryan", itemType2);


        itemRepository.addItem(item1);
        itemRepository.addItem(item2);

        rentManager.rentItem(LocalDateTime.now(), client, item1);
        rentManager.rentItem(LocalDateTime.now(), client, item2);

        ItemType itemType3 = new Comics();
        Item item1 = new Item(12, "Kizo", itemType1);
        itemRepository.addItem(item3);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            rentManager.rentItem(LocalDateTime.now(), client, item3);
        });
    }

    @Test
    void returnItemTest() {
        ClientType clientType = new DiamondMembership();
        Client client = new Client(123456789, "Jan", "Kowalski", clientType);
        clientRepository.addClient(client);

        ItemType itemType = new Music();
        Item item = new Item(12, "Kizo", itemType);
        itemRepository.addItem(item);

        LocalDateTime beginTime = LocalDateTime.now();
        rentManager.rentItem(beginTime, client, item);
        Rent rent = rentRepository.getRent(item.getId());
        rentManager.returnItem(rent.getId(), item);

        rent = rentRepository.getRent(rent.getId());
        Assertions.assertTrue(rent.isArchive());
        Assertions.assertNotNull(rent.getEndTime());
    }

    @Test
    void removeRentTest() {
        ClientType clientType = new DiamondMembership();
        Client client = new Client(123456789, "Jan", "Kowalski", clientType);
        clientRepository.addClient(client);

        ItemType itemType = new Music();
        Item item = new Item(12, "Kizo", itemType);
        itemRepository.addItem(item);

        LocalDateTime beginTime = LocalDateTime.now();
        rentManager.rentItem(beginTime, client, item);

        Rent rent = rentRepository.getRent(item.getId());

        rentManager.removeRent(rent.getId());

        Assertions.assertNull(rentRepository.getRent(rent.getId()));
    }

    @Test
    void getRentTest() {
        ClientType clientType = new DiamondMembership();
        Client client = new Client(123456789, "Jan", "Kowalski", clientType);
        clientRepository.addClient(client);

        ItemType itemType = new Music();
        Item item = new Item(12, "Kizo", itemType);
        itemRepository.addItem(item);

        LocalDateTime beginTime = LocalDateTime.now();
        rentManager.rentItem(beginTime, client, item);

        Rent rent = rentRepository.getRent(item.getId());
        Assertions.assertNotNull(rent);
        Assertions.assertEquals(client.getId(), rent.getClient().getId());
        Assertions.assertEquals(item.getId(), rent.getItem().getId());
    }
}*/