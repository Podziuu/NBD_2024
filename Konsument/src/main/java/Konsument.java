import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import config.MongoEntity;

import models.Rent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import repos.RentRepository;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Konsument {
    private final KafkaConsumer<Long, String> consumer;
    private final ObjectMapper objectMapper;
    private static MongoCollection<Rent> rentCollection;
    private static RentRepository rentRepository;
    private final CountDownLatch latch;

    public Konsument(String bootstrapServers, String groupId, String topic, CountDownLatch latch) {
        this.latch = latch;

        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(Collections.singletonList(topic));

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();

        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoEntity mongoEntity = new MongoEntity();
        MongoDatabase database = mongoEntity.getDatabase().withCodecRegistry(pojoCodecRegistry);
        rentCollection = database.getCollection("rents", Rent.class);
        rentRepository = new RentRepository(rentCollection);

    }

    public void startConsuming() {
        int messageLimit = 10;
        try {
            int processedMessages = 0;
            while (processedMessages < messageLimit) {
                ConsumerRecords<Long, String> records = consumer.poll(Duration.ofSeconds(10));
                if (records.isEmpty()) {
                    System.out.println("No records found, polling again...");
                    continue;
                }
                for (ConsumerRecord<Long, String> record : records) {
                    processedMessages++;
                    System.out.printf("Received message: key = %d, value = %s, partition = %d, offset = %d%n",
                            record.key(), record.value(), record.partition(), record.offset());

                    try {
                        RentWithCompany receivedRentWithCompany = objectMapper.readValue(record.value(), RentWithCompany.class);
                        System.out.println("Deserialized RentWithCompany: " + receivedRentWithCompany.getRent().getId() + ", " +
                                receivedRentWithCompany.getRent().getClient().getFirstName() + ", " +
                                receivedRentWithCompany.getRent().getItem().getItemName() + ", " +
                                receivedRentWithCompany.getRentalCompany());

                        Rent rent = receivedRentWithCompany.getRent();
                        rentRepository.addRent(rent);
                        System.out.println("Rent saved to database: " + rent.getId());

                        latch.countDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } finally {
            consumer.close();
        }
    }


    public static class RentWithCompany {
        private Rent rent;
        private String rentalCompany;

        public RentWithCompany() {}

        public RentWithCompany(Rent rent, String rentalCompany) {
            this.rent = rent;
            this.rentalCompany = rentalCompany;
        }

        public Rent getRent() {
            return rent;
        }

        public String getRentalCompany() {
            return rentalCompany;
        }
    }
}
