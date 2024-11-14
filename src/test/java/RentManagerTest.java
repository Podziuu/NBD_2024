import com.mongodb.client.MongoCollection;
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
    void testItemNotAvailableForRent() {
        ObjectId clientId = clientManager.addClient("Robert", "Kolonko", 123456789, "DiamondMembership");
        Client client = clientManager.getClient(clientId);
        ObjectId itemId = itemManager.addComics(100, "Comics", 100);
        Item item = itemManager.getItem(itemId);

        LocalDateTime beginTime = LocalDateTime.now();
        rentManager.rentItem(beginTime, client, item);

        Item item1 = itemManager.getItem(itemId);

        Assertions.assertThrows(IllegalStateException.class, () -> {
            rentManager.rentItem(beginTime, client, item1);
        }, "Item should not be available after being rented.");
    }

    @Test
    void rentItemTest() {
        ObjectId clientId = clientManager.addClient("Jan", "Kowalski", 123456789, "DiamondMembership");
        Client client = clientManager.getClient(clientId);

        ObjectId itemId = itemManager.addMusic(100, "Album", MusicGenre.Metal, true);
        Item item = itemManager.getItem(itemId);

        LocalDateTime beginTime = LocalDateTime.now();
        ObjectId rentId = rentManager.rentItem(beginTime, client, item);

        Rent rent = rentManager.getRent(rentId);
        Assertions.assertNotNull(rent, "Wypożyczenie nie zostało zapisane poprawnie");
        Assertions.assertEquals(client.getId(), rent.getClient().getId(), "ID klienta w wypożyczeniu jest niepoprawne");
        Assertions.assertEquals(item.getId(), rent.getItem().getId(), "ID przedmiotu w wypożyczeniu jest niepoprawne");

        Item rentedItem = itemManager.getItem(itemId);
        Assertions.assertFalse(rentedItem.isAvailable(), "Przedmiot powinien być oznaczony jako niedostępny");
    }

    @Test
    void rentItemMaxRentExceededTest() {
        ObjectId clientId = clientManager.addClient("Robert", "Kolonko", 123456789, "NoMembership");
        Client client = clientManager.getClient(clientId);

        ObjectId itemId = itemManager.addComics(100, "Comics1", 100);
        Item item1 = itemManager.getItem(itemId);

        ObjectId itemId2 = itemManager.addComics(100, "Comics2", 100);
        Item item2 = itemManager.getItem(itemId2);

        LocalDateTime beginTime = LocalDateTime.now();
        rentManager.rentItem(beginTime, client, item1);
        rentManager.rentItem(beginTime, client, item2);

        Client client1 = clientManager.getClient(clientId);

        ObjectId itemId3 = itemManager.addComics(100, "Comics3", 100);
        Item item3 = itemManager.getItem(itemId3);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            rentManager.rentItem(LocalDateTime.now(), client1, item3);
        });
    }

    @Test
    void returnItemTest() {
        ObjectId clientId = clientManager.addClient("Robert", "Kolonko", 123456789, "NoMembership");
        Client client = clientManager.getClient(clientId);

        ObjectId itemId = itemManager.addComics(100, "Comics1", 100);
        Item item = itemManager.getItem(itemId);
        LocalDateTime beginTime = LocalDateTime.now();
        ObjectId rentId = rentManager.rentItem(beginTime, client, item);
        rentManager.returnItem(rentId);

        Rent rent = rentManager.getRent(rentId);
        Assertions.assertTrue(rent.isArchive());
        Assertions.assertTrue(item.isAvailable());
        System.out.println(rent.getEndTime());
        Assertions.assertNotNull(rent.getEndTime());
    }
//
//    @Test
//    void removeRentTest() {
//        ClientType clientType = new DiamondMembership();
//        Client client = new Client(123456789, "Jan", "Kowalski", clientType);
//        clientRepository.addClient(client);
//
//        ItemType itemType = new Music();
//        Item item = new Item(12, "Kizo", itemType);
//        itemRepository.addItem(item);
//
//        LocalDateTime beginTime = LocalDateTime.now();
//        rentManager.rentItem(beginTime, client, item);
//
//        Rent rent = rentRepository.getRent(item.getId());
//
//        rentManager.removeRent(rent.getId());
//
//        Assertions.assertNull(rentRepository.getRent(rent.getId()));
//    }
//
//    @Test
//    void getRentTest() {
//        ClientType clientType = new DiamondMembership();
//        Client client = new Client(123456789, "Jan", "Kowalski", clientType);
//        clientRepository.addClient(client);
//
//        ItemType itemType = new Music();
//        Item item = new Item(12, "Kizo", itemType);
//        itemRepository.addItem(item);
//
//        LocalDateTime beginTime = LocalDateTime.now();
//        rentManager.rentItem(beginTime, client, item);
//
//        Rent rent = rentRepository.getRent(item.getId());
//        Assertions.assertNotNull(rent);
//        Assertions.assertEquals(client.getId(), rent.getClient().getId());
//        Assertions.assertEquals(item.getId(), rent.getItem().getId());
//    }
}