package com.project.consumer.listener;

import com.project.consumer.service.InvocationCounterService;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import static com.project.consumer.util.Helper.info;

@Service
public class EventKafkaListener {

    private final Environment env;
    private final InvocationCounterService invocationCounterService;

    public EventKafkaListener(Environment env, InvocationCounterService invocationCounterService) {
        this.env = env;
        this.invocationCounterService = invocationCounterService;
    }

    @KafkaListener(topics = "#{'${spring.kafka.consumer.consumer-1.properties.topics}'.split(',')}",
        groupId = "${spring.kafka.consumer.consumer-1.group-id}",
        containerFactory = "consumer_1_ContainerFactory")
    public void listener_1(@Payload String message,
                           @Header(KafkaHeaders.RECEIVED_KEY) String key,
                           @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                           @Header(KafkaHeaders.RECEIVED_PARTITION) String partition,
                           @Header(KafkaHeaders.OFFSET) String offset) {
        processMessages("consumer-1", message, key, topic, partition, offset);
    }

    @KafkaListener(topics = "#{'${spring.kafka.consumer.consumer-2.properties.topics}'.split(',')}",
        groupId = "${spring.kafka.consumer.consumer-2.group-id}",
        containerFactory = "consumer_2_ContainerFactory")
    public void listener_2(@Payload String message,
                           @Header(KafkaHeaders.RECEIVED_KEY) String key,
                           @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                           @Header(KafkaHeaders.RECEIVED_PARTITION) String partition,
                           @Header(KafkaHeaders.OFFSET) String offset) {
        processMessages("consumer-2", message, key, topic, partition, offset);
    }

    @KafkaListener(topics = "#{'${spring.kafka.consumer.consumer-3.properties.topics}'.split(',')}",
        groupId = "${spring.kafka.consumer.consumer-3.group-id}",
        containerFactory = "consumer_3_ContainerFactory")
    public void listener_3(@Payload String message,
                           @Header(KafkaHeaders.RECEIVED_KEY) String key,
                           @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                           @Header(KafkaHeaders.RECEIVED_PARTITION) String partition,
                           @Header(KafkaHeaders.OFFSET) String offset) {
        processMessages("consumer-3", message, key, topic, partition, offset);
    }

    @KafkaListener(topics = "#{'${spring.kafka.consumer.consumer-4.properties.topics}'.split(',')}",
        groupId = "${spring.kafka.consumer.consumer-4.group-id}",
        containerFactory = "consumer_4_ContainerFactory")
    public void listener_4(@Payload String message,
                           @Header(KafkaHeaders.RECEIVED_KEY) String key,
                           @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                           @Header(KafkaHeaders.RECEIVED_PARTITION) String partition,
                           @Header(KafkaHeaders.OFFSET) String offset) {
        processMessages("consumer-4", message, key, topic, partition, offset);
    }

    private void processMessages(String consumerName, String message, String key, String topic, String partition, String offset) {
        String consumer = getConsumerGroupId(consumerName);
        info(String.format("MESSAGE RECEIVED : consumer=[%s], message=[%s], key=[%s], topic=[%s], partition=[%s], offset=[%s]",
                consumer, message, key, topic, partition, offset));
        invocationCounterService.countInvocation(topic, key);
    }

    private String getConsumerGroupId(String name) {
        return env.getProperty("spring.kafka.consumer." + name + ".group-id");
    }

}
