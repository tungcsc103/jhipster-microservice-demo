package vn.pops.taskservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TaskServiceKafkaConsumer {

    private final Logger log = LoggerFactory.getLogger(TaskServiceKafkaConsumer.class);
    private static final String TOPIC = "topic_taskservice";

    @KafkaListener(topics = "topic_taskservice", groupId = "group_id")
    public void consume(String message) throws IOException {
        log.info("Consumed message in {} : {}", TOPIC, message);
    }
}
