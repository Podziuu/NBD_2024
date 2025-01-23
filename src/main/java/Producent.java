import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import models.Rent;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class Producent {
    private static KafkaProducer<Long, String> kafkaProducer;
    private static final String RENT_TOPIC = "rents";
    private static final String RENTAL_COMPANY_NAME = "Mediastore";
    private static final ObjectMapper objectMapper = createObjectMapper();

    public Producent() throws ExecutionException, InterruptedException {
        initProducer();
        createTopic();
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(ObjectId.class, new JsonSerializer<ObjectId>() {
            @Override
            public void serialize(ObjectId objectId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(objectId.toHexString());
            }
        });
        module.addDeserializer(ObjectId.class, new StdDeserializer<ObjectId>(ObjectId.class) {
            @Override
            public ObjectId deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                return new ObjectId(jsonParser.getText());
            }
        });
        mapper.registerModule(module);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    private static void initProducer() {
        Properties producerConfig = new Properties();
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerConfig.put(ProducerConfig.CLIENT_ID_CONFIG, "local");
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192,kafka2:9292,kafka3:9392");
        producerConfig.put(ProducerConfig.ACKS_CONFIG, "all");
        producerConfig.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");

        kafkaProducer = new KafkaProducer<>(producerConfig);
    }

    public void sendRent(Rent rent) {
        try {
            RentWithCompany rentWithCompany = new RentWithCompany(rent, RENTAL_COMPANY_NAME);
            String rentJson = objectMapper.writeValueAsString(rentWithCompany);

            Long rentIdAsLong = convertObjectIdToLong(rent.getId());
            ProducerRecord<Long, String> record = new ProducerRecord<>(RENT_TOPIC, rentIdAsLong, rentJson);

            kafkaProducer.send(record, this::onCompletion);
        } catch (JsonProcessingException e) {
            System.err.println("Error sending message: " + e.getCause().getMessage());
        }
    }

    private Long convertObjectIdToLong(ObjectId objectId) {
        return Math.abs((long) objectId.hashCode());
    }

    private void onCompletion(RecordMetadata metadata, Exception exception) {
        if (exception == null) {
            System.out.println("Message sent to partition: " + metadata.partition() + ", offset: " + metadata.offset());
        } else {
            System.err.println("Error sending message: " + exception.getMessage());
        }
    }

    public void createTopic() throws InterruptedException {
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192,kafka2:9292,kafka3:9392");

        int partitionsNumber = 3;
        short replicationFactor = 3;

        try (Admin admin = Admin.create(properties)) {
            NewTopic newTopic = new NewTopic(RENT_TOPIC, partitionsNumber, replicationFactor);
            CreateTopicsOptions options = new CreateTopicsOptions()
                    .timeoutMs(1000)
                    .validateOnly(false)
                    .retryOnQuotaViolation(true);
            CreateTopicsResult result = admin.createTopics(List.of(newTopic), options);
            KafkaFuture<Void> futureResult = result.values().get(RENT_TOPIC);
            futureResult.get();
        } catch (ExecutionException e) {
            System.err.println("Error creating topic: " + e.getCause().getMessage());
        }
    }

    static class RentWithCompany {
        private Rent rent;
        private String rentalCompany;

        public RentWithCompany() {}

        @JsonCreator
        public RentWithCompany(@JsonProperty("rent") Rent rent,
                               @JsonProperty("rentalCompany") String rentalCompany) {
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