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

import static org.junit.jupiter.api.Assertions.assertThrows;

public class RentManagerTest {
    private static RentRepository rentRepository;
    private static ClientRepository clientRepository;
    private static ItemRepository itemRepository;
    private static RentManager rentManager;
    private static ClientManager clientManager;
    private static ItemManager itemManager;

    @BeforeAll
    static void setUp() {
        rentRepository = new RentRepository();
        clientRepository = new ClientRepository();
        itemRepository = new ItemRepository();

        clientManager = new ClientManager(clientRepository);
        itemManager = new ItemManager(itemRepository);
        rentManager = new RentManager(rentRepository, clientManager, itemManager);
    }

    @Test
    void rentItemTest_ItemUnavailable() {
        ObjectId clientId = clientManager.addClient("Robert", "Kolonko", 123456789, "DiamondMembership");
        Client client = clientManager.getClient(clientId);
        ObjectId itemId = itemManager.addComics(100, "Comics", 100);
        Item item = itemManager.getItem(itemId);

        LocalDateTime beginTime = LocalDateTime.now();
        rentManager.rentItem(beginTime, client, item);

        Item item1 = itemManager.getItem(itemId);

        assertThrows(IllegalStateException.class, () -> {
            rentManager.rentItem(beginTime, client, item1);
        }, "Item should not be available after being rented.");
    }

    @Test
    void rentItemTest_Successful() {
        ObjectId clientId = clientManager.addClient("Jan", "Kowalski", 123456789, "DiamondMembership");
        Client client = clientManager.getClient(clientId);

        ObjectId itemId = itemManager.addMusic(100, "Album", MusicGenre.Metal, true);
        Item item = itemManager.getItem(itemId);

        LocalDateTime beginTime = LocalDateTime.now();
        ObjectId rentId = rentManager.rentItem(beginTime, client, item);

        Rent rent = rentManager.getRent(rentId);
        Assertions.assertNotNull(rent);
        Assertions.assertEquals(client.getId(), rent.getClient().getId());
        Assertions.assertEquals(item.getId(), rent.getItem().getId());

        Item rentedItem = itemManager.getItem(itemId);
        Assertions.assertFalse(rentedItem.isAvailable());
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

        assertThrows(IllegalArgumentException.class, () -> {
            rentManager.rentItem(LocalDateTime.now(), client1, item3);
        });
    }

    @Test
    void returnItemTest_SuccessfulReturn() {
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

    @Test
    void returnItemTest_RentNotFound () {
        ObjectId clientId = clientManager.addClient("Robert", "Kolonko", 123456789, "NoMembership");
        Client client = clientManager.getClient(clientId);

        ObjectId itemId = itemManager.addComics(100, "Comics1", 100);
        Item item = itemManager.getItem(itemId);
        LocalDateTime beginTime = LocalDateTime.now();
        ObjectId rentId = rentManager.rentItem(beginTime, client, item);

        rentManager.removeRent(rentId);

        assertThrows(RuntimeException.class, () -> {
            rentManager.returnItem(rentId);
        });
    }

    @Test
    void removeRentTest() {
        ObjectId clientId = clientManager.addClient("Robert", "Mazurek", 123456789, "NoMembership");
        Client client = clientManager.getClient(clientId);

        ObjectId itemId = itemManager.addMusic(111, "Kizo Pogo", MusicGenre.HipHop,true);
        Item item = itemManager.getItem(itemId);

        LocalDateTime beginTime = LocalDateTime.now();
        ObjectId rentId = rentManager.rentItem(beginTime, client, item);

        rentManager.removeRent(rentId);

        Assertions.assertNull(rentManager.getRent(rentId));
    }

    @Test
    void removeRentTest_RentNotFound() {
        ObjectId clientId = clientManager.addClient("Robert", "Mazurek", 123456789, "NoMembership");
        Client client = clientManager.getClient(clientId);

        ObjectId itemId = itemManager.addMusic(111, "Kizo Pogo", MusicGenre.HipHop,true);
        Item item = itemManager.getItem(itemId);

        LocalDateTime beginTime = LocalDateTime.now();
        ObjectId rentId = rentManager.rentItem(beginTime, client, item);

        rentManager.removeRent(rentId);

        assertThrows(RuntimeException.class, () -> {
            rentManager.removeRent(rentId);
        });

        Assertions.assertNull(rentManager.getRent(rentId));
    }

    @Test
    void getRentTest() {
        ObjectId clientId = clientManager.addClient("Robert", "Mazurek", 123456789, "NoMembership");
        Client client = clientManager.getClient(clientId);

        ObjectId itemId = itemManager.addMusic(111, "Kizo Pogo", MusicGenre.HipHop,true);
        Item item = itemManager.getItem(itemId);

        LocalDateTime beginTime = LocalDateTime.now();
        ObjectId rentId = rentManager.rentItem(beginTime, client, item);

        Assertions.assertNotNull(rentManager.getRent(rentId));
        Assertions.assertEquals(client.getId(), rentManager.getRent(rentId).getClient().getId());
        Assertions.assertEquals(item.getId(), rentManager.getRent(rentId).getItem().getId());
    }
}