import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Konsument{
    private final List<KafkaConsumer<Long, String>> kafkaConsumers = new ArrayList<>();
    private static final String RENT_TOPIC = "rents";
    private final int numberOfConsumers;
    private final Repository repository;

    public Konsument(int numberOfConsumers){
        this.numberOfConsumers = numberOfConsumers;
        this.repository = new Repository();
    }

    public void init() {
        Properties consumerConfig = new Properties();
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.LongDeserializer");
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, "group-rents");
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192,kafka2:9292,kafka3:9392");
        consumerConfig.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        if (kafkaConsumers.isEmpty()) {
            for (int i = 0; i < numberOfConsumers; i++) {
                KafkaConsumer<Long, String> kafkaConsumer = new KafkaConsumer<>(consumerConfig);
                kafkaConsumer.subscribe(Collections.singleton(RENT_TOPIC));
                kafkaConsumers.add(kafkaConsumer);
                System.out.println("Subscribed to " + RENT_TOPIC);
                System.out.println("Created consumer " + (i + 1));
            }
        }
    }

    public void consume(KafkaConsumer<Long, String> consumer) {
        try {
            consumer.poll(Duration.ofMillis(0));
            Set<TopicPartition> consumerAssignment = consumer.assignment();
            consumer.seekToBeginning(consumerAssignment);

            Duration timeout = Duration.of(100, ChronoUnit.MILLIS);
            MessageFormat formatter = new MessageFormat("Consumer {5}, Topic {0}, Partition {1}," +
                    " Offset {2, number, integer}, Key {3}, Value {4}");

            while (true) {
                ConsumerRecords<Long, String> records = consumer.poll(timeout);

                for (ConsumerRecord<Long, String> record : records) {
                    String result = formatter.format(new Object[]{
                            record.topic(),
                            record.partition(),
                            record.offset(),
                            record.key(),
                            record.value(),
                            consumer.groupMetadata().memberId()
                    });

                    repository.save(record.value());
                    System.out.println(result);

                    consumer.commitAsync();
                }
            }
        } catch (WakeupException we) {
            System.out.println("Consumer shutdown requested.");
        } catch (Exception e) {
            System.err.println("Error in consumer: " + e.getMessage());
        } finally {
            consumer.close();
        }
    }

    public void consumeTopicByEveryone() {
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfConsumers);
        for (KafkaConsumer<Long, String> consumer : kafkaConsumers) {
            executorService.execute(() -> consume(consumer));
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (KafkaConsumer<Long, String> consumer : kafkaConsumers) {
                consumer.wakeup();
            }
            executorService.shutdown();
            System.out.println("Consumer shutdown.");
        }));
    }
}
