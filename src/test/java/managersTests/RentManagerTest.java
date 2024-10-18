package managersTests;

import exceptions.ItemAvailableException;
import exceptions.LogicException;
import exceptions.ParameterException;
import managers.ClientManager;
import managers.ClientTypeManager;
import managers.ItemManager;
import managers.RentManager;
import models.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class RentManagerTest {
    private RentManager rentManager;
    private ItemManager itemManager;
    private ClientManager clientManager;

    @BeforeEach
    void setUp() {
        rentManager = new RentManager();
        clientManager = new ClientManager();
        itemManager = new ItemManager();
        ClientTypeManager clientTypeManager = new ClientTypeManager();
        clientTypeManager.createBasicClientTypes();
    }

    @Test
    public void testItemNotAvailableForRent() throws ParameterException, ItemAvailableException, LogicException {
        System.out.println("Starting test: testItemNotAvailableForRent");

        long idb = clientManager.createClient("Robercik", "Doe", "DiamondMembership");
        Client client = clientManager.getClient(idb);

        long idc = itemManager.registerMusic(150, "Pop Album", MusicGenre.POP, false);
        Item item = itemManager.getItem(idc);

        LocalDateTime beginTime = LocalDateTime.now();
        System.out.println("Renting item for the first time.");
        rentManager.rentItem(beginTime, client, item);

        System.out.println("Attempting to rent the same item again.");
        Assertions.assertThrows(ParameterException.class, () -> {
            rentManager.rentItem(beginTime, client, item);
        }, "Item should not be available after being rented.");

        System.out.println("Test finished: testItemNotAvailableForRent");
    }


    @Test
    public void testAddRent() throws ParameterException, ItemAvailableException, LogicException {
        System.out.println("TEST");
        long idC = clientManager.createClient("John", "Kowalski", "DiamondMembership");
        Client client = clientManager.getClient(idC);

        long idI = itemManager.registerMusic(100, "POP Album", MusicGenre.POP, true);
        Item item = itemManager.getItem(idI);

        Assertions.assertTrue(item.isAvailable(), "Item should be available before renting.");

        LocalDateTime beginTime = LocalDateTime.now();
        Rent rent = rentManager.rentItem(beginTime, client, item);

        Assertions.assertNotNull(rent, "Rent should not be null.");

        Assertions.assertEquals(client, rent.getClient());
        Assertions.assertEquals(item, rent.getItem());

        Item rentedItem = itemManager.getItem(idI);
        Assertions.assertFalse(rentedItem.isAvailable(), "Item should not be available after renting.");
    }

    @Test
    public void testMaxRentalsExceeded() throws ParameterException, ItemAvailableException, LogicException {
        long idCa = clientManager.createClient("John", "Kowalski", "NoMembership");
        Client client = clientManager.getClient(idCa);

        long idI = itemManager.registerMusic(100, "Jazz Album", MusicGenre.Jazz, true);
        Item item = itemManager.getItem(idI);
        long idI2 = itemManager.registerMusic(100, "Jazz Album", MusicGenre.Jazz, true);
        Item item2 = itemManager.getItem(idI2);
        long idI3 = itemManager.registerMusic(100, "Jazz Album", MusicGenre.Jazz, true);
        Item item3 = itemManager.getItem(idI3);
        long idI4 = itemManager.registerMusic(100, "Jazz Album", MusicGenre.Jazz, true);
        Item item4 = itemManager.getItem(idI4);
        long idI5 = itemManager.registerMusic(100, "Jazz Album", MusicGenre.Jazz, true);
        Item item5 = itemManager.getItem(idI5);
        long idI6 = itemManager.registerMusic(100, "Jazz Album", MusicGenre.Jazz, true);
        Item item6 = itemManager.getItem(idI6);

        LocalDateTime beginTime = LocalDateTime.now();
        rentManager.rentItem(beginTime, client, item);
        rentManager.rentItem(beginTime, client, item2);
        rentManager.rentItem(beginTime, client, item3);
        rentManager.rentItem(beginTime, client, item4);
        rentManager.rentItem(beginTime, client, item5);

        Assertions.assertThrows(ParameterException.class, () -> {
            rentManager.rentItem(beginTime, client, item6);
        }, "Client should not be able to rent more than the maximum number of rentals.");
    }

    @Test
    public void testEndRent() throws ParameterException, ItemAvailableException, LogicException {
        long idd = clientManager.createClient("John", "Kowalski", "DiamondMembership");
        Client client = clientManager.getClient(idd);

        long ide = itemManager.registerMusic(100, "Classical Album", MusicGenre.Classical, true);
        Item item = itemManager.getItem(ide);

        LocalDateTime beginTime = LocalDateTime.now();
        Rent rent = rentManager.rentItem(beginTime, client, item);

        Assertions.assertNotNull(rent, "Rent should not be null.");

        LocalDateTime endTime = LocalDateTime.now().plusDays(1);
        rentManager.endRent(rent.getId(), endTime);

        Item rentedItem = itemManager.getItem(ide);
        Assertions.assertTrue(rentedItem.isAvailable(), "Item should be available after rent ends.");
    }
}