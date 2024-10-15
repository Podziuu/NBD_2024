//package managersTests;
//
//import exceptions.ParameterException;
//import managers.ClientManager;
//import managers.ClientTypeManager;
//import managers.RentManager;
//import models.*;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityManagerFactory;
//import jakarta.persistence.Persistence;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class RentManagerTest {
//
//    private ClientManager clientManager;
//    private RentManager rentManager;
//    private EntityManagerFactory entityManagerFactory;
//
//    @BeforeEach
//    void setUp() {
//        ClientTypeManager clientTypeManager = new ClientTypeManager();
//        clientManager = new ClientManager();
//        clientTypeManager.createBasicClientTypes();
//        entityManagerFactory = Persistence.createEntityManagerFactory("default");
//        rentManager = new RentManager();
//    }
//
//    @AfterEach
//    void tearDown() {
//        entityManagerFactory.close();
//    }
//
//    @Test
//    void rentItem_ShouldCreateRentSuccessfully() throws ParameterException {
//        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
//            entityManager.getTransaction().begin();
//
//            clientManager.createClient("John", "Doe", "DiamondMembership");
//            Client client = clientManager.getClient(1);
//            Item item = new Item(1, 100, "Laptop");
//            entityManager.persist(client);
//            entityManager.persist(item);
//            entityManager.getTransaction().commit();
//
//            Rent rent = rentManager.rentItem("R1", LocalDateTime.now(), client, item);
//
//            assertNotNull(rent);
//            assertEquals("R1", rent.getRentId());
//            assertFalse(item.isAvailable());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    void returnItem_ShouldMarkItemAsReturned() throws ParameterException {
//        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
//            entityManager.getTransaction().begin();
//
//            clientManager.createClient("Jane", "Smith", "DiamondMembership");
//            Client client = clientManager.getClient(1);
//            Item item = new Item(2, 150, "Camera");
//            entityManager.persist(client);
//            entityManager.persist(item);
//            entityManager.getTransaction().commit();
//
//            Rent rent = rentManager.rentItem("R2", LocalDateTime.now(), client, item);
//
//            rentManager.returnItem(item);
//
//            assertTrue(item.isAvailable(), "Przedmiot powinien być dostępny po zwróceniu");
//            assertTrue(rent.isArchive(), "Wynajem powinien być zarchiwizowany");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    void getAllClientRents_ShouldReturnCorrectRents() throws ParameterException {
//        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
//            entityManager.getTransaction().begin();
//
//            clientManager.createClient("Alice", "Johnson", "BasicMembership");
//            Client client1 = clientManager.getClient(1);
//            clientManager.createClient("Bob", "Williams", "BasicMembership");
//            Client client2 = clientManager.getClient(2);
//
//            Item item1 = new Item(3, 200, "Phone");
//            Item item2 = new Item(4, 300, "Tablet");
//            entityManager.persist(client1);
//            entityManager.persist(client2);
//            entityManager.persist(item1);
//            entityManager.persist(item2);
//            entityManager.getTransaction().commit();
//
//            entityManager.getTransaction().begin();
//            Rent rent1 = rentManager.rentItem("R3", LocalDateTime.now(), client1, item1);
//            Rent rent2 = rentManager.rentItem("R4", LocalDateTime.now(), client2, item2);
//            entityManager.persist(rent1);
//            entityManager.persist(rent2);
//            entityManager.getTransaction().commit();
//
//            List<Rent> rentsForClient1 = rentManager.getAllClientRents(client1);
//
//            assertEquals(1, rentsForClient1.size(), "Powinien być tylko jeden wynajem dla klienta 1");
//            assertEquals("R3", rentsForClient1.getFirst().getRentId(), "ID wynajmu powinno być R3");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    void find_ShouldReturnCorrectRent() throws ParameterException {
//        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
//            entityManager.getTransaction().begin();
//
//            clientManager.createClient("Charlie", "Brown", "BasicMembership");
//            Client client = clientManager.getClient(1);
//
//            Item item = new Item(5, 400, "Monitor");
//            entityManager.persist(client);
//            entityManager.persist(item);
//            entityManager.getTransaction().commit();
//
//            Rent rent = rentManager.rentItem("R5", LocalDateTime.now(), client, item);
//
//            Rent foundRent = rentManager.find("R5").orElse(null);
//
//            assertNotNull(foundRent, "Wynajem powinien być znaleziony");
//            assertEquals("R5", foundRent.getRentId(), "ID wynajmu powinno być R5");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    void getAllArchiveRents_ShouldReturnOnlyArchivedRents() throws ParameterException {
//        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
//            entityManager.getTransaction().begin();
//
//            clientManager.createClient("David", "Clark", "BasicMembership");
//            Client client = clientManager.getClient(1);
//
//            Item item1 = new Item(6, 500, "Headphones");
//            entityManager.persist(client);
//            entityManager.persist(item1);
//            entityManager.getTransaction().commit();
//
//            Rent rent = rentManager.rentItem("R6", LocalDateTime.now(), client, item1);
//            rent.setArchive(true);
//
//            try (EntityManager em = entityManagerFactory.createEntityManager()) {
//                em.getTransaction().begin();
//                em.persist(rent);
//                em.getTransaction().commit();
//            }
//
//            List<Rent> archivedRents = rentManager.getAllArchiveRents();
//
//            assertEquals(1, archivedRents.size(), "Powinien być tylko jeden zarchiwizowany wynajem");
//            assertEquals("R6", archivedRents.getFirst().getRentId(), "ID wynajmu powinno być R6");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
