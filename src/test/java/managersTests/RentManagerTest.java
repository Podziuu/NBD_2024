package managersTests;

import exceptions.ItemAvailableException;
import exceptions.LogicException;
import exceptions.ParameterException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import managers.ClientManager;
import managers.ClientTypeManager;
import managers.ItemManager;
import managers.RentManager;
import models.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class RentManagerTest {
    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private RentManager rentManager;
    private ItemManager itemManager;
    private ClientManager clientManager;


    @BeforeAll
    static void setUpFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("default");
    }

    @BeforeEach
    void setUp() {
        entityManager = entityManagerFactory.createEntityManager();

        rentManager = new RentManager();
        clientManager = new ClientManager();
        itemManager = new ItemManager();


        ClientTypeManager clientTypeManager = new ClientTypeManager();
        clientTypeManager.createBasicClientTypes();

        clearDatabase();
    }

    private void clearDatabase() {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            try {
                System.out.println("Clearing Rent, Item, and Client tables.");
                em.createQuery("DELETE FROM Rent").executeUpdate();
                em.createQuery("DELETE FROM Item").executeUpdate();
                em.createQuery("DELETE FROM Client").executeUpdate();
                transaction.commit();
                System.out.println("Tables cleared successfully.");
            } catch (Exception e) {
                System.err.println("Error clearing database: " + e.getMessage());
                if (transaction.isActive()) {
                    transaction.rollback();
                }
            }
        } catch (Exception e) {
            System.err.println("Error creating EntityManager: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }

        entityManager.close();
    }

    @AfterAll
    static void closeFactory() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @Test
    public void testAddRent() throws ParameterException, ItemAvailableException, LogicException {
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

        Item rentedItem = entityManager.find(Item.class, idI);
        Assertions.assertFalse(rentedItem.isAvailable(), "Item should not be available after renting.");
    }

    @Test
    public void testMaxRentalsExceeded() throws ParameterException, ItemAvailableException, LogicException {
        long idCa = clientManager.createClient("John", "Kowalski", "DiamondMembership");
        Client client = clientManager.getClient(idCa);

        long idI = itemManager.registerMusic(100, "Jazz Album", MusicGenre.Jazz, true);
        Item item = itemManager.getItem(idI);

        LocalDateTime beginTime = LocalDateTime.now();
        rentManager.rentItem(beginTime, client, item);

        Assertions.assertThrows(ParameterException.class, () -> {
            rentManager.rentItem(beginTime, client, item);
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

        Item rentedItem = entityManager.find(Item.class, ide);
        Assertions.assertTrue(rentedItem.isAvailable(), "Item should be available after rent ends.");
    }

    @Test
    public void testItemNotAvailableForRent() throws ParameterException, ItemAvailableException, LogicException {
        long idb = clientManager.createClient("John", "Kowalski", "DiamondMembership");
        Client client = clientManager.getClient(idb);

        long idc = itemManager.registerMusic(100, "Rock Album", MusicGenre.POP, true);
        Item item = itemManager.getItem(idc);

        LocalDateTime beginTime = LocalDateTime.now();
        rentManager.rentItem(beginTime, client, item);

        Assertions.assertThrows(ParameterException.class, () -> {
            rentManager.rentItem(beginTime, client, item);
        }, "Item should not be available after being rented.");
    }
}