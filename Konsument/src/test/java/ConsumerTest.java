//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.apache.kafka.common.serialization.LongDeserializer;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//
//import java.util.Collections;
//import java.util.Properties;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.TimeUnit;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//public class ConsumerTest {
//    private static KafkaConsumer<Long, String> consumer;
//    private static CountDownLatch latch;
//    private static Konsument konsument;
//
//    @BeforeAll
//    static void setUp() {
//        Properties consumerProps = new Properties();
//        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192,kafka2:9292,kafka3:9392");
//        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
//        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
//        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//
//        consumer = new KafkaConsumer<>(consumerProps);
//        consumer.subscribe(Collections.singletonList("rents"));
//
//        konsument = new Konsument("kafka1:9192,kafka2:9292,kafka3:9392", "test-group", "rents");
//
//        latch = new CountDownLatch(1);
//    }
//
//    @AfterAll
//    static void tearDown() {
//        consumer.close();
//    }
//
//    @Test
//    void consumerTest() throws InterruptedException {
//        new Thread(() -> {
//            konsument.startConsuming();
//            latch.countDown();
//        }).start();
//
//        assertTrue(latch.await(30, TimeUnit.SECONDS), "Message was not received by the consumer.");
//    }
//
//    @Test
//    void consumerTwoTest() throws InterruptedException {
//        consumerTest();
//    }
//}
