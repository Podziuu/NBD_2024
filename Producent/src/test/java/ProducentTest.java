//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//import config.MongoEntity;
//import managers.ClientManager;
//import managers.ItemManager;
//import managers.RentManager;
//import models.Client;
//import models.Item;
//import org.apache.kafka.clients.consumer.*;
//import org.apache.kafka.common.serialization.LongDeserializer;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import models.Rent;
//import org.bson.types.ObjectId;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import repos.ClientRepository;
//import repos.ItemRepository;
//import repos.RentRepository;
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.Properties;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.TimeUnit;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class ProducentTest {
//    private static Producent producer;
//    private static KafkaConsumer<Long, String> consumer;
//
//    private static MongoEntity mongoEntity;
//    private static MongoDatabase database;
//    private static MongoCollection<Rent> rentCollection;
//    private static MongoCollection<Client> clientCollection;
//    private static MongoCollection<Item> itemCollection;
//    private static RentRepository rentRepository;
//    private static ClientRepository clientRepository;
//    private static ItemRepository itemRepository;
//    private static RentManager rentManager;
//    private static ClientManager clientManager;
//    private static ItemManager itemManager;
//
//    @BeforeAll
//    static void setUp() throws ExecutionException, InterruptedException {
//        mongoEntity = new MongoEntity();
//        database = mongoEntity.getDatabase();
//        rentCollection = database.getCollection("rents", Rent.class);
//        clientCollection = database.getCollection("clients", Client.class);
//        itemCollection = database.getCollection("items", Item.class);
//
//        rentRepository = new RentRepository(rentCollection);
//        clientRepository = new ClientRepository(clientCollection);
//        itemRepository = new ItemRepository(itemCollection);
//
//        clientManager = new ClientManager(clientRepository);
//        itemManager = new ItemManager(itemRepository);
//        rentManager = new RentManager(rentRepository, clientManager, itemManager);
//
//        producer = new Producent();
//
//        Properties consumerProps = new Properties();
//        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192,kafka2:9292,kafka3:9392");
//        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
//        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
//        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        consumer = new KafkaConsumer<>(consumerProps);
//        consumer.subscribe(Collections.singletonList("rents"));
//    }
//
//    @AfterAll
//    public static void tearDown() {
//        consumer.close();
//    }
//
//    @Test
//    void testSendRent() throws InterruptedException {
//        ObjectId clientId = clientManager.addClient("Robert", "Kolonko", 123456789, "DiamondMembership");
//        Client client = clientManager.getClient(clientId);
//        ObjectId itemId = itemManager.addComics(100, "Comics", 100);
//        Item item = itemManager.getItem(itemId);
//
//        LocalDateTime beginTime = LocalDateTime.now();
//        ObjectId rentId = rentManager.rentItem(beginTime, client, item);
//        Rent rent = rentManager.getRent(rentId);
//
//        producer.sendRent(rent);
//
//        CountDownLatch latch = new CountDownLatch(1);
//
//        Thread consumerThread = new Thread(() -> {
//            try {
//                ConsumerRecords<Long, String> records = consumer.poll(Duration.ofSeconds(20));
//                for (ConsumerRecord<Long, String> recordReceived : records) {
//                    System.out.println("Received record with key: " + recordReceived.key() + " and value: " + recordReceived.value());
//                    if (recordReceived.key().equals(rentId.hashCode() * 1L)) {
//                        try {
//                            ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
//                            Producent.RentWithCompany receivedRent = objectMapper.readValue(recordReceived.value(), Producent.RentWithCompany.class);
//                            assertEquals(rent.getId(), receivedRent.getRent().getId());
//                            assertEquals(rent.getClient().getFirstName(), receivedRent.getRent().getClient().getFirstName());
//                            assertEquals(rent.getItem().getItemName(), receivedRent.getRent().getItem().getItemName());
//                            assertEquals("Mediastore", receivedRent.getRentalCompany());
//                        } catch (JsonProcessingException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                if (records.isEmpty()) {
//                    System.out.println("No records received in this poll interval.");
//                }
//            } finally {
//                latch.countDown();
//            }
//        });
//        consumerThread.start();
//        latch.await(60, TimeUnit.SECONDS);
//    }
//}