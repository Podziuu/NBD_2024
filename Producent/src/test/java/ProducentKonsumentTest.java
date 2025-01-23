import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import config.MongoEntity;
import managers.ClientManager;
import managers.ItemManager;
import managers.RentManager;
import models.Client;
import models.Item;
import models.Rent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repos.ClientRepository;
import repos.ItemRepository;
import repos.RentRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProducentKonsumentTest {
    private static Producent producer;
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
    private static CountDownLatch latch1;
    private static CountDownLatch latch2;

    @BeforeAll
    static void setUp() throws ExecutionException, InterruptedException {
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

        producer = new Producent();

        latch1 = new CountDownLatch(1);
        latch2 = new CountDownLatch(1);
    }

    @AfterAll
    static void tearDown() {
    }

    @Test
    void testProducentAndKonsument() throws InterruptedException, ExecutionException {
        // Tworzenie wypożyczenia
        ObjectId clientId = clientManager.addClient("Robert", "Kolonko", 123456789, "DiamondMembership");
        Client client = clientManager.getClient(clientId);
        ObjectId itemId = itemManager.addComics(100, "Comics", 100);
        Item item = itemManager.getItem(itemId);

        LocalDateTime beginTime = LocalDateTime.now();

        Rent rent = new Rent();
        rent.setId(new ObjectId());
        rent.setClient(client);
        rent.setBeginTime(beginTime);
        rent.setItem(item);

        producer.sendRent(rent);


        Konsument konsument1 = new Konsument("localhost:9192,localhost:9292,localhost:9392", "test-group", "rents", latch1);
        konsument1.startConsuming();

        Konsument konsument2 = new Konsument("localhost:9192,localhost:9292,localhost:9392", "test-group", "rents", latch2);
        konsument2.startConsuming();


        Rent savedRent = rentRepository.getRent(rent.getId());
        assertNotNull(savedRent, "Rent was not found in the database.");

        assertTrue(latch1.await(30, TimeUnit.SECONDS), "Message was not processed by the first consumer.");
        assertTrue(latch2.await(30, TimeUnit.SECONDS), "Message was not processed by the second consumer.");
    }
}
