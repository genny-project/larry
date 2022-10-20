package io.gada;

import io.gada.message.UserData;
import io.gada.message.UserMessage;
import io.quarkus.kafka.client.serialization.ObjectMapperSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import java.util.Properties;

public class KafkaDataGeneratorTest {

    public static final String TOPIC_USER = "user";
    public static final String TOPIC_USER_ACCESS = "user-access";

    public static Producer<Integer, UserMessage> getkafkaProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "kafka-test");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ObjectMapperSerializer.class.getName());

        return new KafkaProducer<>(props);
    }

    public static void produceUsers() {
        Producer<Integer, UserMessage> producer = getkafkaProducer();

        producer.send(new ProducerRecord<>(TOPIC_USER_ACCESS, 1, new UserMessage("code1", "jti1")));
        producer.send(new ProducerRecord<>(TOPIC_USER_ACCESS, 2, new UserMessage("code2", "jti2")));
        producer.send(new ProducerRecord<>(TOPIC_USER_ACCESS, 3, new UserMessage("code3", "jti3")));
    }
}
